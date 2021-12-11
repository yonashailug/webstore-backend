package edu.miu.webstorebackend.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotFoundDTO {
    private String name;
    private String message;
}