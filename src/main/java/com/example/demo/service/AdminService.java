package com.example.demo.service;

import com.example.demo.controller.request.AddUserRequest;
import com.example.demo.manager.UserAccessManager;
import com.example.demo.service.model.UserAccess;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class AdminService {

    @Resource
    private UserAccessManager userAccessManager;

    public void addUserAccess(AddUserRequest addUserRequest) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Path filePath = Paths.get(System.getProperty("user.dir"), "user_access.json");
        File file = filePath.toFile();

        List<UserAccess> userAccesses = userAccessManager.readUserAccessFromFile(file, objectMapper);

        userAccesses.add(new UserAccess(addUserRequest.getUserId(), addUserRequest.getEndpoint()));

        saveToFile(objectMapper, file, userAccesses);
    }

    private void saveToFile(ObjectMapper objectMapper, File file, List<UserAccess> userAccesses) throws IOException {
        objectMapper.writeValue(file, userAccesses);
    }

}
