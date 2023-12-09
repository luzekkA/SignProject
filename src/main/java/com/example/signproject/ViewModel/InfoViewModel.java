package com.example.signproject.ViewModel;

import com.example.signproject.Utils.Roles;
import lombok.Data;

import java.util.List;

@Data
public class InfoViewModel {
    private String id;
    private String username;
    private String phone;
    private List<Roles> roles;
    private String ico;
}
