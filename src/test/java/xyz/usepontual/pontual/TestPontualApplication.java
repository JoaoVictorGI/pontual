package xyz.usepontual.pontual;

import org.springframework.boot.SpringApplication;

public class TestPontualApplication {

    static void main(String[] args) {
        SpringApplication.from(PontualApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}
