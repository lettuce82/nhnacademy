package com.nhnacademy.nhnmart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class NhnMartApplication {

    public static void main(String[] args) {
        SpringApplication.run(NhnMartApplication.class, args);
    }

}
