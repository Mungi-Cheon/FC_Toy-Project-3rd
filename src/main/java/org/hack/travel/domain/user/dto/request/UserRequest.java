package org.hack.travel.domain.user.dto.request;

import lombok.Getter;

@Getter
public class UserRequest {

    private String email;
    private String username;
    private String password;
}
