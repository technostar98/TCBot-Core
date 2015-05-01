package com.technostar98.tcbot.bot;

import com.technostar98.tcbot.api.command.Command;
import com.technostar98.tcbot.api.filter.ChatFilter;
import com.technostar98.tcbot.api.filter.FilterResponse;
import com.technostar98.tcbot.command.CommandManager;
import com.technostar98.tcbot.api.lib.WrappedEvent;
import com.technostar98.tcbot.lib.Logger;
import com.technostar98.tcbot.modules.CommandPool;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.*;
import org.pircbotx.hooks.types.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * <p>Will hold all of the listeners for a bot instance</p>
 *
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot-Core.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class ListenerPipeline extends ListenerAdapter<PircBotX>{
    public HashMap<String, CommandManager> commandManagers = new HashMap<>(); //Command managers for channels
    public MessengerPipeline messengerPipeline;
    public String server;
    private boolean inputEnabled = true;

    //TODO logging incoming messages/all things

    public ListenerPipeline(String server, String... channels){
        this.server = server;
        for(String s : channels) commandManagers.put(s, new CommandManager(server, s)); //TODO load command managers from database
        messengerPipeline  = new MessengerPipeline(server, 5);
    }

    public ListenerPipeline(String server, List<String> channels){
        this.server = server;
        channels.stream().forEach(s -> commandManagers.put(s, new CommandManager(server, s)));
        messengerPipeline = new MessengerPipeline(server, 5);
    }

    public CommandManager getCommandManager(String channel){
        return commandManagers.getOrDefault(channel, null);
    }

    public void removeCommandManager(String channel){
        commandManagers.remove(channel);
    }

    public void closeListener(){
        inputEnabled = false;

        try {
            for (String s : commandManagers.keySet()) {
                CommandManager cm = commandManagers.get(s);
                if(cm.getFilters() != null)
                    cm.getFilters().forEach(f -> f.close());
                if(cm.getCommands() != null)
                    cm.getCommands().forEach(c -> c.close());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onAction(ActionEvent<PircBotX> event) throws Exception {
        if(!event.getUser().getNick().equals(event.getBot().getNick()) && inputEnabled)
            getCommandManager(event.getChannel().getName()).getFilters().forEach(f -> f.onUserAction(new WrappedEvent<>(event)));
        super.onAction(event);
    }

    @Override
    public void onChannelInfo(ChannelInfoEvent<PircBotX> event) throws Exception {
        super.onChannelInfo(event);
    }

    @Override
    public void onConnect(ConnectEvent<PircBotX> event) throws Exception {
//        commandManagers.keySet().stream().forEach(cm -> getCommandManager(cm).getFilters().forEach(f -> f.onServerConnect(new WrappedEvent<>(event))));
        super.onConnect(event);
    }

    @Override
    public void onDisconnect(DisconnectEvent<PircBotX> event) throws Exception {
        commandManagers.keySet().stream().forEach(cm ->{
            if(getCommandManager(cm) != null){
                if(getCommandManager(cm).getFilters() != null){
                    getCommandManager(cm).getFilters().forEach(f -> f.onServerDisconnect(new WrappedEvent<>(event)));
                }
            }else{
                System.out.println("Command manager for " + cm + " is null");
            }
        });
//        commandManagers.entrySet().stream().forEach(e -> removeCommandManager(e.getKey()));
        super.onDisconnect(event);
    }

    @Override
    public void onFinger(FingerEvent<PircBotX> event) throws Exception {
        super.onFinger(event);
    }

    @Override
    public void onHalfOp(HalfOpEvent<PircBotX> event) throws Exception {
        super.onHalfOp(event);
    }

    @Override
    public void onIncomingChatRequest(IncomingChatRequestEvent<PircBotX> event) throws Exception {
        super.onIncomingChatRequest(event);
    }

    @Override
    public void onIncomingFileTransfer(IncomingFileTransferEvent<PircBotX> event) throws Exception {
        super.onIncomingFileTransfer(event);
    }

    @Override
    public void onInvite(InviteEvent<PircBotX> event) throws Exception {
        event.getBot().sendIRC().joinChannel(event.getChannel());
        super.onInvite(event);
    }

    @Override
    public void onJoin(JoinEvent<PircBotX> event) throws Exception {
        if(event.getUser().getNick().contains(event.getBot().getNick())) {
            Logger.info("Joined channel " + event.getChannel().getName());
            CommandManager cm = new CommandManager(server, event.getChannel().getName());

            //TODO module command/filter loading
            CommandPool.getBotCommandsList().forEach(c -> cm.addCommand(c));
            if(cm.getCommands() != null)
                cm.getCommands().stream().forEach(c -> c.setServer(server));
            CommandPool.getBotFilterList().forEach(f -> cm.addFilter(f));
            if(cm.getFilters() != null)
                cm.getFilters().stream().forEach(f -> f.setServer(server));

            commandManagers.put(event.getChannel().getName(), cm);
        }else if(inputEnabled){
            CommandManager cm = commandManagers.get(event.getChannel().getName());
            if(cm != null && cm.getFilters() != null)
                cm.getFilters().stream().forEach(f -> f.onUserJoin(new WrappedEvent<>(event)));
        }

        super.onJoin(event);
    }

    @Override
    public void onKick(KickEvent<PircBotX> event) throws Exception {
        if(event.getRecipient().getNick().equals(event.getBot().getNick())) {
            commandManagers.keySet().stream().forEach(cm ->{
                if(getCommandManager(cm) != null){
                    if(getCommandManager(cm).getFilters() != null){
                        getCommandManager(cm).getFilters().forEach(f -> f.onKicked(new WrappedEvent<>(event)));
                    }
                }else{
                    System.out.println("Command manager for " + cm + " is null");
                }
            });
            commandManagers.remove(event.getChannel().getName());
        }else{
            CommandManager cm = getCommandManager(event.getChannel().getName());
            if(cm.getFilters() != null)
                cm.getFilters().forEach(f -> f.onUserKick(new WrappedEvent<>(event)));
        }
        super.onKick(event);
    }

    @Override
    public void onMessage(MessageEvent<PircBotX> event) throws Exception {
        if(inputEnabled) {
            CommandManager cm = getCommandManager(event.getChannel().getName());

            try {
                messaged:
                if (cm != null) {
                    if (cm.getFilters() != null) {
                        if (event.getMessage().contains(event.getBot().getNick())) {
                            FilterResponse filterResponse;
                            List<ChatFilter> filters = cm.getFilters();
                            Collections.sort(filters);
                            WrappedEvent<MessageEvent<PircBotX>> wrapped = new WrappedEvent<>(event);

                            int index = 0;
                            while (index < filters.size()) {
                                filterResponse = filters.get(0).onNickPinged(wrapped);
                                if (filterResponse == FilterResponse.BREAK_FILTERS) break;
                                else if (filterResponse == FilterResponse.BREAK) break messaged;
                                index++;
                            }
                        } else {
                            FilterResponse filterResponse;
                            List<ChatFilter> filters = cm.getFilters();
                            Collections.sort(filters);
                            WrappedEvent<MessageEvent<PircBotX>> wrapped = new WrappedEvent<>(event);

                            int index = 0;
                            while (index < filters.size()) {
                                filterResponse = filters.get(0).onUserMessage(wrapped);
                                if (filterResponse == FilterResponse.BREAK_FILTERS) break;
                                else if (filterResponse == FilterResponse.BREAK) break messaged;
                                index++;
                            }
                        }
                    }
                    if (event.getMessage().startsWith("!")) {
                        int endCommandIndex = event.getMessage().contains(" ") ? event.getMessage().indexOf(" ") : event.getMessage().length();
                        String commandName = event.getMessage().substring(1, endCommandIndex);
                        Command c = cm.getCommand(commandName);
                        WrappedEvent<MessageEvent<PircBotX>> wrappedEvent = new WrappedEvent<>(event);

                        if (c != null && c.isUserAllowed(event)) {
//                            System.out.println("MessengerPipe is null: " + (this.messengerPipeline == null));
//                            System.out.println("c.message is null: " + (c.getMessage(wrappedEvent) == null));
//                            System.out.println("Wrapped message is null: " + (wrappedEvent == null));
                            this.messengerPipeline.sendMessage(c.getMessage(wrappedEvent), c.commandType, wrappedEvent);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        super.onMessage(event);
    }

    @Override
    public void onMode(ModeEvent<PircBotX> event) throws Exception {
        super.onMode(event);
    }

    @Override
    public void onMotd(MotdEvent<PircBotX> event) throws Exception {
        super.onMotd(event);
    }

    @Override
    public void onNickAlreadyInUse(NickAlreadyInUseEvent<PircBotX> event) throws Exception {
        IRCBot b = BotManager.getBot(server);
        event.getBot().sendIRC().message("nickserv", "ghost " + b.getPreferredNick());
        event.getBot().sendIRC().changeNick(b.getPreferredNick());
        super.onNickAlreadyInUse(event);
    }

    @Override
    public void onNickChange(NickChangeEvent<PircBotX> event) throws Exception {
        BotManager.getBot(server).setPreferredNick(event.getNewNick());
        super.onNickChange(event);
    }

    @Override
    public void onNotice(NoticeEvent<PircBotX> event) throws Exception {
        super.onNotice(event);
    }

    @Override
    public void onOp(OpEvent<PircBotX> event) throws Exception {
        super.onOp(event);
    }

    @Override
    public void onOwner(OwnerEvent<PircBotX> event) throws Exception {
        super.onOwner(event);
    }

    @Override
    public void onPart(PartEvent<PircBotX> event) throws Exception {
        super.onPart(event);
    }

    @Override
    public void onPing(PingEvent<PircBotX> event) throws Exception {
        super.onPing(event);
    }

    @Override
    public void onPrivateMessage(PrivateMessageEvent<PircBotX> event) throws Exception {
        super.onPrivateMessage(event);
    }

    @Override
    public void onQuit(QuitEvent<PircBotX> event) throws Exception {
        if(event.getUser().getNick().equals(event.getBot().getNick())){
            commandManagers.keySet().parallelStream().forEach(cm -> {
                if(getCommandManager(cm).getFilters() != null)
                    getCommandManager(cm).getFilters().forEach(f -> f.onQuit(new WrappedEvent<>(event)));
            });
        }
        super.onQuit(event);
    }

    @Override
    public void onRemoveChannelBan(RemoveChannelBanEvent<PircBotX> event) throws Exception {
        super.onRemoveChannelBan(event);
    }

    @Override
    public void onRemoveChannelKey(RemoveChannelKeyEvent<PircBotX> event) throws Exception {
        super.onRemoveChannelKey(event);
    }

    @Override
    public void onRemoveChannelLimit(RemoveChannelLimitEvent<PircBotX> event) throws Exception {
        super.onRemoveChannelLimit(event);
    }

    @Override
    public void onRemoveInviteOnly(RemoveInviteOnlyEvent<PircBotX> event) throws Exception {
        super.onRemoveInviteOnly(event);
    }

    @Override
    public void onRemoveModerated(RemoveModeratedEvent<PircBotX> event) throws Exception {
        super.onRemoveModerated(event);
    }

    @Override
    public void onRemoveNoExternalMessages(RemoveNoExternalMessagesEvent<PircBotX> event) throws Exception {
        super.onRemoveNoExternalMessages(event);
    }

    @Override
    public void onRemovePrivate(RemovePrivateEvent<PircBotX> event) throws Exception {
        super.onRemovePrivate(event);
    }

    @Override
    public void onRemoveSecret(RemoveSecretEvent<PircBotX> event) throws Exception {
        super.onRemoveSecret(event);
    }

    @Override
    public void onRemoveTopicProtection(RemoveTopicProtectionEvent<PircBotX> event) throws Exception {
        super.onRemoveTopicProtection(event);
    }

    @Override
    public void onServerPing(ServerPingEvent<PircBotX> event) throws Exception {
        super.onServerPing(event);
    }

    @Override
    public void onServerResponse(ServerResponseEvent<PircBotX> event) throws Exception {
        super.onServerResponse(event);
    }

    @Override
    public void onSetChannelBan(SetChannelBanEvent<PircBotX> event) throws Exception {
        super.onSetChannelBan(event);
    }

    @Override
    public void onSetChannelKey(SetChannelKeyEvent<PircBotX> event) throws Exception {
        super.onSetChannelKey(event);
    }

    @Override
    public void onSetChannelLimit(SetChannelLimitEvent<PircBotX> event) throws Exception {
        super.onSetChannelLimit(event);
    }

    @Override
    public void onSetInviteOnly(SetInviteOnlyEvent<PircBotX> event) throws Exception {
        super.onSetInviteOnly(event);
    }

    @Override
    public void onSetModerated(SetModeratedEvent<PircBotX> event) throws Exception {
        super.onSetModerated(event);
    }

    @Override
    public void onSetNoExternalMessages(SetNoExternalMessagesEvent<PircBotX> event) throws Exception {
        super.onSetNoExternalMessages(event);
    }

    @Override
    public void onSetPrivate(SetPrivateEvent<PircBotX> event) throws Exception {
        super.onSetPrivate(event);
    }

    @Override
    public void onSetSecret(SetSecretEvent<PircBotX> event) throws Exception {
        super.onSetSecret(event);
    }

    @Override
    public void onSetTopicProtection(SetTopicProtectionEvent<PircBotX> event) throws Exception {
        super.onSetTopicProtection(event);
    }

    @Override
    public void onSocketConnect(SocketConnectEvent<PircBotX> event) throws Exception {
//        commandManagers.entrySet().stream().forEach(e -> e.getValue().getFilters().forEach(f -> f.onSocketConnect(new WrappedEvent<>(event))));
        super.onSocketConnect(event);
    }

    @Override
    public void onSuperOp(SuperOpEvent<PircBotX> event) throws Exception {
        super.onSuperOp(event);
    }

    @Override
    public void onTime(TimeEvent<PircBotX> event) throws Exception {
        super.onTime(event);
    }

    @Override
    public void onTopic(TopicEvent<PircBotX> event) throws Exception {
        super.onTopic(event);
    }

    @Override
    public void onUnknown(UnknownEvent<PircBotX> event) throws Exception {
        super.onUnknown(event);
    }

    @Override
    public void onUserList(UserListEvent<PircBotX> event) throws Exception {
        super.onUserList(event);
    }

    @Override
    public void onUserMode(UserModeEvent<PircBotX> event) throws Exception {
        super.onUserMode(event);
    }

    @Override
    public void onVersion(VersionEvent<PircBotX> event) throws Exception {
        super.onVersion(event);
    }

    @Override
    public void onVoice(VoiceEvent<PircBotX> event) throws Exception {
        super.onVoice(event);
    }

    @Override
    public void onWhois(WhoisEvent<PircBotX> event) throws Exception {
        super.onWhois(event);
    }

    @Override
    public void onGenericCTCP(GenericCTCPEvent<PircBotX> event) throws Exception {
        super.onGenericCTCP(event);
    }

    @Override
    public void onGenericUserMode(GenericUserModeEvent<PircBotX> event) throws Exception {
        super.onGenericUserMode(event);
    }

    @Override
    public void onGenericChannelMode(GenericChannelModeEvent<PircBotX> event) throws Exception {
        super.onGenericChannelMode(event);
    }

    @Override
    public void onGenericDCC(GenericDCCEvent<PircBotX> event) throws Exception {
        super.onGenericDCC(event);
    }

    @Override
    public void onGenericMessage(GenericMessageEvent<PircBotX> event) throws Exception {
        super.onGenericMessage(event);
    }

    @Override
    public void onGenericChannel(GenericChannelEvent<PircBotX> event) throws Exception {
        super.onGenericChannel(event);
    }

    @Override
    public void onGenericUser(GenericUserEvent<PircBotX> event) throws Exception {
        super.onGenericUser(event);
    }

    @Override
    public void onGenericChannelUser(GenericChannelUserEvent<PircBotX> event) throws Exception {
        super.onGenericChannelUser(event);
    }
}
