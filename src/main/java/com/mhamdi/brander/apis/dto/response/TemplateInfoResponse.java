package com.mhamdi.brander.apis.dto.response;

import java.util.Vector;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TemplateInfoResponse implements DataInterface {
    private Vector<String> layers;
}
