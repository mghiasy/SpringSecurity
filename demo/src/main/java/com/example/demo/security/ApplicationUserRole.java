package com.example.demo.security;

import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static com.example.demo.security.ApplicationUserPermission.*;

public enum  ApplicationUserRole {
    STUDENT(Sets.newHashSet()), //creates empty set => Role Student doesn't have any permission
    ADMIN(Sets.newHashSet(COURSE_READ,COURDE_WRITE,STUDENT_READ,STUDENT_WRITE)),
    ADMINTRAINEE(Sets.newHashSet(COURSE_READ,STUDENT_READ));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }
    public Set<SimpleGrantedAuthority> getGrantedAuthority(){
        //convert every permissions in the list to new SimpleGrantedAuthority and add Role to the list and return
        Set<SimpleGrantedAuthority> authoroties = getPermissions()
                .stream().map(permissions -> new SimpleGrantedAuthority(permissions.getPermission()))
                .collect(Collectors.toSet());
        //add role to the list of permissions also
        authoroties.add(new SimpleGrantedAuthority("ROLE_"+ this.name()));
        return authoroties;
    }
}
