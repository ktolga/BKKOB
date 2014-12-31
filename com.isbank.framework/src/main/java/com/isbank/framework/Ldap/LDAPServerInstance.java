package com.isbank.framework.Ldap;
/*
 * Copyright 2010 Daniel Weisser
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


        import java.io.Serializable;
        import java.util.ArrayList;
        import java.util.List;
        import javax.net.SocketFactory;

        import android.util.Log;
        import com.unboundid.ldap.sdk.ExtendedResult;
        import com.unboundid.ldap.sdk.LDAPConnection;
        import com.unboundid.ldap.sdk.LDAPConnectionOptions;
        import com.unboundid.ldap.sdk.LDAPException;
        import com.unboundid.ldap.sdk.ResultCode;
        import com.unboundid.ldap.sdk.extensions.StartTLSExtendedRequest;
        import com.unboundid.util.ssl.SSLUtil;
        import com.unboundid.util.ssl.TrustAllTrustManager;
        import com.unboundid.ldap.sdk.RootDSE;
        import com.unboundid.ldap.sdk.SearchResult;
        import com.unboundid.ldap.sdk.BindResult;
        import com.unboundid.ldap.sdk.SearchRequest;
        import com.unboundid.ldap.sdk.SearchResultEntry;
        import com.unboundid.ldap.sdk.SearchScope;
        import com.unboundid.ldap.sdk.ReadOnlyEntry;

/**
 * Encapsulates a LDAP directory server instance and provides methods to access the server. This is based on code from the UnboundID LDAP Client.
 *
 * @author <a href="mailto:daniel.weisser@gmx.de">Daniel Weisser</a>
 */
public final class LDAPServerInstance implements Serializable {

    private String[] PROPERTIES_TO_LOAD = new String[] { "uid", "givenname", "sn", "mudkod", "gmudkod", "dgmyil", "dgmay", "dgmgun", "unvkod", "ou", "dkod", "imzayetki", "title", "asilsube", "mail", "amir", "msid", "pozkod", "poztitle", "HRdurum", "gorkod" ,"bolumkod","serviskod","bolumsuzpozkod","ekipkod"};
    private static final String TAG = "LDAPServerInstance";

    /**
     * The serial version UID for this serializable class.
     */
    private static final long serialVersionUID = -7633400003887348205L;


    /**
     * The encryption method (0 - no encryption, 1 - SSL, 2 - StartTLS)
     */
    private final int encryption;

    /**
     * The host address of the LDAP server
     */
    private final String host;

    /**
     * The port number of the LDAP server
     */
    private final int port;

    /**
     * The DN to use to bind to the server.
     */
    private final String bindDN;

    /**
     * The password to use to bind to the server.
     */
    //private final String bindPW;

    /*
    private final String uid;

    private final String bindDNWithUid;
    */

    /**
     * Creates a new LDAP server instance with the provided information.
     *
     * @param host
     *            The address of the server. It must not be {@code null}.
     * @param port
     *            The port number for the server. It must be between 1 and 65535.
     * @param encryption
     *            The encryption method (0 - no encryption, 1 - SSL, 2 - StartTLS)
     * @param bindDN
     *            The DN to use to bind to the server. It may be {@code null} or empty if no authentication should be performed.
     * @param bindPW
     *            The password to use to bind to the server. It may be {@code null} or empty if no authentication should be performed.
     */
    public LDAPServerInstance(final String host, final int port, final int encryption, final String bindDN/*, final String uid, final String bindPW*/) {
        this.host = host;
        this.port = port;
        this.encryption = encryption;
        //this.uid = (uid == null) || (uid.trim().length()) == 0 ? "" : uid;

        this.bindDN = (bindDN == null) || (bindDN.length() == 0) ? null : bindDN;
        //this.bindPW = (bindPW == null) || (bindPW.length() == 0) ? null : bindPW;
        //this.bindDNWithUid = (bindDN == null) || (bindDN.length() == 0) ? null : String.format("uid=%s,%s", this.uid, this.bindDN) ;
    }

    /**
     * Retrieves an LDAP connection that may be used to communicate with the LDAP server.
     *
     * @return An LDAP connection that may be used to communicate with the LDAP server.
     *
     * @throws com.unboundid.ldap.sdk.LDAPException
     *             If a problem occurs while attempting to establish the connection.
     */
    public LDAPConnection getConnection(String userName, String password) throws LDAPException {
        Log.d(TAG, "Trying to connect to: " + toString());
        SocketFactory socketFactory = null;
        if (usesSSL()) {
            final SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager());
            try {
                socketFactory = sslUtil.createSSLSocketFactory();
            } catch (Exception e) {
                Log.e(TAG, "getConnection", e);
                throw new LDAPException(ResultCode.LOCAL_ERROR, "Cannot initialize SSL", e);
            }
        }

        final LDAPConnectionOptions options = new LDAPConnectionOptions();
        options.setAutoReconnect(true);
        options.setConnectTimeoutMillis(5000);
        options.setFollowReferrals(false);
        options.setMaxMessageSize(0);

