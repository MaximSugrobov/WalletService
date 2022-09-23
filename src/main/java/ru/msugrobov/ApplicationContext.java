package ru.msugrobov;

import java.util.HashMap;

/**
 * Singleton for application context
 */
public class ApplicationContext<T> {

    public static HashMap<String, Object> appContextStorage = new HashMap<>();
    public static ApplicationContext<Object> INSTANCE = new ApplicationContext<>();
    private ApplicationContext(){}

    /**
     * Save context to singleton
     *
     * @param keyName key for the entity in map
     * @param entity entity to save
     */
    public void saveInfo(String keyName, T entity) {

        appContextStorage.put(keyName, entity);
    }

    /**
     * Get context of singleton
     *
     * @return entity of the authorized player
     */
    public static ApplicationContext<Object> getContext() {
        return INSTANCE;
    }
}
