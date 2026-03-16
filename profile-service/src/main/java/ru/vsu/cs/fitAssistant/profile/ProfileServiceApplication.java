package ru.vsu.cs.fitAssistant.profile;

import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.vsu.cs.fitAssistant.profile.service.ProfileService;

@SpringBootApplication
public class ProfileServiceApplication {
    public static void main(String[] args){
        SpringApplication.run(ProfileServiceApplication.class, args);
    }

}
