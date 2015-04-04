package com.technostar.tcbot.bot;

import com.technostar.tcbot.command.CommandManager;
import com.technostar.tcbot.command.CommandType;
import com.technostar.tcbot.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.*;
import org.pircbotx.hooks.types.*;

import java.util.HashMap;
import java.util.List;

/**
 * Will hold all of the listeners for a bot instance
 */
public class ListenerPipeline extends ListenerAdapter<PircBotX>{
    public HashMap<String, CommandManager> commandManagers = new HashMap<>(); //Command managers for channels
    public MessengerPipeline messengerPipeline;
    public String server;

    //TODO logging incoming messages/all things

    public ListenerPipeline(String server, String... channels){
        this.server = server;
        for(String s : channels) commandManagers.put(s, new CommandManager(server, s)); //TODO load command managers from database
        messengerPipeline  = new MessengerPipeline(server, 5);
    }

    public ListenerPipeline(String server, List<String> channels){
        this.server = server;
        channels.stream().forEach(s -> commandManagers.put(s, new CommandManager(server, s)));
    }

    public CommandManager getCommandManager(String channel){
        return commandManagers.getOrDefault(channel, null);
    }

    public void removeCommandManager(String channel){
        commandManagers.remove(channel);
    }

    @Override
    public void onAction(ActionEvent<PircBotX> event) throws Exception {
        super.onAction(event);
    }

    @Override
    public void onChannelInfo(ChannelInfoEvent<PircBotX> event) throws Exception {
        super.onChannelInfo(event);
    }

    @Override
    public void onConnect(ConnectEvent<PircBotX> event) throws Exception {
        super.onConnect(event);
    }

    @Override
    public void onDisconnect(DisconnectEvent<PircBotX> event) throws Exception {
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
        super.onInvite(event);
    }

    @Override
    public void onJoin(JoinEvent<PircBotX> event) throws Exception {
        super.onJoin(event);
    }

    @Override
    public void onKick(KickEvent<PircBotX> event) throws Exception {
        super.onKick(event);
    }

    @Override
    public void onMessage(MessageEvent<PircBotX> event) throws Exception {
        if(event.getMessage().startsWith("!")) {
            int endCommandIndex = event.getMessage().contains(" ") ? event.getMessage().indexOf(" ") : event.getMessage().length();
            this.messengerPipeline.sendMessage(event.getChannel().getName(),
                    getCommandManager(event.getChannel().getName()).enactCommand(CommandType.USER_MESSAGE,
                    event.getMessage().substring(1, endCommandIndex), new WrappedEvent<>(event)), event.getBot());
        }
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
        super.onNickAlreadyInUse(event);
    }

    @Override
    public void onNickChange(NickChangeEvent<PircBotX> event) throws Exception {
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