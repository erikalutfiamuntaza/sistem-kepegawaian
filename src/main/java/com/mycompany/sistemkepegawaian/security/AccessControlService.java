package com.mycompany.sistemkepegawaian.security;

import org.springframework.stereotype.Service;

@Service
public class AccessControlService {

    public boolean checkAccess(String role, String action) {
        if (role.equals("ROLE_ADMIN")) {
            return true;
        }

        if (role.equals("ROLE_HRD")) {
            return action.equals("MANAGE_EMPLOYEE") || action.equals("MANAGE_SALARY");
        }

        if (role.equals("ROLE_MANAGER")) {
            return action.equals("APPROVE_LEAVE") || action.equals("VIEW_PERFORMANCE");
        }

        if (role.equals("ROLE_EMPLOYEE")) {
            return action.equals("VIEW_SELF");
        }

        return false;
    }
}
