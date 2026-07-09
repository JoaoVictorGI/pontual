package xyz.usepontual.pontual;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

public class ModularityTests {
    ApplicationModules modules = ApplicationModules.of(PontualApplication.class);

    @Test
    void verifiesArchitecture() {
        modules.verify();
    }

    @Test
    void createDocumentation() {
        new Documenter(modules).writeDocumentation();
    }
}
