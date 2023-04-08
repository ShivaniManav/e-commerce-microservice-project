package com.sm.core.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SMServiceExceptionResponse {

    private String message;

    private Integer responseCode;

    private Integer errorCode;

}
