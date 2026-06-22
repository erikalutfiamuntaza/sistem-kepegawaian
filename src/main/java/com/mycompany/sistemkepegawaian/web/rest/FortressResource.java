package com.mycompany.sistemkepegawaian.web.rest;

import com.mycompany.sistemkepegawaian.security.AccessControlService;
import com.mycompany.sistemkepegawaian.security.VulnerableAccessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fortress")
public class FortressResource {

    private final AccessControlService accessControlService;
    private final VulnerableAccessService vulnerableAccessService;

    public FortressResource(AccessControlService accessControlService, VulnerableAccessService vulnerableAccessService) {
        this.accessControlService = accessControlService;
        this.vulnerableAccessService = vulnerableAccessService;
    }

    // =========================
    // EMPLOYEE boleh lihat data sendiri
    // =========================
    @GetMapping("/employee")
    public ResponseEntity<String> employee() {
        boolean allowed = accessControlService.checkAccess("ROLE_EMPLOYEE", "VIEW_SELF");

        if (!allowed) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        return ResponseEntity.ok("Employee boleh lihat data sendiri");
    }

    // =========================
    // HARDENED MODE
    // EMPLOYEE tidak boleh akses salary
    // =========================
    @GetMapping("/salary")
    public ResponseEntity<String> salary() {
        boolean allowed = accessControlService.checkAccess("ROLE_EMPLOYEE", "MANAGE_SALARY");

        if (!allowed) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        return ResponseEntity.ok("Salary accessed");
    }

    @GetMapping("/hrd")
    public ResponseEntity<String> hrd() {
        boolean allowed = accessControlService.checkAccess("ROLE_HRD", "MANAGE_SALARY");

        if (!allowed) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        return ResponseEntity.ok("HRD boleh mengelola gaji");
    }

    // =========================
    // VULNERABLE MODE
    // EMPLOYEE bisa akses salary
    // =========================
    @GetMapping("/vulnerable/salary")
    public ResponseEntity<String> vulnerableSalary() {
        boolean allowed = vulnerableAccessService.checkAccess("ROLE_EMPLOYEE", "MANAGE_SALARY");

        if (!allowed) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        return ResponseEntity.ok("EMPLOYEE bisa akses salary");
    }

    @GetMapping("/manager")
    public ResponseEntity<String> manager() {
        boolean allowed = accessControlService.checkAccess("ROLE_MANAGER", "APPROVE_LEAVE");

        if (!allowed) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        return ResponseEntity.ok("Manager approve leave");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> admin() {
        boolean allowed = accessControlService.checkAccess("ROLE_ADMIN", "DELETE_DATABASE");

        if (!allowed) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        return ResponseEntity.ok("Admin full access");
    }

    @GetMapping("/vulnerable/admin")
    public ResponseEntity<String> vulnerableAdmin() {
        boolean allowed = vulnerableAccessService.checkAccess("ROLE_EMPLOYEE", "DELETE_DATABASE");

        if (!allowed) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        return ResponseEntity.ok("EMPLOYEE berhasil akses admin");
    }
}
