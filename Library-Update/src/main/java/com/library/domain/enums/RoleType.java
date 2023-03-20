package com.library.domain.enums;

public enum RoleType {

    ROLE_STAFF("Staff"),
    ROLE_MEMBER("Member"),
    ROLE_ADMIN("Administrator");

    private String name;

    RoleType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}