package com.technostar98.tcbot.api;

import java.lang.annotation.*;

/**
 * <p>Used to indicate a {@link java.util.Collection} which holds loaded commands. Lists are preferable because Commands should have their names already.</p>
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
public @interface Commands {
}
