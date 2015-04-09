package com.technostar98.tcbot.api;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited
/**
 * Used to indicate methods that should be called to load up Commands, Filters, or anything else necessary.
 */
public @interface AssetLoader {
}
