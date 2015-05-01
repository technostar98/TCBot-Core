package com.technostar98.tcbot.command.Commands;

import com.technostar98.tcbot.api.command.Command;
import com.technostar98.tcbot.api.command.CommandType;
import com.technostar98.tcbot.api.command.TextCommand;
import com.technostar98.tcbot.api.lib.WrappedEvent;
import com.technostar98.tcbot.bot.BotManager;
import com.technostar98.tcbot.command.CommandManager;
import org.pircbotx.PircBotX;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class TextCommandControllerCommand extends Command {

    public TextCommandControllerCommand(String server){
        super("command", CommandType.USER_MESSAGE, server, UserLevel.OP, UserLevel.OWNER, UserLevel.VOICE);
    }

    @Override
    public String getMessage(WrappedEvent<MessageEvent<PircBotX>> event) {
        String[] message = event.getEvent().getMessage().split(" ");

        if(message.length >= 3){
            String action = message[1];
            String target = message[2];
            CommandManager cm = BotManager.getBotOutputPipe(this.getServer()).getCommandManager(event.getEvent().getChannel().getName());

            if(action.equals("add")){
                TextCommand testTC = cm.getTextCommand(target);

                if(testTC == null) {
                    List<UserLevel> userLevels = new LinkedList<>();
                    String commandMessage;

                    if (message.length >= 4) {
                        if (!message[3].startsWith("--ul=")) {
                            StringJoiner joiner = new StringJoiner(" ");

                            for (int i = 3; i < message.length; i++) {
                                joiner.add(message[i]);
                            }

                            commandMessage = joiner.toString();
                        } else {
                            if (message[3].length() > 5) {
                                String[] uls = message[3].substring(5, message[3].length()).split(",");
                                List<UserLevel> ulList = new LinkedList<>();

                                for (String s : uls) {
                                    UserLevel ul = UserLevel.valueOf(s);
                                    if (ul != null && !ulList.contains(ul))
                                        ulList.add(ul);
                                }

                                if (ulList.size() <= 0) {
                                    return "Specify userlevels, not just put the tag there.";
                                } else {
                                    userLevels = ulList;
                                }

                                if (message.length >= 5) {
                                    StringJoiner joiner = new StringJoiner(" ");

                                    for (int i = 4; i < message.length; i++) {
                                        joiner.add(message[i]);
                                    }

                                    commandMessage = joiner.toString();
                                } else {
                                    return "Specify a message to send.";
                                }
                            } else {
                                return "Specify userlevels, not just put the tag there.";
                            }
                        }
                    } else {
                        return "Specify userlevels and a message.";
                    }

                    TextCommand tc = new TextCommand(target, getServer(), commandMessage, (UserLevel[]) userLevels.toArray());
                    if (userLevels.isEmpty()) tc.setAcceptAllUsers(true);
                    cm.addTextCommand(tc);

                    return "Successfully created command " + tc.getName();
                }else{
                    return "Command " + target + " already exists.";
                }
            }else if(action.equals("remove")){
                TextCommand tc = cm.getTextCommand(target);
                if(tc != null){
                    return "Command " + target + " does not exist.";
                }else{
                    cm.removeTextCommand(target);

                    return "Successfully removed command "+ target;
                }
            }else if(action.equals("edit")){
                TextCommand tc = cm.getTextCommand(target);

                if(tc == null){
                    return "Command " + target + " does not exist.";
                }else{
                    if(message.length >= 4){
                        if(!message[3].startsWith("--ul=")){
                            StringJoiner joiner = new StringJoiner(" ");

                            for(int i = 3; i < message.length; i++){
                                joiner.add(message[i]);
                            }

                            tc.setMessage(joiner.toString());


                        }else{
                            if(message[3].length() > 5) {
                                String[] uls = message[3].substring(5, message[3].length()).split(",");
                                List<UserLevel> ulList = new LinkedList<>();

                                for(String s : uls){
                                    UserLevel ul = UserLevel.valueOf(s);
                                    if(ul != null && !ulList.contains(ul))
                                        ulList.add(ul);
                                }

                                if(ulList.size() <= 0){
                                    return "Specify userlevels, not just put the tag there.";
                                }else{
                                    tc.setRequiredUserLevels(ulList);
                                }

                                if (message.length >= 5) {
                                    StringJoiner joiner = new StringJoiner(" ");

                                    for (int i = 4; i < message.length; i++) {
                                        joiner.add(message[i]);
                                    }

                                    tc.setMessage(joiner.toString());
                                }
                            }else{
                                return "Specify userlevels, not just put the tag there.";
                            }
                        }
                    }else{
                        return "Specify userlevels or a message.";
                    }
                }
            }else{
                return "Successfully update command " + target;
            }
        }else{
            return null;
        }

        return null;
    }

    @Override
    public boolean runCommand(WrappedEvent<MessageEvent<PircBotX>> event) {
        return false;
    }

    @Override
    public String getHelpMessage() {
        return "!command {add|remove|edit} name(without the '!') <--ul=ul1,ul2 (must NOT have spaces in between userlevels; options are op, voice(non-twitch), and user, user assumed if argument is absent)> <commandText (API info WIP)>";
    }
}
