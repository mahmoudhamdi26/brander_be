package com.mhamdi.brander.apis.dto.response;

import java.util.Vector;

import com.mhamdi.brander.apis.dto.response.DataInterface;
import com.mhamdi.core.svg.models.Layer;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TemplateInfoResponse implements DataInterface {
    private Long id;
    private String fileid;
    private String filepath;
    private String filename;
    private String filetype;
    private Long filesize;
    private Long user_id;
    private Long template_id;

    private Vector<Layer> layers;
    private Vector<Layer> txt_layers;
}
