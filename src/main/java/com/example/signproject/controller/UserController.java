package com.example.signproject.controller;

import com.example.signproject.Utils.JwtUtil;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.ViewModel.RegisterViewModel;
import com.example.signproject.ViewModel.UpdateUserViewModel;
import com.example.signproject.entity.User;
import com.example.signproject.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/login")
    public ResultJson<Object> Login(@RequestBody User login) {
        return this.userService.loginDate(login);
    }

    @PostMapping("register")
    public ResultJson<Object> Register(@RequestBody RegisterViewModel register) {
        return this.userService.register(register);
    }

    @GetMapping("/info")
    public ResultJson<Object> Info(@RequestParam("token") String token) {
        return this.userService.getUserByToken(token);
    }

    @PostMapping("/logout")
    public ResultJson<Object> Logout() {
        return ResultJson.success();
    }

    @GetMapping("/rolelist")
    public ResultJson<Object> RoleList() {
        return this.userService.getRoleList();
    }

    @PutMapping("/update")
    public ResultJson<Object> Update(@RequestBody UpdateUserViewModel user) {
        return this.userService.updateUser(user);
    }

    @PostMapping("/ico")
    public ResultJson<Object> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("token") String token) throws IOException {
        //String token = request.getHeader("token");
        String id = JwtUtil.getId(token);
        return this.userService.uploadIco(file, id);
    }
}