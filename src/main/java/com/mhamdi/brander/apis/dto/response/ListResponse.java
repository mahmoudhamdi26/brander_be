package com.mhamdi.brander.apis.dto.response;

import java.util.List;

import com.mhamdi.brander.apis.dto.response.DataInterface;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListResponse implements DataInterface {
    private List<?> items;
}
