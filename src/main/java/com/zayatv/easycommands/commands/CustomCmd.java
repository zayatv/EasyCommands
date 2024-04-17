package com.zayatv.easycommands.commands;

import com.zayatv.easycommands.EasyCommands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class CustomCmd extends CCommand {

    private final EasyCommands plugin;

    public CustomCmd(String cmd, EasyCommands plugin) {
        super(cmd, plugin);
        this.plugin = plugin;
    }

    @Override
    public void run(CommandSender sender, String commandLabel, String[] arguments) {
        if (!(sender instanceof Player)) return;
        String cmdString = commandLabel.toString();
        if (!containsString(cmdString, plugin.getCommandsSection().getKeys(false))) return;

        Player player = (Player) sender;

        if (arguments.length == 0)
        {
            player.performCommand(plugin.getCommandsSection().getString(cmdString + ".old"));
            return;
        }

        String finalCmd = plugin.getCommandsSection().getString(cmdString + ".old");

        for (String arg : arguments)
        {
            ConfigurationSection args = plugin.getCommandsSection().getConfigurationSection(cmdString + ".args");

            if (!containsString(arg, args.getKeys(false)))
            {
                player.sendMessage("Argument is not registered!");
                continue;
            }

            System.out.println(args.getString(arg));

            finalCmd += " " + args.getString(arg);
        }

        System.out.println(finalCmd);

        player.performCommand(finalCmd);
    }

    private boolean containsString(String string, Set<String> set)
    {
        for (String s : set)
        {
            if (string.equals(s)) return true;
        }
        return false;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] arguments) {
        //Set<String> args = plugin.getCommandsSection().getConfigurationSection(cmd.toString() + ".args").getKeys(false);

        //List<String> arguments1 = new ArrayList<>();
        //arguments1.addAll(args);
        List<String> arguments1 = Arrays.asList("testing", "testing2");

        List<String> result = new ArrayList<String>();
        if (arguments.length == 1) {
            for (String argument : arguments1)
                if (argument.toLowerCase().startsWith(arguments[0].toLowerCase()))
                    result.add(argument);
            return result;
        }

        return result;
    }
}
