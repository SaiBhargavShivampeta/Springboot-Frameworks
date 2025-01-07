package com.example.demo;

import org.springframework.batch.item.ItemProcessor;


public class MyProcessor implements ItemProcessor<User, User> {

    @Override
    public User process(User user) throws Exception {
        user.setName(user.getName().toUpperCase());
        return user; // Return the processed User object
    }
}
