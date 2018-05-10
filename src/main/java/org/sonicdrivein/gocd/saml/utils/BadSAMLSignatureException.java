package org.sonicdrivein.gocd.saml.utils;


public class BadSAMLSignatureException extends Exception {
    BadSAMLSignatureException(String s) {
        super(s);
    }
}
