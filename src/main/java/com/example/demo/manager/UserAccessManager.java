package com.example.demo.manager;

import com.example.demo.service.model.UserAccess;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserAccessManager {

    public List<UserAccess> readUserAccessFromFile(File file, ObjectMapper objectMapper) throws IOException {
        if (file.exists() && file.length() > 0) {
            return objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, UserAccess.class));
        }

        return new ArrayList<>();
    }
}
