package com.e_commerceapp.clothshops.data;

import com.e_commerceapp.clothshops.model.Users;
import com.e_commerceapp.clothshops.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component  // Do not forget component
//this one to make action when application run
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // here we will create 5 user when app run to help in test
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

