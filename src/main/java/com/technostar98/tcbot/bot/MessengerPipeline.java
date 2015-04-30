package com.technostar98.tcbot.bot;

import com.technostar98.tcbot.api.command.CommandType;
import com.technostar98.tcbot.api.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.HashMap;

public class MessengerPipeline {
    private final String server;
    private int messagesPerSec, lastMessageID = 1, maxMessages = 5, minMessages = 1;
    private long milisBetween = 1000L;
    private HashMap<Integer, Long> messageTimes = new HashMap<>();
    private boolean outputEnabled = true;

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

    public void sendMessage(String message, CommandType type, WrappedEvent<MessageEvent<PircBotX>> wrappedEvent){
        if(isOutputEnabled() && message != null && wrappedEvent.getEvent().getChannel().getName()
                != null && (shouldMessageSend() || type == CommandType.LEAVE)) {
            switch (type) {
                case ACTION:
                    wrappedEvent.getEvent().getBot().sendIRC().action(wrappedEvent.getEvent().getChannel().getName(), message);
                    break;
                case USER_MESSAGE:
                    wrappedEvent.getEvent().getBot().sendIRC().message(wrappedEvent.getEvent().getChannel().getName(), message);
                    break;
                case LEAVE:
                    wrappedEvent.getEvent().getChannel().send().part(message);
                    break;
                case KICK:
                    wrappedEvent.getEvent().getChannel().send().kick(wrappedEvent.getEvent().getUser(), message);
                    break;
                case JOIN:
                    wrappedEvent.getEvent().getBot().sendIRC().joinChannel(message);
                    break;
                case KICKED: //TODO Kicked command output?
                    break;
                case GENERIC: //TODO Generic command output?
                    break;
                default:
                    System.out.println("Unknown type");
            }
        }
    }

    private boolean shouldMessageSend(){
        if(lastMessageID > maxMessages) lastMessageID = minMessages;
        if(messageTimes.containsKey(lastMessageID)){ //TODO Buffer method for output
            if(System.currentTimeMillis() - messageTimes.get(lastMessageID) >= milisBetween){
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

    public void setOutputEnabled(boolean outputEnabled) {
        this.outputEnabled = outputEnabled;
    }

    public boolean isOutputEnabled() {
        return outputEnabled;
    }
}
