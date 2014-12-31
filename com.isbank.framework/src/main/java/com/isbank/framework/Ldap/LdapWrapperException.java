package com.isbank.framework.Ldap;

/**
 * Created by TK57720 on 13.03.2014.
 */
public class LdapWrapperException extends Exception {
    public LdapWrapperException(String message) {
        super(message);
    }

    public LdapWrapperException(String message, Throwable throwable) {
        super(message, throwable);
    }
}