package de.jugglegaming.oneWayElytra.listeners;

import de.jugglegaming.oneWayElytra.OneWayElytra;
import de.jugglegaming.oneWayElytra.utils.ActionBar;
import de.jugglegaming.oneWayElytra.utils.WorldguardHook;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.*;

public class OneWayElytraListener implements Listener {

    private int radius;
    private int boostMultiplier;
    private List<Player> playersFlying = new ArrayList<>();
    private List<Player> playersFalling = new ArrayList<>();
    private List<Player> playersBoosted = new ArrayList<>();

    private List<Location> positions = new ArrayList<>();

    private OneWayElytra oneWayElytra;
    private WorldguardHook worldguardHook;

    public OneWayElytraListener(OneWayElytra oneWayElytra, WorldguardHook worldguardHook) {
        this.oneWayElytra = oneWayElytra;
        this.worldguardHook = worldguardHook;
        this.radius = oneWayElytra.getFileManager().getConfig().getInt("radius");

        Bukkit.getScheduler().runTaskTimer(oneWayElytra, () -> {

            final NamespacedKey key = new NamespacedKey(oneWayElytra, "onewayelytra-elytraitem");

            for (Player player : Bukkit.getOnlinePlayers()) {

                boolean wgStartAllowed = false;

                if (worldguardHook != null) {
                    wgStartAllowed = worldguardHook.canStart(player);
                }

                boolean inRadiusArea =
                        oneWayElytra.getRadiusManager()
                                .isInAnyArea(player.getLocation());

                boolean hasTaggedElytra = false;
                ItemStack chestplate = player.getInventory().getChestplate();
                if (chestplate != null && chestplate.hasItemMeta()) {
                    PersistentDataContainer container = chestplate.getItemMeta().getPersistentDataContainer();
                    if (container.has(key, PersistentDataType.BYTE)) {
                        hasTaggedElytra = true;
                    }
                }

                if (hasTaggedElytra && worldguardHook != null && !worldguardHook.isItemAllowed(player)) {
                    hasTaggedElytra = false;
                }

                if (isAllowedToFly(player)
                        && !playersFlying.contains(player)
                        && player.isOnGround()) {

                    if (player.getGameMode() == GameMode.SURVIVAL
                            || (player.getGameMode() == GameMode.ADVENTURE
                            && oneWayElytra.getFileManager()
                            .getConfig()
                            .getBoolean("adventure"))) {

                        player.setAllowFlight(true);
                    }
                }

                if (!(wgStartAllowed || inRadiusArea || hasTaggedElytra)
                        && !playersFlying.contains(player)
                        && (player.getGameMode() == GameMode.SURVIVAL
                        || player.getGameMode() == GameMode.ADVENTURE)) {
                    player.setAllowFlight(false);
                }

                if (player.getGameMode() == GameMode.SURVIVAL
                        || player.getGameMode() == GameMode.ADVENTURE) {

                    if (playersFlying.contains(player)
                            && !player.getLocation()
                            .getBlock()
                            .getRelative(BlockFace.DOWN)
                            .getType()
                            .isAir()) {
                        player.setGliding(false);
                        playersBoosted.remove(player);

                        Bukkit.getScheduler().runTaskLater(oneWayElytra, () -> {
                                    playersFlying.remove(player);
                                    player.setAllowFlight(false);
                                    ItemStack landingChest = player.getInventory().getChestplate();
                                    if (landingChest != null && landingChest.hasItemMeta()) {
                                        PersistentDataContainer container = landingChest.getItemMeta().getPersistentDataContainer();
                                        if (container.has(key, PersistentDataType.BYTE)) {
                                            player.getInventory().setChestplate(null);
                                        }
                                    }
                                }, 5
                        );
                    }
                }
            }

        }, 0, 3);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

        if (to == null || (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ())) return;

        if (playersFlying.contains(player)
                && worldguardHook != null
                && !worldguardHook.isEntryAllowed(player, to)) {

            playersFlying.remove(player);
            playersFalling.add(player);

            player.setGliding(false);
            player.setFlying(false);
            player.setAllowFlight(false);

            player.setFallDistance(0f);
            player.setVelocity(new Vector(0, 0, 0));

            event.setTo(from);
            ActionBar.send(player, oneWayElytra.getTools().replaceVariables(oneWayElytra.getFileManager().getMessages().getString("areaEntryNotAllowed")));
        }
        if (playersFalling.contains(player) && player.isOnGround()) {
            ItemStack chestplate = player.getInventory().getChestplate();
            if (chestplate != null && chestplate.hasItemMeta()) {
                PersistentDataContainer container = chestplate.getItemMeta().getPersistentDataContainer();
                final NamespacedKey key = new NamespacedKey(oneWayElytra, "onewayelytra-elytraitem");
                if (container.has(key, PersistentDataType.BYTE)) {
                    player.getInventory().setChestplate(null);
                }
            }
            Bukkit.getScheduler().runTaskLater(oneWayElytra, () -> {
                playersFalling.remove(player);
            }, 1L);
        }
    }

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent event){
        Player player = event.getPlayer();

        if(player.getGameMode().equals(GameMode.SURVIVAL) || player.getGameMode().equals(GameMode.ADVENTURE)) {
            if(isAllowedToFly(player)){
                event.setCancelled(true);
                event.getPlayer().setGliding(true);
                playersFlying.add(event.getPlayer());
                ActionBar.send(event.getPlayer(), oneWayElytra.getTools().replaceVariables(oneWayElytra.getFileManager().getMessages().getString("boostMessage")));
                Bukkit.getScheduler().runTaskLater(oneWayElytra, () -> {
                    player.setAllowFlight(false);
                }, 1L);
            }
            if(player.getInventory().getChestplate() != null){
                ItemStack itemStack = player.getInventory().getChestplate();
                ItemMeta meta = itemStack.getItemMeta();
                PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
                final NamespacedKey key = new NamespacedKey(oneWayElytra, "onewayelytra-elytraitem");
                if (dataContainer.has(key, PersistentDataType.BYTE)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    public boolean isAllowedToFly(Player player) {
        boolean wgStartAllowed = false;
        if (worldguardHook != null) {
            wgStartAllowed = worldguardHook.canStart(player);
        }
        boolean inRadiusArea = oneWayElytra.getRadiusManager().isInAnyArea(player.getLocation());
        boolean hasTaggedElytra = false;

        ItemStack chestplate = player.getInventory().getChestplate();

        if (chestplate != null && chestplate.hasItemMeta()) {
            PersistentDataContainer container = chestplate.getItemMeta().getPersistentDataContainer();
            final NamespacedKey key = new NamespacedKey(oneWayElytra, "onewayelytra-elytraitem");
            if (container.has(key, PersistentDataType.BYTE)) {
                hasTaggedElytra = true;
            }
        }

        if (hasTaggedElytra && worldguardHook != null && !worldguardHook.isItemAllowed(player)) {
            hasTaggedElytra = false;
        }

        if (hasTaggedElytra) return true;
        return wgStartAllowed || inRadiusArea;
    }


    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        if(event.getEntityType() == EntityType.PLAYER){
            Player player = (Player) event.getEntity();
            if(playersFlying.contains(player) || playersFalling.contains(player)){
                if(event.getCause().equals(EntityDamageEvent.DamageCause.FALL)
                        || event.getCause().equals(EntityDamageEvent.DamageCause.FLY_INTO_WALL)
                        || event.getCause().equals(EntityDamageEvent.DamageCause.CONTACT)){
                    event.setCancelled(true);
                    player.setFallDistance(0f);
                }
            }
        }
    }

    @EventHandler
    public void onBoost(PlayerInteractEvent event){
        if (!playersBoosted.contains(event.getPlayer()) && playersFlying.contains(event.getPlayer())) {
            this.boostMultiplier = oneWayElytra.getFileManager().getConfig().getInt("boostMultiplier");
            if(event.getAction().equals(Action.LEFT_CLICK_AIR)){
                event.setCancelled(true);
                playersBoosted.add(event.getPlayer());
                event.getPlayer().setVelocity(event.getPlayer().getLocation().getDirection().multiply(boostMultiplier));
            } else if(event.getAction().equals(Action.RIGHT_CLICK_AIR)){
                event.setCancelled(true);
                playersBoosted.add(event.getPlayer());
                event.getPlayer().setVelocity(event.getPlayer().getLocation().getDirection().multiply(boostMultiplier));
            }
        }
    }

    @EventHandler
    public void onEntityGlide(EntityToggleGlideEvent event){
        if (event.getEntityType() == EntityType.PLAYER && playersFlying.contains(event.getEntity())) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        World fromWorld = event.getFrom().getWorld();
        World toWorld = event.getTo().getWorld();

        if (!fromWorld.equals(toWorld)) {
            if(playersFlying.contains(event.getPlayer())){
                event.getPlayer().setGliding(false);
                playersFlying.remove(event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack chestplate = player.getInventory().getChestplate();
        if(playersFlying.contains(player)){
            if (chestplate != null && chestplate.hasItemMeta()) {
                PersistentDataContainer container = chestplate.getItemMeta().getPersistentDataContainer();
                final NamespacedKey key = new NamespacedKey(oneWayElytra, "onewayelytra-elytraitem");
                if (container.has(key, PersistentDataType.BYTE)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onItemClick(InventoryClickEvent event){
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if(playersFlying.contains(player)){
            final NamespacedKey key = new NamespacedKey(oneWayElytra, "onewayelytra-elytraitem");
            if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
                ItemStack current = event.getCurrentItem();
                if (current != null && current.hasItemMeta()) {
                    PersistentDataContainer container = current.getItemMeta().getPersistentDataContainer();
                    if (container.has(key, PersistentDataType.BYTE)) {
                        event.setCancelled(true);
                    }
                }
            }
            if (event.isShiftClick()) {
                ItemStack current = event.getCurrentItem();
                if (current != null && current.hasItemMeta()) {
                    PersistentDataContainer container = current.getItemMeta().getPersistentDataContainer();
                    if (container.has(key, PersistentDataType.BYTE)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}