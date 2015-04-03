
package com.technostar.tcbot;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.lang.Exception;
import java.lang.Override;

public class MyListener extends ListenerAdapter {
    @Override
    public void onGenericMessage(GenericMessageEvent event) {
        String channel = event.getMessage();
        //When someone says ?helloworld respond with "Hello World"
        if (event.getMessage().startsWith("?helloworld")) {
            event.respond("Hello world!");
        }
        if (event.getMessage().contains("rpo")) {
            event.getBot().sendIRC().action("#TheSixPack", "eats rpo!");
        }
        if (event.getMessage().startsWith("!resetnick")) {
            event.getBot().sendIRC().changeNick("TechnoBot");
        }
        if (event.getMessage().startsWith("!burn")) {
            event.getBot().sendIRC().message("#TheSixPack", event.getUser().getNick() + " burns " + event.getMessage().substring(6) + "!");
        }
        if (event.getMessage().startsWith("!test")) {
            event.getBot().sendIRC().message("#TheSixPack,", "User: " + event.getUser().getNick() + ", IS_OP: " + event.getUser().isIrcop() + ", Identified with NS: " + event.getUser().isVerified() + ", Is away: " + event.getUser().isAway());
        }
        if (event.getMessage().startsWith("!disconnect")) {
            if (event.getUser().getNick().equalsIgnoreCase("Devin_Laptop") && event.getUser().isVerified()) {
                event.getBot().sendIRC().quitServer("I was told to quit by: " + event.getUser().getNick());
            }
            else event.respond("You aren't my master!");
        }
        if (event.getMessage().startsWith("!join")) {
            if (event.getUser().getNick().equalsIgnoreCase("Devin_Laptop") && event.getUser().isVerified()) {
                event.respond("Joining channel: " + event.getMessage().substring(6));
                event.getBot().sendIRC().joinChannel(event.getMessage().substring(6));
            }
            else event.respond("You aren't my master!");
        }
        // I am broken. someone fix me.
        if (event.getMessage().startsWith("!part")) {
            if (event.getUser().getNick().equalsIgnoreCase("Devin_Laptop") && event.getUser().isVerified()) {
                event.getBot().sendRaw().rawLine("PRIVMSG PART " + (event.getMessage().substring(6)));
            }
            else event.respond("You aren't my master!");
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration.Builder()
                .setName("TechnoBot")
                .setServerHostname("irc.esper.net")
                .addAutoJoinChannel("#TechnoDev")
                .addAutoJoinChannel("#TheSixPack")
                .addListener(new MyListener())
                .setRealName("TCBot")
                .buildConfiguration();




        PircBotX bot = new PircBotX(configuration);
        bot.startBot();
    }

}