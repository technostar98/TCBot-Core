package com.technostar98.tcbot.lib.config;

import java.util.Arrays;
import java.util.List;

public class ServerConfiguration {
    private String serverName;
    private String serverAddress;
    private String nick;
    private String password;
    private List<String> autoJoinChannels;

    public ServerConfiguration(String serverName, String address, String nick, String password, String... channels){
        this(serverName, address, nick, password, Arrays.asList(channels));
    }

    public ServerConfiguration(String serverName, String address, String nick, String password, List<String> channels){
        this.serverName = serverName;
        this.serverAddress = address;
        this.nick = nick;
        this.password = password;
        this.autoJoinChannels = channels;
    }

    public void setAutoJoinChannels(List<String> autoJoinChannels) {
        this.autoJoinChannels = autoJoinChannels;
    }

    public void setAutoJoinChannels(String[] channels){
        this.autoJoinChannels = Arrays.asList(channels);
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void addChannel(String channel){
        if(!autoJoinChannels.contains(channel)) autoJoinChannels.add(channel);
    }

    public List<String> getAutoJoinChannels() {
        return autoJoinChannels;
    }

    public String getNick() {
        return nick;
    }

    public String getPassword() {
        return password;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public String getServerName() {
        return serverName;
    }
}
