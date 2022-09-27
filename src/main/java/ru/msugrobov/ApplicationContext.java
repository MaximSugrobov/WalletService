package ru.msugrobov;

import ru.msugrobov.exceptions.AppContextExpectedClassException;
import ru.msugrobov.exceptions.AppContextKeyException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton for application context
 */
public class ApplicationContext {

    private final Map<String, Object> appContextStorage;
    public static final ApplicationContext INSTANCE;
    private ApplicationContext(){
        this.appContextStorage = new ConcurrentHashMap<>();
    }

    static {
        INSTANCE = new ApplicationContext();
    }

    /**
     * Save context to singleton
     *
     * @param key key for the entity in map
     * @param value entity to save
     */
    public void saveInfo(String key, Object value) {

        appContextStorage.put(key, value);
    }

    /**
     * Get context of singleton
     *
     * @return entity
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> expectedClass) {
        if (!this.appContextStorage.containsKey(key)) {
            throw new AppContextKeyException(String.format("Key %s does not exist in storage", key));
        }
        Object value = this.appContextStorage.get(key);
        try {
            return (T) value;
        } catch (ClassCastException exception) {
            throw new AppContextExpectedClassException(String
                    .format("Provided class %s is not equal to expected class %s", value, expectedClass));
        }
    }
}
