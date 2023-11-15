package com.mhamdi.brander.apis.dto.response;

import com.mhamdi.db.models.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthenticationResponse implements DataInterface {
    private String token;
    private User user;
}
