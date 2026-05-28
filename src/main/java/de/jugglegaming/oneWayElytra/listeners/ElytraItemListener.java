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



}
