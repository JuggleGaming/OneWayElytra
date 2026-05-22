package de.jugglegaming.oneWayElytra.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import de.jugglegaming.oneWayElytra.OneWayElytra;
import de.jugglegaming.oneWayElytra.utils.ActionBar;
import de.jugglegaming.oneWayElytra.utils.WorldguardFlags;
import de.jugglegaming.oneWayElytra.utils.WorldguardUtils;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.List;

public class OneWayElytraListener implements Listener {

    private int radius;
    private int boostMultiplier;
    private List<Player> playersFlying = new ArrayList<>();
    private List<Player> playersBoosted = new ArrayList<>();

    private List<Location> positions = new ArrayList<>();

    private OneWayElytra oneWayElytra;

    public OneWayElytraListener(OneWayElytra oneWayElytra){
        this.oneWayElytra = oneWayElytra;
        this.radius = oneWayElytra.getFileManager().getConfig().getInt("radius");
        Bukkit.getScheduler().runTaskTimer(oneWayElytra, () -> {

            for (Player player : Bukkit.getOnlinePlayers()) {
                if(WorldguardUtils.isWorldGuardInstalled()){

                    com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(player.getLocation());
                    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                    RegionQuery query = container.createQuery();
                    ApplicableRegionSet set = query.getApplicableRegions(loc);
                    LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

                    boolean wgAllowed = set.testState(localPlayer, WorldguardFlags.ONEWAYELYTRA);
                    boolean inRadiusArea = oneWayElytra.getRadiusManager().isInAnyArea(player.getLocation());

                    if ((wgAllowed || inRadiusArea) && !playersFlying.contains(player) && player.isOnGround()) {
                        if(player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE && oneWayElytra.getFileManager().getConfig().getBoolean("adventure")) {
                            player.setAllowFlight(true);
                        }
                    }

                }

                if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
                    if (playersFlying.contains(player)
                            && !player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isAir()) {

                        player.setGliding(false);
                        playersBoosted.remove(player);

                        Bukkit.getScheduler().runTaskLater(oneWayElytra, () -> {
                            playersFlying.remove(player);
                            player.setAllowFlight(false);
                        }, 5);
                    }
                }
            }
        }, 0, 3);
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
        }


    }

    public boolean isAllowedToFly(Player player) {
        boolean wgAllowed = false;
        if (WorldguardUtils.isWorldGuardInstalled()) {
            com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(player.getLocation());
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            ApplicableRegionSet set = query.getApplicableRegions(loc);
            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

            wgAllowed = set.testState(localPlayer, WorldguardFlags.ONEWAYELYTRA);
        }

        boolean inRadiusArea = oneWayElytra.getRadiusManager().isInAnyArea(player.getLocation());

        return wgAllowed || inRadiusArea;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        if(event.getEntityType() == EntityType.PLAYER){
            Player player = (Player) event.getEntity();
            if(playersFlying.contains(event.getEntity())){
                if(event.getCause() == EntityDamageEvent.DamageCause.FALL){
                    event.setCancelled(true);
                } else if(event.getCause() == EntityDamageEvent.DamageCause.FLY_INTO_WALL){
                    event.setCancelled(true);
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



}