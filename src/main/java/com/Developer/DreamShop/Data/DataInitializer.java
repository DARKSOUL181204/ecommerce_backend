package com.Developer.DreamShop.Data;


import com.Developer.DreamShop.model.User;
import com.Developer.DreamShop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {


    private final UserRepository userRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
            createDefaultUserIfNotExists();
    }

    private void createDefaultUserIfNotExists() {
        for(int i = 0;i<=5;i++){
            String defaultEmail = "user"+i+"@gmail.com";
            if(userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user = new User();
            user.setFirstName("The user");
            user.setLastName("User"+i);
            user.setEmail(defaultEmail);
            user.setPassword("12345");
            userRepository.save(user);
            System.out.println("Default vet user " + i  +" created successfully !");

        }
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
