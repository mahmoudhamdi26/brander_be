package com.mhamdi.core.svg.models;

import com.mhamdi.brander.apis.dto.response.DataInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Layer implements Serializable, DataInterface {
    String id;
    String val;
    Map<String, String> attrs = new HashMap<>();
}