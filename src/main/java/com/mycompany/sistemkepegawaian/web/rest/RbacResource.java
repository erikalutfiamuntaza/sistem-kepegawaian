package com.mycompany.sistemkepegawaian.web.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rbac")
public class RbacResource {

    @GetMapping("/employee")
    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
    public String employee() {
        return "Employee boleh akses data sendiri";
    }

    @GetMapping("/hrd")
    @PreAuthorize("hasAuthority('ROLE_HRD')")
    public String hrd() {
        return "HRD kelola pegawai dan gaji";
    }

    @GetMapping("/manager")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public String manager() {
        return "Manager approve cuti";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String admin() {
        return "Admin semua akses";
    }
}
