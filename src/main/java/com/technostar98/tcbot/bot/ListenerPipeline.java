package com.technostar98.tcbot.bot;

import api.command.Command;
import api.command.ICommandFilterRegistry;
import api.command.TextCommand;
import api.filter.ChatFilter;
import api.filter.event.*;
import api.lib.WrappedEvent;
import com.google.common.base.Optional;
import com.technostar98.tcbot.command.Cancelable;
import com.technostar98.tcbot.command.ChannelManager;
import com.technostar98.tcbot.command.TextCommandParser;
import com.technostar98.tcbot.lib.Logger;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.*;
import org.pircbotx.hooks.types.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
    public HashMap<String, ChannelManager> channelManagers = new HashMap<>(); //Command managers for channels
    public MessengerPipeline messengerPipeline;
    public String server;
    private boolean inputEnabled = true;

    //TODO logging incoming messages/all things

    public ListenerPipeline(String server, String... channels){
        this.server = server;
        for(String s : channels) channelManagers.put(s, new ChannelManager(server, s)); //TODO load command managers from database
        messengerPipeline  = new MessengerPipeline(server, 5);
    }

    public ListenerPipeline(String server, List<String> channels){
        this.server = server;
        channels.stream().forEach(s -> channelManagers.put(s, new ChannelManager(server, s)));
        messengerPipeline = new MessengerPipeline(server, 5);
    }

    public ChannelManager getChannelManager(String channel){
        return channelManagers.getOrDefault(channel, null);
    }

    public List<ChannelManager> getChannelManagers(){
        return channelManagers.keySet().stream().map(s -> getChannelManager(s)).collect(Collectors.toList());
    }

    public void removeCommandManager(String channel){
        channelManagers.remove(channel);
    }

    public void closeListener(){
        inputEnabled = false;

        try {
            for (String s : channelManagers.keySet()) {
                ChannelManager cm = channelManagers.get(s);
                cm.saveChannelData();
                if(cm.getFilters() != null)
                    cm.getFilters().keySet().forEach(f -> cm.getFilter(f).get().close());
                if(cm.getCommands() != null)
                    cm.getCommands().keySet().forEach(c -> cm.getCommand(c).get().close());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onAction(ActionEvent<PircBotX> event) throws Exception {
        if(!event.getUser().getNick().equals(event.getBot().getNick()) && inputEnabled) {
            ChannelManager cm = getChannelManager(event.getChannel().getName());
            if(event.getMessage().contains(event.getBot().getNick())){
                @Cancelable ChannelPingEvent pEvent =new ChannelPingEvent(new EventContext(event.getBot(), server, event.getChannel(), event.getUser(), event));
                cm.eventBus.post(pEvent);
            }else{
                @Cancelable ChannelActionEvent cEvent = new ChannelActionEvent(new EventContext(event.getBot(), server, event.getChannel(), event.getUser(), event));
                cm.eventBus.post(cEvent);
            }
        }
        super.onAction(event);
    }

    @Override
    public void onChannelInfo(ChannelInfoEvent<PircBotX> event) throws Exception {
        super.onChannelInfo(event);
    }

    @Override
    public void onConnect(ConnectEvent<PircBotX> event) throws Exception {
//        channelManagers.keySet().stream().forEach(cm -> getCommandManager(cm).getFilters().forEach(f -> f.onServerConnect(new WrappedEvent<>(event))));
        super.onConnect(event);
    }

    @Override
    public void onDisconnect(DisconnectEvent<PircBotX> event) throws Exception {
        for(String c : channelManagers.keySet()){
            ChannelManager cm = getChannelManager(c);
            @Cancelable BotDisconnectEvent dEvent = new BotDisconnectEvent(new EventContext(event.getBot(), server, null, null, event));
            cm.eventBus.post(dEvent);
        }
//        channelManagers.entrySet().stream().forEach(e -> removeCommandManager(e.getKey()));
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
        if(event.getUser().getNick().equals(event.getBot().getNick())) {
            Logger.info("Joined channel " + event.getChannel().getName());
            ChannelManager cm = new ChannelManager(server, event.getChannel().getName());
            ICommandFilterRegistry manager = api.command.CommandManager.commandManager.get();

            channelManagers.put(event.getChannel().getName(), cm);
        }else if(inputEnabled){
            ChannelManager cm = channelManagers.get(event.getChannel().getName());
            @Cancelable UserJoinEvent jEvent = new UserJoinEvent(new EventContext(event.getBot(), server, event.getChannel(), event.getUser(), event));
            cm.eventBus.post(jEvent);
        }

        super.onJoin(event);
    }

    @Override
    public void onKick(KickEvent<PircBotX> event) throws Exception {
        if(event.getRecipient().getNick().equals(event.getBot().getNick())) {
            ChannelManager cm = getChannelManager(event.getChannel().getName());
            @Cancelable BotKickedEvent kEvent = new BotKickedEvent(new EventContext(event.getBot(), server, event.getChannel(), event.getUser(), event));
            cm.eventBus.post(kEvent);
        }else{
            ChannelManager cm = getChannelManager(event.getChannel().getName());
            @Cancelable UserKickedEvent kEvent = new UserKickedEvent(new EventContext(event.getBot(), server, event.getChannel(), event.getUser(), event));
            cm.eventBus.post(kEvent);
        }
        super.onKick(event);
    }

    @Override
    public void onMessage(MessageEvent<PircBotX> event) throws Exception {
        if(inputEnabled) {
            ChannelManager cm = getChannelManager(event.getChannel().getName());

            try {
                if(event.getMessage().contains(event.getBot().getNick())){
                    @Cancelable ChannelPingEvent cEvent = new ChannelPingEvent(new EventContext(event.getBot(), server, event.getChannel(), event.getUser(), event));
                    cm.eventBus.post(cEvent);
                }else {
                    @Cancelable ChannelMessageEvent cEvent = new ChannelMessageEvent(new EventContext(event.getBot(), server, event.getChannel(), event.getUser(), event));
                    cm.eventBus.post(cEvent);
                }
                if (event.getMessage().startsWith("!")) {
                    int endCommandIndex = event.getMessage().contains(" ") ? event.getMessage().indexOf(" ") : event.getMessage().length();
                    String commandName = event.getMessage().substring(1, endCommandIndex);
                    Optional<Command> cO = cm.getCommand(commandName);
                    Optional<TextCommand> tcO = cm.getTextCommand(commandName);
                    WrappedEvent<MessageEvent<PircBotX>> wrappedEvent = new WrappedEvent<>(event);

                    if (cO.isPresent() && cO.get().isUserAllowed(event)) {
//                            System.out.println("MessengerPipe is null: " + (this.messengerPipeline == null));
//                            System.out.println("c.message is null: " + (c.getMessage(wrappedEvent) == null));
//                            System.out.println("Wrapped message is null: " + (wrappedEvent == null));
                        this.messengerPipeline.sendMessage(cO.get().getMessage(wrappedEvent), cO.get().commandType, wrappedEvent);
                    }else if(tcO.isPresent() && tcO.get().isUserAllowed(event)){
                        this.messengerPipeline.sendMessage(TextCommandParser.parseCommand(tcO.get().getMessage(wrappedEvent), wrappedEvent, cm.getChannelValues()),
                                tcO.get().getCommandType(), wrappedEvent);
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
        if(event.getUser().getNick().equals(event.getBot().getNick())) {
            @Cancelable BotQuitEvent qEvent = new BotQuitEvent(new EventContext(event.getBot(), server, null, event.getUser(), event));
            getChannelManagers().forEach(cm -> cm.eventBus.post(qEvent));
        }else{
            @Cancelable UserQuitEvent qEvent = new UserQuitEvent(new EventContext(event.getBot(), server, null, event.getUser(), event));
            getChannelManagers().forEach(cm -> cm.eventBus.post(qEvent));
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
//        channelManagers.entrySet().stream().forEach(e -> e.getValue().getFilters().forEach(f -> f.onSocketConnect(new WrappedEvent<>(event))));
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
