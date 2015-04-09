package com.technostar98.tcbot.api;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
/**
 * Used to indicate a {@link java.util.Collection} which holds loaded commands. Lists are preferable because Commands should have their names already.
 */
public @interface Commands {
}
