package com.example.signproject.ViewModel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetTeamUserViewModel {
    private String id;
    private String user;
    private String role;
    private String phone;
}
