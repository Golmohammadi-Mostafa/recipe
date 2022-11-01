package com.abn.dto;

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
public class ResponseMsgDTO implements Serializable {
    private String message;
}