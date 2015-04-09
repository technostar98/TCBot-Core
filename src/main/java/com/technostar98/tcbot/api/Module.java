package com.technostar98.tcbot.api;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
@Inherited
@Documented
/**
 * Used to indicate a module's main class that will be used to load up stuff from the module.
 * Once a class with this has been found, any methods annotated with {@link AssetLoader} will be called.
 *
 */
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
