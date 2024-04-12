package pl.sda.carrental.configuration.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("jpassword"));
        System.out.println(encoder.encode("dpassword"));
        System.out.println(encoder.encode("ppassword"));
        System.out.println(encoder.encode("mpassword"));
        System.out.println(encoder.encode("password1"));
        System.out.println(encoder.encode("changedPassword"));
    }
}
