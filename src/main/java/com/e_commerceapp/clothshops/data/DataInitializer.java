package com.e_commerceapp.clothshops.data;

import com.e_commerceapp.clothshops.data.model.Users;
import com.e_commerceapp.clothshops.data.repository.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        createNew5DefaultUsersIfNotExist();
    }

    private void createNew5DefaultUsersIfNotExist() {
        for (int i = 1 ; i<=5 ; i++){
            String defaultEmail = "user"+i+"@gmail.com";
            if (userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            Users user = new Users();
            user.setFirstName("The User");
            user.setLastName("User" + i);
            user.setEmail(defaultEmail);
            user.setPassword("123456");
            userRepository.save(user);
            System.out.println("Default " + i + " created successfully");
        }
    }
}

