package com.mycompany.sistemkepegawaian.security;

import org.springframework.stereotype.Service;

@Service
public class VulnerableAccessService {

    public boolean checkAccess(String role, String action) {
        // Tidak ada validasi role
        return true;
    }
}
