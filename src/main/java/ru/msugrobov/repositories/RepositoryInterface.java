package ru.msugrobov.repositories;

public interface RepositoryInterface<T> {
    T readById(int id);
    void create(T entity);
    void update(int id, T entity);
    void delete(int id);
}
