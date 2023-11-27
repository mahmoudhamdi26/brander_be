package com.mhamdi.brander.services.impl;

import com.mhamdi.brander.apis.dto.request.NewUserRequest;
import com.mhamdi.brander.apis.resources.UserInfo;
import com.mhamdi.brander.exceptions.apis.ApplicationException;
import com.mhamdi.brander.exceptions.apis.Errors;
import com.mhamdi.brander.services.intrfaces.StorageService;
import com.mhamdi.brander.services.intrfaces.UserService;
import com.mhamdi.db.models.Template;
import com.mhamdi.db.models.User;
import com.mhamdi.db.models.UserTemplates;
import com.mhamdi.db.models.enums.AccountType;
import com.mhamdi.db.repos.TemplateRepository;
import com.mhamdi.db.repos.UserTemplatesRepository;
import com.mhamdi.db.repos.UsersRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Getter
    @Autowired
    private final UsersRepository repository;
    @Autowired
    private final TemplateRepository templates_repository;
    @Autowired
    private final UserTemplatesRepository userTemplatesRepository;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;

    @Override
    public User createUser(NewUserRequest request) {
        var user = User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .email(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(AccountType.USER)
                .build();
        var item = repository.save(user);

        request.getTemplates().forEach((templateid) -> {
            Template template = templates_repository.findById(templateid).orElseThrow(
                    () -> new ApplicationException(Errors.ID_NOT_FOUND, Map.of("id", templateid)));
            File sourceFile = storageService.getFile(template.getFileid());
            String userTemplateFileID = "USER_" + item.getId().toString() + "_" + template.getFileid();
            Path destinationPath = storageService.load(userTemplateFileID)
                    .normalize().toAbsolutePath();
            try {
                Files.copy(sourceFile.toPath(), destinationPath);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ApplicationException(Errors.DISK_ERROR, e.getCause());
            }

            var ut = UserTemplates.builder()
                    .template(template)
                    .title(template.getTitle())
                    .fileid(userTemplateFileID)
                    .filename(template.getFilename())
                    .filepath(destinationPath.toString())
                    .filesize(template.getFilesize())
                    .filetype(template.getFiletype())
                    .user(item)
                    .build();
            // userTemplatesRepository.save(ut);
            item.getTemplates().add(ut);
            repository.save(item);
        });

        return item;
    }

    @Override
    public List<User> allUsers() {
        List<User> items = repository.findAll();
        return items;
    }

    @Override
    public UserInfo getUserInfo(Long id) {
        var user = repository.findById(id)
                .orElseThrow(() -> new ApplicationException(Errors.USER_NOT_FOUND, Map.of("id", id)));
        UserInfo result = new UserInfo();
        result.setEmail(user.getEmail());
        result.setUsername(user.getUsername());
        user.getTemplates().forEach(userTemplates -> {
            result.getTemplates().add(userTemplates);
        });

        return result;
    }
}
