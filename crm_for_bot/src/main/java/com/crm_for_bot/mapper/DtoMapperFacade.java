package com.crm_for_bot.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.record.RecordModule;

/**
 * A generic mapper facade class for converting between entities and DTOs.
 * Provides a base implementation for mapping using {@link ModelMapper} and supports custom mapping logic.
 *
 * @param <E> the type of the entity
 * @param <D> the type of the DTO
 */
public class DtoMapperFacade<E, D> {
    private final Class<E> entityClass;
    private final Class<D> dtoClass;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * Constructs a new {@code DtoMapperFacade}.
     *
     * @param eClass the class of the entity
     * @param dClass the class of the DTO
     */
    public DtoMapperFacade(final Class<E> eClass, final Class<D> dClass) {
        entityClass = eClass;
        dtoClass = dClass;
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.registerModule(new RecordModule());
    }

    /**
     * Converts a DTO to an entity.
     * Applies custom mapping logic defined in {@link #decorateEntity}.
     *
     * @param dto the DTO to be converted
     * @return the corresponding entity
     */
    public E convertToEntity(final D dto) {
        final E entity = modelMapper.map(dto, entityClass);
        decorateEntity(entity, dto);
        return entity;
    }

    /**
     * Converts an entity to a DTO.
     * Applies custom mapping logic defined in {@link #decorateDto}.
     *
     * @param entity the entity to be converted
     * @return the corresponding DTO
     */
    public D convertToDto(final E entity) {
        final D dto = modelMapper.map(entity, dtoClass);
        decorateDto(dto, entity);
        return dto;
    }

    /**
     * Customizes the entity-to-DTO mapping.
     * Can be overridden by subclasses to provide additional mapping logic.
     *
     * @param entity the entity being mapped
     * @param dto the DTO to be decorated
     */
    protected void decorateEntity(final E entity, final D dto) {
        // Default implementation does nothing
    }

    /**
     * Customizes the DTO-to-entity mapping.
     * Can be overridden by subclasses to provide additional mapping logic.
     *
     * @param dto the DTO being mapped
     * @param entity the entity to be decorated
     */
    protected void decorateDto(final D dto, final E entity) {
        // Default implementation does nothing
    }
}
