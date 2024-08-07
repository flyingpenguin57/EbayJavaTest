package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.example.demo.service.model.UserAccess;
import com.example.demo.util.Base64Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@SpringBootTest
public class AdminControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() throws IOException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Path filePath = Paths.get(System.getProperty("user.dir"), "user_access.json");
        File file = filePath.toFile();
        file.delete();
        boolean newFile = file.createNewFile();
    }

    @AfterEach
    public void clean() throws IOException {
        Path filePath = Paths.get(System.getProperty("user.dir"), "user_access.json");
        File file = filePath.toFile();
        file.delete();
        boolean newFile = file.createNewFile();
    }


    @Test
    public void testGetUserSuccess() throws Exception {

        //prepare token
        Map<String, String> adminInfo = new HashMap<>();
        adminInfo.put("userId", "123456");
        adminInfo.put("accountName", "123456");
        adminInfo.put("role", "admin");
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("userId", "1234567");
        userInfo.put("accountName", "1234567");
        userInfo.put("role", "user");

        String adminString = JSON.toJSONString(adminInfo);
        String adminToken = Base64Util.encodeBase64(adminString);

        String userString = JSON.toJSONString(userInfo);
        String userToken = Base64Util.encodeBase64(userString);

        //1.user invoke sayHello without admin authorize, will fail
        mockMvc.perform(get("/user/sayHello").header("token", userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));

        //2.admin authorize user to access sayHello
        UserAccess userAccess = new UserAccess();
        userAccess.setUserId("1234567");
        userAccess.setEndpoint(Collections.singletonList("sayHello"));
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(
                post("/admin/addUser").header("token", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userAccess)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        //3.user invoke sayHello success
        mockMvc.perform(get("/user/sayHello").header("token", userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
