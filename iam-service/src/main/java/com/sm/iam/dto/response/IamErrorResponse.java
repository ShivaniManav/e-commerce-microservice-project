package com.sm.iam.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IamErrorResponse {

    private int status;

    private String message;

    private long timeStamp;

}
