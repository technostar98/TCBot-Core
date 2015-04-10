package com.technostar98.tcbot.api;


import java.lang.annotation.*;

/**
 * Similar to {@link Commands}, indicates a {@link java.util.Collection} which holds chat filters.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Filters {
}
