package de.jugglegaming.oneWayElytra;

import de.jugglegaming.oneWayElytra.commands.OneWayElytraCMD;
import de.jugglegaming.oneWayElytra.listeners.OneWayElytraListener;
import de.jugglegaming.oneWayElytra.managers.FileManager;
import de.jugglegaming.oneWayElytra.managers.RadiusManager;
import de.jugglegaming.oneWayElytra.utils.Tools;
import de.jugglegaming.oneWayElytra.utils.WorldguardFlags;
import de.jugglegaming.oneWayElytra.utils.WorldguardUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class OneWayElytra extends JavaPlugin {

    private static OneWayElytra Instance;

    public String prefix;
    private FileManager fileManager;
    private RadiusManager radiusManager;
    public Tools tools;
    PluginManager pluginManager = Bukkit.getPluginManager();

    public ConsoleCommandSender ccs = Bukkit.getServer().getConsoleSender();

    private OneWayElytraListener oneWayElytraListener;

    private WorldguardFlags wgFlag;

    @Override
    public void onLoad() {
        connectToWorldguard();
    }

    @Override
    public void onEnable() {
        Instance = this;
        fileManager = new FileManager(this);
        fileManager.loadConfig(Instance);
        ccs.sendMessage("[OneWayElytra] Config loaded.");
        radiusManager = new RadiusManager(fileManager.getConfig());
        fileManager.loadMessages(Instance);
        ccs.sendMessage("[OneWayElytra] Language loaded: §7" + fileManager.getConfig().getString("language"));
        this.tools = new Tools(this);
        prefix = this.getTools().replaceVariables(getFileManager().getMessages().getString("prefix") + " ");
        ccs.sendMessage(prefix + "Enabling...");
        registerListener();
        ccs.sendMessage(prefix + "Listeners successfully registered!");
        registerCommands();
        ccs.sendMessage(prefix + "Commands successfully registered!");

        ccs.sendMessage(prefix + "*~*~*~*~*~*~*~* <<OneWayElytra>> *~*~*~*~*~*~*~*");
        ccs.sendMessage(prefix + "Plugin successfully loaded!");
        ccs.sendMessage(prefix + "Version: " + getDescription().getVersion());
        ccs.sendMessage(prefix + "Author: " + getDescription().getAuthors());
        ccs.sendMessage(prefix + "*~*~*~*~*~*~*~* <<OneWayElytra>> *~*~*~*~*~*~*~*");

    }

    @Override
    public void onDisable() {
        ccs.sendMessage(prefix + "*~*~*~*~*~*~*~* <<OneWayElytra>> *~*~*~*~*~*~*~*");
        ccs.sendMessage(prefix + "Plugin successfully disabled!");
        ccs.sendMessage(prefix + "*~*~*~*~*~*~*~* <<OneWayElytra>> *~*~*~*~*~*~*~*");
    }

    public static OneWayElytra getInstance() {return Instance;}

    public Tools getTools(){
        return  tools;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    @EventHandler
    public void registerListener() {
        oneWayElytraListener = new OneWayElytraListener(this);
        pluginManager.registerEvents(oneWayElytraListener, this);
    }

    @EventHandler
    public void registerCommands() {
        //Objects.requireNonNull(getCommand("onewayelytra")).setExecutor(new OneWayElytraCMD(Instance));
        getCommand("onewayelytra").setExecutor(new OneWayElytraCMD(Instance, oneWayElytraListener));
    }

    public void connectToWorldguard(){
        Plugin worldGuard = pluginManager.getPlugin("WorldGuard");
        if(WorldguardUtils.isWorldGuardInstalled()){
            WorldguardFlags.load();
            ccs.sendMessage("[OneWayElytra] Successfully connected to WorldGuard!");
        } else {
            ccs.sendMessage("[OneWayElytra] Worldguard not found.");
        }
    }

    public RadiusManager getRadiusManager() {
        return radiusManager;
    }
}
