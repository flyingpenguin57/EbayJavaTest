package com.example.demo.interceptor;

import com.example.demo.exception.BusinessException;
import com.example.demo.manager.UserAccessManager;
import com.example.demo.service.model.UserAccess;
import com.example.demo.service.model.UserInfo;
import com.example.demo.util.Base64Util;
import com.example.demo.util.RoleConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Resource
    private UserAccessManager userAccessManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("start authorization...");

        //get token from request header
        String token = request.getHeader("token");
        //get userInfo from header
        String decodedToken = Base64Util.decodeBase64(token);
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfo userInfo = objectMapper.readValue(decodedToken, UserInfo.class);

        String requestURI = request.getRequestURI();

        //admin can access all resources
        if (StringUtils.equals(userInfo.getRole(), RoleConstants.ADMIN)) {
            return true;
        } else if (StringUtils.equals(userInfo.getRole(), RoleConstants.USER)) {
            //user cannot access admin api
            if (requestURI.startsWith("/admin")) {
                throw new BusinessException("no access.");
            } else if (requestURI.startsWith("/user")) {
                List<String> userAccessEndpoints = getUserAccessEndpoints(userInfo.getUserId());
                if (userAccessEndpoints.isEmpty()) {
                    throw new BusinessException("no access.");
                }
                String[] split = requestURI.split("/");
                if (!userAccessEndpoints.contains(split[split.length-1])) {
                    throw new BusinessException("no access.");
                }
                return true;
            } else {
                throw new BusinessException("illeagle url");
            }
        } else {
            throw new BusinessException("illeagle role");
        }
    }

    private List<String> getUserAccessEndpoints(String userId) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Path filePath = Paths.get(System.getProperty("user.dir"), "user_access.json");
        File file = filePath.toFile();
        List<UserAccess> userAccesses = userAccessManager.readUserAccessFromFile(file, objectMapper);
        for (UserAccess userAccess : userAccesses) {
            if (StringUtils.equals(userAccess.getUserId(), userId)) {
                return userAccess.getEndpoint();
            }
        }

        return new ArrayList<>();
    }
}
