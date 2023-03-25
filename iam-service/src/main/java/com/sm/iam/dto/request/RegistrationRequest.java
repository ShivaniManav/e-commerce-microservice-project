package com.sm.iam.dto.request;

import com.sm.iam.entity.Role;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegistrationRequest {

    @NotNull
    private String username;

    @NotNull
    private String password;

    private String firstName;

    private String lastName;

    @NotNull
    private String email;

    @NotNull
    private String mobile;

    private Collection<Role> roles;

}
