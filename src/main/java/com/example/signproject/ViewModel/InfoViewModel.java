package com.example.signproject.ViewModel;

import com.example.signproject.Enum.RolesEnum;
import lombok.Data;

import java.util.List;

@Data
public class InfoViewModel {
    private String id;
    private String username;
    private String phone;
    private List<RolesEnum> roles;
    private String ico;
}
