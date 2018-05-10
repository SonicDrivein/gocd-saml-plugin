package org.sonicdrivein.gocd.saml.annotation;

public enum FieldType {
    STRING {
        @Override
        public String validate(String value) {
            return null;
        }
    };

    public abstract String validate(String value);
}
