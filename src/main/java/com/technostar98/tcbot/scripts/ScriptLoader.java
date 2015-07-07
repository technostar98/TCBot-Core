package com.technostar98.tcbot.scripts;

import com.google.common.collect.Lists;
import com.technostar98.tcbot.lib.Logger;
import com.technostar98.tcbot.lib.config.Configs;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.UserLevel;

import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author Bret 'Horfius' Dusseault
 * @version 1.0
 */
public class ScriptLoader {
    private final String SL_NAME, SL_FILEEND;
    private final String[] ARGS;
    private String scriptName;
    public Process process;

    protected ScriptLoader(String SL_NAME, String SL_FILEEND, String... args){
        this.SL_NAME = SL_NAME;
        this.SL_FILEEND = SL_FILEEND;
        this.ARGS = args;
    }

    public String getSLFileEnd(){
        return SL_NAME;
    }

    public String getSLName(){
        return SL_FILEEND;
    }

    public ProcessBuilder getDefaultBuilder(String scriptName, String server, Channel channel, User user, List<String> userArgs){
        this.scriptName = scriptName;

        List<String> args = Lists.newLinkedList();
        args.add(SL_NAME);
        args.add(scriptName + "." + SL_FILEEND);
        for(String s : ARGS) args.add(s);

        ProcessBuilder builder = new ProcessBuilder()
                .command(args)
                .directory(new File(Configs.getStringConfiguration("scriptDir").getValue()));

        StringJoiner chanULSJoiner = new StringJoiner(";");
        for(UserLevel ul : user.getUserLevels(channel)) chanULSJoiner.add(ul.name());

        Map<String, String> env = builder.environment();
        env.put("USER_NAME", user.getNick());
        env.put("CHAN", channel.getName());
        env.put("SERVER", server);
        env.put("USER_CHAN_ULS", chanULSJoiner.toString());

        for (int i = 0; i < userArgs.size(); i++) {
            env.put("arg" + i, userArgs.get(i));
        }

        return builder;
    }

    public InputStreamReader getInputReader(){
        return new InputStreamReader(process.getInputStream());
    }

    public void run(ProcessBuilder builder){
        process = ScriptManager.postScript(builder);
    }

    public void close(){
        process.destroy();

        if(process.exitValue() != 0) {
            Logger.warning("Script %s did not end properly, exit code %d.", scriptName, process.exitValue());
        }

        ScriptManager.freeProcess(process.hashCode());
    }
}
