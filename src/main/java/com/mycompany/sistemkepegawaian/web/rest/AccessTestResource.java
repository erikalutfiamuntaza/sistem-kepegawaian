package com.mycompany.sistemkepegawaian.web.rest;

import com.mycompany.sistemkepegawaian.security.AccessControlService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/access")
public class AccessTestResource {

    public AccessTestResource(AccessControlService accessControlService) {
        System.out.println("ACCESS CONTROLLER LOADED");
        this.AccessControlService = accessControlService;
    }

    @GetMapping("/employee")
    public String employeeAccess() {
        boolean allowed = accessControlService.checkAccess("ROLE_EMPLOYEE", "VIEW_SELF");

        if (allowed) {
            return "Employee boleh lihat data sendiri";
        }

        return "Forbidden";
    }

    @GetMapping("/salary")
    public String salaryAccess() {
        boolean allowed = accessControlService.checkAccess("ROLE_EMPLOYEE", "MANAGE_SALARY");

        if (allowed) {
            return "Boleh akses gaji";
        }

        return "Forbidden";
    }
}
