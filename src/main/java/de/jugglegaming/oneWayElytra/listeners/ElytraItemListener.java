package de.jugglegaming.oneWayElytra.listeners;

import de.jugglegaming.oneWayElytra.OneWayElytra;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ElytraItemListener implements Listener {

    private OneWayElytra oneWayElytra;
    public ElytraItemListener(OneWayElytra oneWayElytra) {
        this.oneWayElytra = oneWayElytra;
    }

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent event){
        Player player = event.getPlayer();
        //TODO: SURVIVAL AND ADVENTURE MODE ONLY
        if(player.getGameMode().equals(GameMode.SURVIVAL) || player.getGameMode().equals(GameMode.ADVENTURE)){
            if(player.getInventory().getChestplate() != null){
                ItemStack itemStack = player.getInventory().getChestplate();
                ItemMeta meta = itemStack.getItemMeta();
                PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(oneWayElytra, "onewayelytra-elytraitem");
                if (dataContainer.has(key, PersistentDataType.STRING)) {
                    player.sendMessage("JO");
                } else {
                    player.sendMessage("NOPE");
                }
            } else {
                player.sendMessage("NO CHESTPLATE");
            }
        } else {
            player.sendMessage("WRONG GAMEMODE");
        }


    }

}
