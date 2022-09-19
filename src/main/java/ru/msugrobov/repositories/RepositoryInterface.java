package ru.msugrobov.repositories;

import java.util.List;

/**
 * Describes contract for interaction with repositories
 *
 * @param <T> entity for each repository
 */
public interface RepositoryInterface<T> {
    /**
     * Creates new entity
     *
     * @param entity creates entity if not already exists
     */
    void create(T entity);

    /**
     * Read context of an entity
     *
     * @param id identifier
     * @return context of the entity
     */
    T readById(Integer id);

    /**
     * Update context of an entity
     *
     * @param id identifier of an entity to be updated
     * @param entity updated context of the entity
     */
    void update(int id, T entity);

    /**
     * Delete entity by its identifier
     *
     * @param id identifier
     */
    void delete(int id);

    /**
     * Read all entities in storage
     *
     * @return all entities in storage
     */
    List<T> readAll();
}
