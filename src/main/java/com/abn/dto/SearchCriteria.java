package com.abn.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Mostafa
 * @version 1.0
 * @since 2022-11-01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchCriteria {

    private String key;
    private String operation;
    private Object value;

}