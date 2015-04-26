package com.technostar98.tcbot.command.Commands;

import com.technostar98.tcbot.api.command.Command;
import com.technostar98.tcbot.api.command.CommandType;
import com.technostar98.tcbot.api.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.HashMap;

public class SlapCommand extends Command{
    private int lastMessageID = 1, minMessages = 1, maxMessages = 3;
    private long millisBetween = 5000;
    private HashMap<Integer, Long> messageTimes = new HashMap<>();

    public SlapCommand(String server){
        super("slap", CommandType.ACTION, server);
    }

    @Override
    public String getMessage(WrappedEvent<MessageEvent<PircBotX>> event) {
        boolean shouldRun;

        if(lastMessageID > maxMessages) lastMessageID = minMessages;
        if(messageTimes.containsKey(lastMessageID)){ //TODO Buffer method for output
            if(System.currentTimeMillis() - messageTimes.get(lastMessageID) >= millisBetween){
                messageTimes.put(lastMessageID, System.currentTimeMillis());
                lastMessageID++;
                shouldRun = true;
            }else{
                shouldRun = false;
            }
        }else{
            messageTimes.put(lastMessageID, System.currentTimeMillis());
            lastMessageID++;
            shouldRun = true;
        }

        if(shouldRun) {
            String message = event.getEvent().getMessage();
            String target;
            if (message.indexOf(" ") < 0) target = event.getEvent().getUser().getNick();
            else {
                int spaceIndex = message.indexOf(" ");
                if (spaceIndex == message.length() - 1) target = event.getEvent().getUser().getNick();
                else {
                    String partedMessage = message.substring(++spaceIndex);
                    spaceIndex = partedMessage.indexOf(" ");
                    if (spaceIndex == 0) target = event.getEvent().getUser().getNick();
                    else if (spaceIndex < 0) target = partedMessage;
                    else target = partedMessage.substring(0, spaceIndex);
                }
            }

            if(target.equals(event.getEvent().getBot().getNick())) target = event.getEvent().getUser().getNick();

            return "slaps " + target + " around a bit with a large trout";
        }else
            return null;
    }

    @Override
    public boolean runCommand(WrappedEvent<MessageEvent<PircBotX>> event) {
        return true;
    }

    @Override
    public boolean isUserAllowed(MessageEvent<PircBotX> event) {
        return true;
    }

    @Override
    public String getHelpMessage() {
        return "!slap target";
    }
}
