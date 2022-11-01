package com.abn.mapper;

import java.util.List;

/**
 * @author Mostafa
 * @version 1.0
 * @since 2022-11-01
 */
public interface EntityMapper<D, E> {
    E toEntity(D dto);

    D toDto(E entity);

    List<E> toEntity(List<D> dtoList);

    List<D> toDto(List<E> entityList);

}