package com.mhamdi.brander.apis.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private String name;
    private String username;
    private String password;
}
