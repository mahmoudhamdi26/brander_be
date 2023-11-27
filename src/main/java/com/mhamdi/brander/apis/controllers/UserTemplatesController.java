package com.mhamdi.brander.apis.controllers;

import com.mhamdi.brander.apis.dto.response.AppResponse;
import com.mhamdi.brander.apis.dto.response.ListResponse;
import com.mhamdi.brander.apis.dto.response.TemplateInfoResponse;
import com.mhamdi.brander.exceptions.apis.ApplicationException;
import com.mhamdi.brander.exceptions.apis.Errors;
import com.mhamdi.brander.services.intrfaces.StorageService;
import com.mhamdi.brander.services.intrfaces.UserTemplatesService;
import com.mhamdi.core.svg.MHsvg;
import com.mhamdi.db.models.Template;
import com.mhamdi.db.repos.TemplateRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user-templates")
public class UserTemplatesController {
    private final UserTemplatesService service;
    private final StorageService storageService;

    @GetMapping("/{id}")
    AppResponse getInfo(@PathVariable("id") Long id) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        TemplateInfoResponse data = service.getInfo(id);
        var res = AppResponse.builder().data(data).status(1).message("success").build();
        return res;
    }

    @GetMapping("/file/{fid}")
    AppResponse openTemplateFile(@PathVariable String fid) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        TemplateInfoResponse data = service.getInfoByFileId(fid);
        var res = AppResponse.builder().data(data).status(1).message("success").build();
        return res;
    }

    @GetMapping("/edit-layer/{id}")
    AppResponse SetData(@PathVariable("id") Long id, @RequestParam("layer_name") String layer_name,
            @RequestParam("layer_val") String layer_val) {
        service.saveLayerValue(id, layer_name, layer_val);
        var res = AppResponse.builder().data(null).status(1).message("success").build();
        return res;
    }
}
