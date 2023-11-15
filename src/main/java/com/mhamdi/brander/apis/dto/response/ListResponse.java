package com.mhamdi.brander.apis.dto.response;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListResponse implements DataInterface {
    private List<?> items;
}
