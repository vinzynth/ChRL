package at.chrl.nutils.configuration.listener;

/**
 * Interface for Config Event listener
 *
 * Created by vinzynth on 21.12.15.
 */
public interface ConfigEventListener {

    /**
     * Load event befor load configuration Files.
     * <p>
     *     Triggered before Config Field parsing
     * </p>
     *
     * @param targetClass - loaded class
     */
    default void beforeOnLoadedConfigClass(final Class<?> targetClass){
    }

    /**
     * Load event befor load of configuration Files.
     * <p>
     *     Triggered before Config Field parsing
     * </p>
     *
     * @param obj - loaded object with annotated member fields
     */
    default void beforeOnLoadedConfigObject(final Object obj){
    }

    /**
     * Load event for configuration Files.
     * <p>
     *     Triggered after Config Field parsing
     * </p>
     *
     * @param targetClass - loaded class
     */
    default void onLoadedConfigClass(final Class<?> targetClass){
    }

    /**
     * Load event for configuration Files.
     * <p>
     *     Triggered after Config Field parsing
     * </p>
     *
     * @param obj - loaded object with annotated member fields
     */
    default void onLoadedConfigObject(final Object obj){
    }

    /**
     * Gets triggered after export was executed
     *
     * @param targetClass - loaded class
     */
    default void onExportedConfigClass(final Class<?> targetClass){
    }

    /**
     * Gets triggered after export was executed
     *
     * @param obj - loaded object
     */
    default void onExportedConfigObject(final Object obj){
    }

}
