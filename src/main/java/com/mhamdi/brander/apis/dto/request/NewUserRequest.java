package com.mhamdi.brander.apis.dto.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewUserRequest {
    private String name;
    private String username;
    private String password;
    // private AccountType role;
    private List<Long> templates;
}
