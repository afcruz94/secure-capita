package com.afcruz.securecapita.enums;

public enum VerificationType {
    ACCOUNT("ACCOUNT"),
    PASSWORD("PASSWORD");

    private final String type;

    VerificationType(String password) {
        this.type = password;
    }

    public String getType() {
        return this.type.toLowerCase();
    }
}
