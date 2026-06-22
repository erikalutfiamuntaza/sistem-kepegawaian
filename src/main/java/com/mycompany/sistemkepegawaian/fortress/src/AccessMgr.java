package com.mycompany.sistemkepegawaian.fortress;

public class AccessMgr {

    public boolean checkAccess(Session session, Permission permission) {
        String role = session.getRole();
        String perm = permission.getName();

        if (role.equals("ADMIN")) return true;

        if (role.equals("EMPLOYEE")) return perm.equals("VIEW_SELF");

        if (role.equals("HRD")) return perm.equals("MANAGE_EMPLOYEE") || perm.equals("MANAGE_SALARY");

        if (role.equals("MANAGER")) return perm.equals("APPROVE_LEAVE") || perm.equals("VIEW_PERFORMANCE");

        return false;
    }
}
