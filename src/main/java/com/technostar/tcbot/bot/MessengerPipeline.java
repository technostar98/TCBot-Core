package com.technostar.tcbot.bot;

import org.pircbotx.PircBotX;

import java.util.HashMap;

/**
 * Created by bret on 4/3/15.
 */
public class MessengerPipeline {
    private final String server;
    private int messagesPerSec, lastMessageID = 1;
    private HashMap<Integer, Long> messageTimes = new HashMap<>();

    public MessengerPipeline(String server, int limitPerSecond){
        this.server = server;
        this.messagesPerSec = limitPerSecond;
    }

    public String getServer() {
        return server;
    }

    public int getMessagesPerSec() {
        return messagesPerSec;
    }

    public void setMessagesPerSec(int messagesPerSec) {
        this.messagesPerSec = messagesPerSec;
    }

    public void sendMessage(String channel, String message, PircBotX bot){
        if(message != null && shouldMessageSend()) bot.sendIRC().message(channel, message);
    }

    private boolean shouldMessageSend(){
        if(messageTimes.containsKey(lastMessageID)){ //TODO Buffer method for output
            if(System.currentTimeMillis() - messageTimes.get(lastMessageID) >= 1000){
                messageTimes.put(lastMessageID, System.currentTimeMillis());
                lastMessageID++;
                return true;
            }else{
                return false;
            }
        }else{
            messageTimes.put(lastMessageID, System.currentTimeMillis());
            lastMessageID++;
            return true;
        }
    }
}
