package com.technostar98.tcbot.lib;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public interface ITextFileIO<T> {

    public boolean openFile(String url);
    public boolean openFile(URL url);
    public HashMap<String, T> getMappedContents();
    public List<String> getFields();
    public void closeFile();
    public void setContents(HashMap<String, T> contents);
    public void addField(String key, T value);
    public void saveFileContents();
    public void readFileContents() throws IOException;
}
