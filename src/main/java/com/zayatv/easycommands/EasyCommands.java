package com.zayatv.easycommands;

import com.zayatv.easycommands.commands.CCommand;
import com.zayatv.easycommands.commands.CustomCmd;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;

public final class EasyCommands extends JavaPlugin {

    private File commandsFile;
    private FileConfiguration commands;

    private ConfigurationSection commandsSection;

    private static SimpleCommandMap scm;
    private SimplePluginManager spm;

    @Override
    public void onEnable() {
        setupSimpleCommandMap();

        createConfig();
        commandsSection = commands.getConfigurationSection("commands");

        CustomCmd[] cmds = new CustomCmd[commandsSection.getKeys(false).size()];
        int i = 0;
        for (String cmd : commandsSection.getKeys(false))
        {
            cmds[i] = new CustomCmd(cmd, this);
            i++;
        }
        registerCommands(cmds);
    }

    @Override
    public void onDisable() {
        saveResource("commands.yml", false);
    }

    private void registerCommands(CCommand... commands) {
        Arrays.stream(commands).forEach(command -> scm.register("customcommands", command));//Register the plugin
    }

    private void setupSimpleCommandMap() {
        spm = (SimplePluginManager) this.getServer().getPluginManager();
        Field f = null;
        try {
            f = SimplePluginManager.class.getDeclaredField("commandMap");
        } catch (Exception e) {
            e.printStackTrace();
        }
        f.setAccessible(true);
        try {
            scm = (SimpleCommandMap) f.get(spm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SimpleCommandMap getCommandMap() {
        return scm;
    }

    public FileConfiguration getConfig() {
        return this.commands;
    }

    public ConfigurationSection getCommandsSection()
    {
        return this.commandsSection;
    }

    private void createConfig() {
        commandsFile = new File(getDataFolder(), "commands.yml");
        if (!commandsFile.exists()) {
            commandsFile.getParentFile().mkdirs();
            saveResource("commands.yml", false);
        }

        commands = YamlConfiguration.loadConfiguration(commandsFile);
    }
}
