package com.technostar98.tcbot.lib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * All terms and conditions of using said code
 * are determined by the GNU LGPL 3.0.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public interface IJsonFileIO<U> extends ITextFileIO<U> {

    public default Gson buildGson(){
        return new GsonBuilder()
                .disableHtmlEscaping()
                .serializeNulls()
                .setPrettyPrinting()
                .create();
    }
}
