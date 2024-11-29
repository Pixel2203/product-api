package com.example.firstrestapi.permission;

import java.util.Arrays;

public class PermissionHelper {

    static boolean hasPermission(String[] permissions, Permissions requiredPermission){
        String requiredPermissionString = requiredPermission.name().toUpperCase();
        return Arrays.stream(permissions).map(String::toUpperCase).anyMatch(s -> s.equals(requiredPermissionString));
    }
}
