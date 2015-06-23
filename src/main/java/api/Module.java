package api;

import java.lang.annotation.*;

/**
 * <p>Used to indicate a module's main class that will be used to load up stuff from the module.
 * Once a class with this has been found, any methods annotated with {@link AssetLoader} will be called.</p>
 *
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot-Core.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
@Inherited
@Documented
public @interface Module {
    /**
     * The name of a module
     * @return
     */
    String name();

    /**
     * The version of a module
     * @return an integer version
     */
    int version();

    /**
     * ID of a module, which should be static
     * @return
     */
    String id();
}
