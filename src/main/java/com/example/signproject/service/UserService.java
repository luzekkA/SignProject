package com.example.signproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.ViewModel.RegisterViewModel;
import com.example.signproject.ViewModel.UpdateUserViewModel;
import com.example.signproject.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;

public interface UserService extends IService<User> {
    ResultJson<Object> loginDate(User login);

    User getUserById(String id);

    ResultJson<Object> getUserByToken(String token);

    ResultJson<Object> register(RegisterViewModel register);

    ResultJson<Object> addOtherInfo(RegisterViewModel register);

    ResultJson<Object> getRoleList();

    ResultJson<Object> updateUser(UpdateUserViewModel user);

    ResultJson<Object> uploadIco(MultipartFile file, Serializable id) throws IOException;

}
