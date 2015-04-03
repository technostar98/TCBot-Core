import org.pircbotx.*;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.*;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.lang.Exception;
import java.lang.Override;

public class MyListener extends ListenerAdapter {
    @Override
    public void onGenericMessage(GenericMessageEvent event) {
        //When someone says ?helloworld respond with "Hello World"
        if (event.getMessage().startsWith("?helloworld")) {
            event.respond("Hello world!");
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration.Builder();
                .setName("TechnoBot")
                .setServerHostname("irc.esper.net")
                .addAutoJoinChannel("#thesixpack")
                .addListener(new MyListener())

        PircBotX bot = new PircBotX(configuration);
        bot.startBot();
    }

}