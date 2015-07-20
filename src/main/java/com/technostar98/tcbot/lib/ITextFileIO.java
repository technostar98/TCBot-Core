package com.technostar98.tcbot.lib;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot-Core.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public interface ITextFileIO<T> {

    public boolean openFile(String url);
    public boolean openFile(URL url);
    public Map<String, T> getMappedContents();
    public List<String> getFields();
    public void closeFile();
    public void setContents(Map<String, T> contents);
    public void addField(String key, T value);
    public void saveFileContents();
    public void readFileContents() throws IOException;
    public boolean isInitialized();
}
