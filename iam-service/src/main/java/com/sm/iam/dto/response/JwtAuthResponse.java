package com.sm.iam.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JwtAuthResponse {

    private String jwtToken;

}
