package com.example.demo.security;

public enum ApplicationUserPermission {
    STUDENT_READ("student:read"),// represent this as student:read
    STUDENT_WRITE("student:write"),
    COURSE_READ("course:read"),
    COURDE_WRITE("course:write");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
