package com.mycompany.sistemkepegawaian;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/admin")
    public String admin() {
        return "ADMIN ACCESS";
    }

    @GetMapping("/hrd")
    public String hrd() {
        return "HRD ACCESS";
    }

    @GetMapping("/manager")
    public String manager() {
        return "MANAGER ACCESS";
    }

    @GetMapping("/pegawai")
    public String pegawai() {
        return "SEMUA ROLE BISA";
    }
}
