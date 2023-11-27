package com.mhamdi.brander.services.intrfaces;

import com.mhamdi.brander.apis.dto.request.NewUserRequest;
import com.mhamdi.brander.apis.dto.response.TemplateInfoResponse;
import com.mhamdi.brander.apis.resources.UserInfo;
import com.mhamdi.db.models.User;
import com.mhamdi.db.repos.UsersRepository;

import java.util.List;

public interface UserTemplatesService {
    TemplateInfoResponse getInfo(Long id);

    TemplateInfoResponse getInfoByFileId(String fid);

    void saveLayerValue(Long id, String layerId, String val);
}
