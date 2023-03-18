package com.sm.iam.dto.request;

import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginRequest {

    @NotNull
    private String username;

    @NotNull
    private String password;

}
