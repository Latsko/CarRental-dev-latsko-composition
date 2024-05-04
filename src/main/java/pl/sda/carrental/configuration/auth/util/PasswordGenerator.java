package pl.sda.carrental.configuration.auth.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("Apassword"));
        System.out.println(encoder.encode("Mpassword"));
        System.out.println(encoder.encode("Epassword"));
        System.out.println(encoder.encode("Cpassword"));
        System.out.println(encoder.encode("password1"));
        System.out.println(encoder.encode("changedPassword"));
    }
}
