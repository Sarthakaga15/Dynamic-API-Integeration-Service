package com.cloudeagle.integration.controller;

import com.cloudeagle.integration.entity.FetchedUser;
import com.cloudeagle.integration.repository.FetchedUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private FetchedUserRepository userRepository;

    @GetMapping
    public List<FetchedUser> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{integrationName}")
    public List<FetchedUser> getUsersByIntegration(@PathVariable String integrationName) {
        // We would need to add a method to repository for this, or filter
        // For now, let's just return all
        return userRepository.findAll().stream()
                .filter(u -> u.getIntegrationName().equals(integrationName))
                .toList();
    }
}
