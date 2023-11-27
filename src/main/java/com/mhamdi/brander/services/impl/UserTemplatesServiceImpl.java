package com.mhamdi.brander.services.impl;

import com.mhamdi.brander.apis.dto.response.TemplateInfoResponse;
import com.mhamdi.brander.services.intrfaces.StorageService;
import com.mhamdi.brander.services.intrfaces.UserTemplatesService;
import com.mhamdi.core.svg.MHsvg;
import com.mhamdi.db.models.UserTemplates;
import com.mhamdi.db.repos.UserTemplatesRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

import javax.xml.transform.TransformerException;

@Service
@RequiredArgsConstructor
public class UserTemplatesServiceImpl implements UserTemplatesService {
    @Getter
    @Autowired
    private final UserTemplatesRepository repository;
    private final StorageService storageService;

    @Override
    public TemplateInfoResponse getInfo(Long id) {
        UserTemplates template = repository.findById(id).get();
        File templateFile = storageService.getFile(template.getFileid());
        MHsvg svg = new MHsvg(templateFile);
        var layers = svg.listSvgLayers();

        TemplateInfoResponse data = TemplateInfoResponse.builder()
                .template_id(template.getTemplate().getId())
                .filesize(template.getFilesize())
                .fileid(template.getFileid())
                .filepath(template.getFilepath())
                .filename(template.getFilename())
                .id(template.getId())
                .filetype(template.getFiletype())
                .layers(layers)
                .build();
        return data;
    }

    @Override
    public TemplateInfoResponse getInfoByFileId(String fid) {
        File templateFile = storageService.getFile(fid);
        MHsvg svg = new MHsvg(templateFile);
        var layers = svg.listSvgLayers();

        TemplateInfoResponse data = TemplateInfoResponse.builder().layers(layers).build();
        return data;
    }

    @Override
    public void saveLayerValue(Long id, String layerId, String val) {
        UserTemplates template = repository.findById(id).get();
        File templateFile = storageService.getFile(template.getFileid());
        MHsvg svg = new MHsvg(templateFile);
        svg.changeTextLayer(layerId, val);
        try {
            svg.saveDocument(templateFile);
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
