package ru.msugrobov;

import ru.msugrobov.entities.Player;

/**
 * Singleton for application context
 */
public class ApplicationContext {

    public static Player authorizedPlayer;
    private static ApplicationContext INSTANCE;
    private ApplicationContext() {
        authorizedPlayer = null;
    }

    /**
     * Init singleton
     *
     * @return singleton
     */
    public static ApplicationContext initInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ApplicationContext();
        }
        return INSTANCE;
    }

    /**
     * Save context to singleton
     *
     * @param playerToSaveContext entity of the authorized player
     */
    public void saveUserInfo(Player playerToSaveContext) {
        authorizedPlayer = playerToSaveContext;
    }

    /**
     * Get context of singleton
     *
     * @return entity of the authorized player
     */
    public static ApplicationContext getContext() {
        return INSTANCE;
    }
}
