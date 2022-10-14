package ru.msugrobov.mapper;

/**
 * Describes contract for mappers
 *
 * @param <T> entity
 * @param <DTO> dto
 */
public interface MapperInterface<T, DTO> {
    /**
     * Converts entity to dto
     *
     * @param entity entity to convert
     * @return dto from the entity
     */
    DTO dtoFromEntity(T entity);

    /**
     * Converts dto to entity
     *
     * @param dto dto to convert
     * @return entity from the dto
     */
    T entityFromDto(DTO dto);
}
