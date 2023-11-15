package com.mhamdi.brander.apis.controllers;

import com.mhamdi.brander.apis.dto.request.NewUserRequest;
import com.mhamdi.brander.apis.dto.request.SignUpRequest;
import com.mhamdi.brander.apis.dto.response.AppResponse;
import com.mhamdi.brander.apis.dto.response.ListResponse;
import com.mhamdi.brander.exceptions.apis.ApplicationException;
import com.mhamdi.brander.exceptions.apis.Errors;
import com.mhamdi.brander.services.intrfaces.StorageService;
import com.mhamdi.db.models.Template;
import com.mhamdi.db.models.User;
import com.mhamdi.db.models.UserTemplates;
import com.mhamdi.db.models.enums.AccountType;
import com.mhamdi.db.repos.TemplateRepository;
import com.mhamdi.db.repos.UsersRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
class UsersController {
    @Autowired
    private final UsersRepository repository;
    @Autowired
    private final TemplateRepository templates_repository;
    private final PasswordEncoder passwordEncoder;    
    private final StorageService storageService;


    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/users")
    AppResponse all() {
        List<User> items = repository.findAll();
        var data = ListResponse.builder().items(items).build();
        var res = AppResponse.builder().data(data).status(1).message("success").build();
        return res;
    }
    // end::get-aggregate-root[]

    @PostMapping("/users")
    AppResponse newUser(@RequestBody NewUserRequest request) {
        try{            
            var user = User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .email(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(AccountType.USER)
                .build();
            var item = repository.save(user);

            request.getTemplates().forEach( (templateid) -> {
                Template template = templates_repository.findById(templateid).orElseThrow(
                    () -> new ApplicationException(Errors.ID_NOT_FOUND, Map.of("id", templateid))
                );
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
                item.getTemplates().add(
                    UserTemplates.builder()
                    .template(template)
                    .fileid(userTemplateFileID)
                    .filename(template.getFilename())
                    .filepath(destinationPath.toString())
                    .filesize(template.getFilesize())
                    .filetype(template.getFiletype())
                    .user(item)
                    .build()                 
                );
                repository.save(item);
                // UserTemplates.builder().build()
            });
            // item.getTemplates().add(null)
            var res = AppResponse.builder().data(item).status(1).message("success").build();
            return res;
        }
        catch (RuntimeException ex){
            ex.printStackTrace();
            throw new ApplicationException(Errors.CREATE_ERROR, ex.getCause());
        }
    }

    // Single item

    @GetMapping("/users/{id}")
    User one(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ApplicationException(Errors.USER_NOT_FOUND, Map.of("id", id)));
    }

    @PutMapping("/users/{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable Long id) {

        return repository.findById(id)
                .map(user -> {
                    user.setName(newUser.getName());
                    user.setEmail(newUser.getEmail());
                    return repository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return repository.save(newUser);
                });
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id) {
        repository.deleteById(id);
    }
}