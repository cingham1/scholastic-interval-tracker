package com.scholastic.test.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *  Object to represent the JSON response returned from the server
 *
 * @author cingham
 */
@AllArgsConstructor
@Getter
@Setter
public class ServerResponseDto {
    int status;
    String message;
}
