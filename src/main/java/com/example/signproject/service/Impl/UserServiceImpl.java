package com.example.signproject.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.signproject.Enum.RolesEnum;
import com.example.signproject.Utils.FileUtil;
import com.example.signproject.Utils.JwtUtil;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.ViewModel.InfoViewModel;
import com.example.signproject.ViewModel.RegisterViewModel;
import com.example.signproject.ViewModel.UpdateUserViewModel;
import com.example.signproject.entity.User;
import com.example.signproject.mapper.UserMapper;
import com.example.signproject.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public ResultJson<Object> loginDate(User login) {
        try {
            User user = getUserById(login.getId());
            if (user.getId().equals(login.getId())) {
                if (user.getPassword().equals(login.getPassword())) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("token", JwtUtil.generateToken(login));
                    return ResultJson.success(map);
                }
            }
            return ResultJson.error("没有用户");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("全部查询出现异常");
            return ResultJson.error(log.toString());
        }
    }

    @Override
    public User getUserById(String id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public ResultJson<Object> getUserByToken(String token) {
        try {
            User user = this.baseMapper.selectById(JwtUtil.getId(token));
            if (user != null) {
                InfoViewModel infoViewModel = new InfoViewModel();
                infoViewModel.setId(user.getId());
                infoViewModel.setUsername(user.getUsername());
                infoViewModel.setPhone(user.getPhone());
                infoViewModel.setRoles(Collections.singletonList(user.getRoles()));
                infoViewModel.setIco(FileUtil.getObjectUrl(user.getIco()));
                return ResultJson.success(infoViewModel);
            }
            return ResultJson.error("没有用户");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("全部查询出现异常");
            return ResultJson.error(log.toString());
        }
    }

    @Override
    public ResultJson<Object> register(RegisterViewModel register) {
        try {
            if (register.getId() != null) {
                User user = getUserById(register.getId());
                if (user == null) {
                    user = new User();
                    if (register.getPassword().equals(register.getConfirmpassword()) &&
                            (register.getRole().equals("player")
                                    || register.getRole().equals("leader")
                                    || register.getRole().equals("coach")
                            )) {
                        user.setId(register.getId());
                        user.setUsername(register.getName());
                        user.setRoles(RolesEnum.valueOf(register.getRole()));
                        user.setPassword(register.getPassword());
                        user.setPhone(register.getPhone());
                        this.baseMapper.insert(user);
                        return ResultJson.success();
                    }
                    return ResultJson.error("非法输入");
                }
                return ResultJson.error("账号存在");
            }
            return ResultJson.error("身份证为空");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("全部查询出现异常");
            return ResultJson.error(log.toString());
        }
    }

    @Override
    public ResultJson<Object> addOtherInfo(RegisterViewModel register) {
        try {
            if (register.getId() != null) {
                User user = getUserById(register.getId());
                if (user == null) {
                    user = new User();
                    if (register.getPassword().equals(register.getConfirmpassword()) &&
                            (register.getRole().equals("player") ||
                                    register.getRole().equals("leader"))) {
                        user.setId(register.getId());
                        user.setUsername(register.getName());
                        user.setRoles(RolesEnum.valueOf(register.getRole()));
                        user.setPassword(register.getPassword());
                        user.setPhone(register.getPhone());
                        this.baseMapper.insert(user);
                        return ResultJson.success();
                    }
                    return ResultJson.error("非法输入");
                }
                return ResultJson.error("账号存在");
            }
            return ResultJson.error("身份证为空");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("全部查询出现异常");
            return ResultJson.error(log.toString());
        }
    }

    @Override
    public ResultJson<Object> getRoleList() {
        return ResultJson.success(RolesEnum.values());
    }

    @Override
    public ResultJson<Object> updateUser(UpdateUserViewModel user) {
        try {
            User update = this.baseMapper.selectById(user.getId());
            if (user.getUsername() != null) {
                update.setUsername(user.getUsername());
            }
            if (user.getPhone() != null) {
                update.setPhone(user.getPhone());
            }
            //update.setPassword(user.getPassword());
            if (this.baseMapper.updateById(update) != 0) {
                return ResultJson.success();
            } else {
                return ResultJson.error("数据更新失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("全部查询出现异常");
            return ResultJson.error(log.toString());
        }
    }

    @Override
    public ResultJson<Object> uploadIco(MultipartFile file, Serializable id) {
        User user = this.getUserById(id.toString());
        if (FileUtil.isImage(file).getCode() == 20000 && user != null) {
            //MultipartFile upload = file;
            String filename = id
                    + Objects.requireNonNull(file
                            .getOriginalFilename())
                    .substring(file.getOriginalFilename().lastIndexOf("."));
            ResultJson<Object> resultJson = FileUtil.uploadFileToMinio(file, filename);
            if (resultJson.getCode() == 20000) {
//                user.setIco(resultJson.getData().toString());
                user.setIco(filename);
            } else {
                return resultJson;
            }
            if (this.updateById(user)) {
                return ResultJson.success();
            }
            return ResultJson.error("数据库信息更新失败");
        }
        return ResultJson.error("文件为空");
    }
}
