package net.infomi.session;

import net.infomi.common.starters.annotations.EnableSessionAutoConfiguration;
import net.infomi.common.starters.annotations.EnableTracerAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("net.infomi.session.*")
@EnableAutoConfiguration
@EnableSessionAutoConfiguration
@EnableTracerAutoConfiguration
public class SessionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SessionApplication.class, args);
    }

}
