package de.jugglegaming.oneWayElytra.commands;

import de.jugglegaming.oneWayElytra.OneWayElytra;
import de.jugglegaming.oneWayElytra.listeners.OneWayElytraListener;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class OneWayElytraCommand implements CommandExecutor {

    private OneWayElytra oneWayElytra;
    private OneWayElytraListener oneWayElytraListener;

    public OneWayElytraCommand(OneWayElytra oneWayElytra, OneWayElytraListener oneWayElytraListener) {
        this.oneWayElytra = oneWayElytra;
        this.oneWayElytraListener = oneWayElytraListener;
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player player){
            if(player.hasPermission("onewayelytra.command")){
                if(args.length == 5) {
                    // /onewayelytra location <set> [name] <radius> [number]
                    if(args[0].equalsIgnoreCase("location")){
                        if(args[1].equalsIgnoreCase("set")){
                            String locationName = args[2];
                            if(oneWayElytra.getFileManager().getConfig().getSection("locations").contains(locationName)) {
                                if (args[3].equalsIgnoreCase("radius")){
                                    try {
                                        int radius = Integer.parseInt(args[4]);
                                        if(radius >= 0){
                                            oneWayElytra.getFileManager().getConfig().set("locations." + locationName + ".radius", radius);
                                            oneWayElytra.getFileManager().saveConfig();
                                            player.sendMessage(oneWayElytra.prefix + oneWayElytra.getTools().replaceVariables(oneWayElytra.getFileManager().getMessages().getString("radiusSet"), Integer.valueOf(args[4]), locationName));
                                            oneWayElytra.getRadiusManager().loadAreas(oneWayElytra.getFileManager().getConfig());
                                        } else {
                                            sender.sendMessage(oneWayElytra.prefix + oneWayElytra.getTools().replaceVariables(oneWayElytra.getFileManager().getMessages().getString("valueMustBeHigherOrEqual")));
                                        }
                                    } catch(NumberFormatException e){
                                        sender.sendMessage(oneWayElytra.prefix + oneWayElytra.getTools().replaceVariables(oneWayElytra.getFileManager().getMessages().getString("valueMustBeInt")));
                                    }
                                }
                            } else {
                                player.sendMessage(oneWayElytra.prefix + oneWayElytra.getTools().replaceVariables(oneWayElytra.getFileManager().getMessages().getString("locationNotFound")));
                            }
                        } else {
                            player.sendMessage(oneWayElytra.prefix + oneWayElytra.getTools().replaceVariables(oneWayElytra.getFileManager().getMessages().getString("wrongArgs")));
                        }
                    } else {
                        player.sendMessage(oneWayElytra.prefix + oneWayElytra.getTools().replaceVariables(oneWayElytra.getFileManager().getMessages().getString("wrongArgs")));
                    }
                }
                if(args.length == 4) {
                    player.sendMessage(oneWayElytra.prefix + oneWayElytra.getTools().replaceVariables(oneWayElytra.getFileManager().getMessages().getString("wrongArgs")));
                }
                if (args.length == 3){
                    if(args[0].equalsIgnoreCase("location")){
                        if (args[1].equalsIgnoreCase("add")){
                            if(args[2] != null){
                                String locationName = args[2];
                                if(true) { // Check if Location exists
                                    oneWayElytra.getFileManager().getConfig().set("locations." + locationName + ".world", player.getLocation().getWorld().getName());
                                    oneWayElytra.getFileManager().getConfig().set("locations." + locationName + ".x", player.getLocation().getX());
                                    oneWayElytra.getFileManager().getConfig().set("locations." + locationName + ".y", player.getLocation().getY());
                                    oneWayElytra.getFileManager().getConfig().set("locations." + locationName + ".z", player.getLocation().getZ());
                                    oneWayElytra.getFileManager().getConfig().set("locations." + locationName + ".radius", oneWayElytra.getFileManager().getConfig().getInt("radius"));
                                    oneWayElytra.getFileManager().saveConfig();
                                    oneWayElytra.getRadiusManager().loadAreas(oneWayElytra.getFileManager().getConfig());
                                    player.sendMessage(oneWayElytra.prefix + oneWayElytra.getTools().replaceVariables(oneWayElytra.getFileManager().getMessages().getString("locationSet"), locationName));
                                } else{
                                    player.sendMessage(oneWayElytra.prefix + oneWayElytra.getTools().replaceVariables(oneWayElytra.getFileManager().getMessages().getString("locationNotFound")));
                                }
                            }
                        } else if (args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("delete")){
                            if(args[2] != null){
                                String locationName = args[2];
                                if(oneWayElytra.getFileManager().getConfig().getSection("locations").contains(locationName)) {
                                    oneWayElytra.getFileManager().getConfig().remove("locations." + locationName);
                                    oneWayElytra.getFileManager().saveConfig();
                                    oneWayElytra.getRadiusManager().loadAreas(oneWayElytra.getFileManager().getConfig());
                                    player.sendMessage(oneWayElytra.prefix + oneWayElytra.getTools().replaceVariables(oneWayElytra.getFileManager().getMessages().getString("locationRemoved"), locationName));
                                } else{
                                    player.sendMessage(oneWayElytra.prefix + oneWayElytra.getTools().replaceVariables(oneWayElytra.getFileManager().getMessages().getString("locationNotFound")));
                                }
                            }
                        } else {
                            player.sendMessage(oneWayElytra.prefix + oneWayElytra.getTools().replaceVariables(oneWayElytra.getFileManager().getMessages().getString("wrongArgs")));
                        }
                    } else {
                        player.sendMessage(oneWayElytra.prefix + oneWayElytra.getTools().replaceVariables(oneWayElytra.getFileManager().getMessages().getString("wrongArgs")));
                    }

                }
                if(args.length == 2){
                    if(args[0].equalsIgnoreCase("location")){
                        if (args[1].equalsIgnoreCase("list")){
                            for(Object locName : oneWayElytra.getFileManager().getConfig().getSection("locations").getKeys()) {
                                // Werte auslesen
                                String world = oneWayElytra.getFileManager().getConfig().getString("locations." + locName + ".world");
                                double x = oneWayElytra.getFileManager().getConfig().getDouble("locations." + locName + ".x");
                                double y = oneWayElytra.getFileManager().getConfig().getDouble("locations." + locName + ".y");
                                double z = oneWayElytra.getFileManager().getConfig().getDouble("locations." + locName + ".z");
                                double radius = oneWayElytra.getFileManager().getConfig().getDouble("locations." + locName + ".radius");

                                // Spieler ausgeben
                                player.sendMessage("§e" + locName + " §7→ " + oneWayElytra.getFileManager().getMessages().getString("world") + ": §a" + world + " §7X: §a" + x + " §7Y: §a" + y + " §7Z: §a" + z + " §7" + oneWayElytra.getFileManager().getMessages().getString("radius") + ": §a" + radius);
                            }

                        } else {
                            player.sendMessage(oneWayElytra.prefix + oneWayElytra.getTools().replaceVariables(oneWayElytra.getFileManager().getMessages().getString("wrongArgs")));
                        }
                    } else if (args[0].equalsIgnoreCase("tag")){
                        //TODO: REMOVE COMMAND IF NEEDED AND FIND ANOTHER SOLUTION
                        if (args[1].equalsIgnoreCase("add")){
                            if(player.getInventory().getChestplate() != null){
                                ItemStack itemStack = player.getInventory().getChestplate();
                                ItemMeta meta = itemStack.getItemMeta();
                                PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
                                NamespacedKey key = new NamespacedKey(oneWayElytra, "onewayelytra-elytraitem");
                                dataContainer.set(key, PersistentDataType.BYTE, (byte) 1);
                                itemStack.setItemMeta(meta);
                                player.sendMessage("Successfully added tag");
                            }
                        } else if (args[1].equalsIgnoreCase("remove")){
                            ItemStack itemStack = player.getInventory().getChestplate();
                            ItemMeta meta = itemStack.getItemMeta();
                            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
                            NamespacedKey key = new NamespacedKey(oneWayElytra, "onewayelytra-elytraitem");
                            dataContainer.remove(key);
                            itemStack.setItemMeta(meta);
                            player.sendMessage("Successfully removed tag");
                        } else if(args[1].equalsIgnoreCase("info")){
                            if(player.getInventory().getChestplate() != null){
                                ItemStack itemStack = player.getInventory().getChestplate();
                                ItemMeta meta = itemStack.getItemMeta();
                                PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
                                player.sendMessage(dataContainer.toString());
                            }
                        }
                    } else {
                        player.sendMessage(oneWayElytra.prefix + oneWayElytra.getTools().replaceVariables(oneWayElytra.getFileManager().getMessages().getString("wrongArgs")));
                    }
                }
                if (args.length == 1){
                    player.sendMessage(oneWayElytra.prefix + oneWayElytra.getTools().replaceVariables(oneWayElytra.getFileManager().getMessages().getString("wrongArgs")));
                }
                if (args.length == 0){
                    player.sendMessage(oneWayElytra.prefix + "§7/onewayelytra location <add/delete> [name]");
                    player.sendMessage(oneWayElytra.prefix + "§7/onewayelytra location set [name] radius [number]");
                    player.sendMessage(oneWayElytra.prefix + "§7/onewayelytra location list");
                }
            } else {
                player.sendMessage(oneWayElytra.prefix + oneWayElytra.getTools().replaceVariables(oneWayElytra.getFileManager().getMessages().getString("commandDenied")));
            }
        } else {
            sender.sendMessage(oneWayElytra.prefix + "*~*~*~*~*~*~*~* <<OneWayElytra>> *~*~*~*~*~*~*~*");
            sender.sendMessage(oneWayElytra.prefix + "Version: " + oneWayElytra.getDescription().getVersion());
            sender.sendMessage(oneWayElytra.prefix + "Author: " + oneWayElytra.getDescription().getAuthors());
            sender.sendMessage(oneWayElytra.prefix + "*~*~*~*~*~*~*~* <<OneWayElytra>> *~*~*~*~*~*~*~*");
        }
        return false;
    }
}
