package com.abn.exception;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Mostafa
 * @version 1.0
 * @since 2022-11-01
 */
@Data
@Builder
public class ErrorResponse implements Serializable {
    private String errorMessage;
    private int errorCode;
}
