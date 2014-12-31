package com.isbank.bkkob.izleme;

/**
 * Created by TK57720 on 13.03.2014.
 */
public class LdapParam {
    private String ldapUrl="";
    private int ldapPort = 0;
    private int ldapSsl = 0;

    public String getLdapUrl() {
        return this.ldapUrl;
    }
    public void setLdapUrl(String ldapUrl) {
        this.ldapUrl = ldapUrl;
    }

    public int getLdapPort() {
        return this.ldapPort;
    }
    public void setLdapPort(int ldapPort) {
        this.ldapPort = ldapPort;
    }

    public int getLdapSsl() { return this.ldapSsl; }
    public void setLdapSsl(int ldapSsl) {
        this.ldapSsl = ldapSsl;
    }
}
