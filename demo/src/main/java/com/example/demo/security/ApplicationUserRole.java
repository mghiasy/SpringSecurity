package com.example.demo.security;

import java.util.Set;
import com.google.common.collect.Sets;

import static com.example.demo.security.ApplicationUserPermission.*;

public enum  ApplicationUserRole {
    STUDENT(Sets.newHashSet()), //creates empty set => Role Student doesn't have any permission
    ADMIN(Sets.newHashSet(COURSE_READ,COURDE_WRITE,STUDENT_READ,STUDENT_WRITE));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }
}
