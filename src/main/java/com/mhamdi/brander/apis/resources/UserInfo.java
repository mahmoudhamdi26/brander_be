package com.mhamdi.brander.apis.resources;

import com.mhamdi.brander.apis.dto.response.DataInterface;
import com.mhamdi.db.models.UserTemplates;
import com.mhamdi.db.models.enums.AccountType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserInfo implements DataInterface {
    private String username;
    private String email;
    private AccountType role;
    List<UserTemplates> templates = new ArrayList<>();
}
