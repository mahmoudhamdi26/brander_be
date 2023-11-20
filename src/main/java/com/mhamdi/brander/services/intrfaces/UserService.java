package com.mhamdi.brander.services.intrfaces;

import com.mhamdi.brander.apis.dto.request.NewUserRequest;
import com.mhamdi.brander.apis.resources.UserInfo;
import com.mhamdi.db.models.User;
import com.mhamdi.db.repos.UsersRepository;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    User createUser(NewUserRequest request);
    List<User> allUsers();
    UserInfo getUserInfo(Long id);

    UsersRepository getRepository();

}
