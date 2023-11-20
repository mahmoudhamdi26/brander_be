package com.mhamdi.brander.apis.controllers;

import java.io.File;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mhamdi.brander.apis.dto.response.AppResponse;
import com.mhamdi.brander.apis.dto.response.ListResponse;
import com.mhamdi.brander.apis.dto.response.TemplateInfoResponse;
import com.mhamdi.brander.exceptions.apis.ApplicationException;
import com.mhamdi.brander.exceptions.apis.Errors;
import com.mhamdi.brander.services.intrfaces.StorageService;
import com.mhamdi.core.svg.MHsvg;
import com.mhamdi.db.models.Template;
import com.mhamdi.db.repos.TemplateRepository;

@RestController
public class TemplatesController {
    private final TemplateRepository repository;
    private final StorageService storageService;

    TemplatesController(TemplateRepository repository, StorageService storageService) {
        this.repository = repository;
        this.storageService = storageService;
    }

    @GetMapping("api/v1/templates")
    AppResponse all() {
        List<Template> items = repository.findAll();
        var data = ListResponse.builder().items(items).build();
        var res = AppResponse.builder().data(data).status(1).message("success").build();
        return res;
    }

    @PostMapping("api/v1/templates")
    @CrossOrigin
    AppResponse create(@RequestParam("file") MultipartFile multipartFile,
            @RequestParam("title") String title) {
        try {
            String originalFileName = multipartFile.getOriginalFilename();
            Long filesize = multipartFile.getSize();
            File savedFile = storageService.save(multipartFile);

            Template item = Template.builder()
                    .title(title)
                    .fileid(savedFile.getName())
                    .filename(originalFileName)
                    .filepath(savedFile.getAbsolutePath())
                    .filesize(filesize)
                    .filetype(multipartFile.getContentType())
                    .build();
            item = repository.save(item);
            var res = AppResponse.builder().data(item).status(1).message("success").build();
            return res;
        } catch (RuntimeException ex) {
            throw new ApplicationException(Errors.CREATE_ERROR);
        }
    }

    @GetMapping("api/v1/templates/info/{id}")
    AppResponse getInfo(@RequestParam("id") Long id) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Template template = repository.findById(id).get();
        File templateFile = storageService.getFile(template.getFileid());

        MHsvg svg = new MHsvg(templateFile);
        var layers = svg.listSvgLayers();

        TemplateInfoResponse data = TemplateInfoResponse.builder().layers(layers).build();
        var res = AppResponse.builder().data(data).status(1).message("success").build();
        return res;
    }

    @GetMapping("api/v1/templates/edit/{id}")
    AppResponse SetData(@PathVariable("id") Long id, @RequestParam("title") String title) {
        Template template = repository.findById(id).get();
        File templateFile = storageService.getFile(template.getFileid());

        MHsvg svg = new MHsvg(templateFile);
        var layers = svg.listSvgLayers();

        TemplateInfoResponse data = TemplateInfoResponse.builder().layers(layers).build();
        var res = AppResponse.builder().data(data).status(1).message("success").build();
        return res;
    }
}
