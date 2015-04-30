package com.technostar98.tcbot.api;


import java.lang.annotation.*;

/**
 * <p>Similar to {@link Commands}, indicates a {@link java.util.Collection} which holds chat filters.</p>
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
@Target(ElementType.FIELD)
@Documented
public @interface Filters {
}
