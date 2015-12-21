package at.chrl.nutils.configuration.listener;

/**
 * Interface for Config Event listener
 *
 * Created by vinzynth on 21.12.15.
 */
public interface ConfigEventListener {

    /**
     * Load event for configuration Files.
     * <p>
     *     Triggered after Config Field parsing
     * </p>
     *
     * @param targetClass - loaded class
     */
    void onLoadedConfigClass(final Class<?> targetClass);

    /**
     * Load event for configuration Files.
     * <p>
     *     Triggered after Config Field parsing
     * </p>
     *
     * @param obj - loaded object with annotated member fields
     */
    void onLoadedConfigObject(final Object obj);

}
