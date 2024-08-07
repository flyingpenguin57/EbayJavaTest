package com.example.demo.controller;

import com.example.demo.controller.request.AddUserRequest;
import com.example.demo.controller.result.CommonResult;
import com.example.demo.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @PostMapping("/addUser")
    public CommonResult<?> addUser(@RequestBody AddUserRequest addUserRequest) throws IOException {

        adminService.addUserAccess(addUserRequest);

        return CommonResult.successResultWithoutData();
    }

    @GetMapping("test")
    public String test() {
        return  "test";
    }

}
