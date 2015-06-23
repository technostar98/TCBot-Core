package com.technostar98.tcbot.lib.config;

import java.util.Arrays;
import java.util.List;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot-Core.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class ServerConfiguration {
    private String serverName;
    private String serverAddress;
    private String nick;
    private String password;
    private List<String> autoJoinChannels;
    private List<String> superusers;

    public ServerConfiguration(String serverName, String address, String nick, String password, List<String> superusers, String... channels){
        this(serverName, address, nick, password, superusers, Arrays.asList(channels));
    }

    public ServerConfiguration(String serverName, String address, String nick, String password, List<String> superusers, List<String> channels){
        this.serverName = serverName;
        this.serverAddress = address;
        this.nick = nick;
        this.password = password;
        this.autoJoinChannels = channels;
        this.superusers = superusers;
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

    public void removeChannel(String channel){
        if(autoJoinChannels.contains(channel)){
            autoJoinChannels.remove(channel);
        }
    }

    public List<String> getSuperusers() {
        return superusers;
    }

    public void setSuperusers(List<String> superusers) {
        this.superusers = superusers;
    }

    public void addSuperuser(String nick){
        if(!superusers.contains(nick))
            superusers.add(nick);
    }

    public void removeSuperuser(String nick){
        if(superusers.contains(nick))
            superusers.remove(nick);
    }
}
