package com.example.demo.controller;

import com.example.demo.controller.result.CommonResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/sayHello")
    public CommonResult<?> sayHello() {
        return CommonResult.successResult("hello!");
    }

    @GetMapping("/sayGoodBye")
    public CommonResult<?> sayGoodBye() {
        return CommonResult.successResult("goodbye!");
    }
}