        //final LDAPConnection conn = new LDAPConnection(socketFactory, options, this.host, this.port, this.bindDN, this.bindPW);
        final LDAPConnection conn = new LDAPConnection(socketFactory, options, this.host, this.port);
        if (usesStartTLS()) {
            final SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager());
            try {
                final ExtendedResult r = conn.processExtendedOperation(new StartTLSExtendedRequest(sslUtil.createSSLContext()));
                if (r.getResultCode() != ResultCode.SUCCESS) {
                    throw new LDAPException(r);
                }
            } catch (LDAPException le) {
                Log.e(TAG, "getConnection", le);
                conn.close();
                throw le;
            } catch (Exception e) {
                Log.e(TAG, "getConnection", e);
                conn.close();
                throw new LDAPException(ResultCode.CONNECT_ERROR, "Cannot initialize StartTLS", e);
            }
        }

        //if ((this.bindDN != null) && (this.bindPW != null)) {
        if ((this.bindDN != null) && (password != null)) {
            try {
                String bindDn =  getBindDn(userName);
                BindResult auth = conn.bind(bindDn, password);
                if (!auth.getResultCode().isConnectionUsable()) {
                    LdapWrapperException x = new LdapWrapperException("unable to bind user");
                    Log.e(TAG, "getConnection", x);
                    conn.close();
                }
            } catch (LDAPException le) {
                Log.e(TAG, "getConnection", le);
                conn.close();
                throw le;
            }
        }

        return conn;
    }

    private String getBindDn(String userName)
    {
        return (bindDN == null) || (bindDN.length() == 0) ? null : String.format("uid=%s,%s", userName, this.bindDN);
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("LDAPServer(host=\"");
        buffer.append(host).append(":").append(port).append("\"");
        buffer.append(", bindDN=\"");
        if (bindDN != null) {
            buffer.append(bindDN);
        }
        buffer.append("\" - ");
        if (usesSSL()) {
            buffer.append("SSL");
        } else if (usesStartTLS()) {
            buffer.append("StartTLS");
        } else {
            buffer.append("No encryption");
        }
        buffer.append(")");
        return buffer.toString();
    }

    public boolean usesSSL() {
        return encryption == 1;
    }

    public boolean usesStartTLS() {
        return encryption == 2;
    }

    public boolean authenticate(String userName, String password) {
        LDAPConnection connection = null;
        boolean retVal = false;

        try {
            connection = this.getConnection(userName, password);
            if (connection != null) {
                String bindDn =  getBindDn(userName);
                String filtre = String.format("(uid=%s)", userName);
                SearchRequest searchRequest = new SearchRequest(bindDn, SearchScope.BASE, filtre, "isMemberOf");
                searchRequest.setResponseTimeoutMillis(3000);
                SearchResult searchResult = connection.search(searchRequest);
                if(searchResult.getEntryCount() == 1)
                    retVal = true;
                else
                    retVal = false;

                /*
                SearchResult searchResult = connection.search(this.bindDN, SearchScope.SUB, filtre, PROPERTIES_TO_LOAD);
                int kayitSayisi = searchResult.getEntryCount();
                if (kayitSayisi > 0)
                    retVal = true;
                    */

                /*
                String[] baseDNs = null;
                if (s != null) {
                    baseDNs = s.getNamingContextDNs();
                }

                //sendResult(baseDNs, true, handler, context, null);
                return true;
                */
            }
        } catch (LDAPException e) {
            Log.e(TAG, "Error authenticating", e);
            //sendResult(null, false, handler, context, e.getMessage());
            retVal = false;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return retVal;
    }

    public List<EmployeeInfo> getEmployeeInfo(String userName, String password, String ldapQuery) {
        LDAPConnection connection = null;
        ArrayList<EmployeeInfo> retVal = new ArrayList<EmployeeInfo>();

        try {
            connection = this.getConnection(userName, password);
            if (connection != null) {
                String bindDn =  getBindDn(userName);
                //String filtre = String.format("(uid=%s)", user2Find);
                SearchRequest searchRequest = new SearchRequest(bindDn, SearchScope.BASE, ldapQuery, PROPERTIES_TO_LOAD);
                searchRequest.setResponseTimeoutMillis(3000);
                SearchResult searchResult = connection.search(searchRequest);
                if(searchResult.getEntryCount() > 0)
                {
                    for (SearchResultEntry user : searchResult.getSearchEntries()) {
//TODO: kayitlari donustur
                        EmployeeInfo tmp = getContactFromLdap(user);
                        if (tmp != null)
                            retVal.add(tmp);
                    }
                }
            }
        } catch (LDAPException e) {
            Log.e(TAG, "Error authenticating", e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return retVal;
    }

    private EmployeeInfo getContactFromLdap(ReadOnlyEntry user)
    {
        EmployeeInfo retVal = new EmployeeInfo();

        try {
            retVal.setFirstName((user.hasAttribute("givenname") ? user.getAttributeValue("givenname") : null));
            retVal.setCorporateNumber(user.hasAttribute("uid") ? Integer.parseInt(user.getAttributeValue("uid")) : 0);
            retVal.setLastName(user.hasAttribute("sn") ? user.getAttributeValue("sn") : null);
        }
         catch (Exception e) {
             Log.e(TAG, "Ldap contact error", e);
             retVal = null;
         }

        return retVal;
    }
}
