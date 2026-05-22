package de.jugglegaming.oneWayElytra.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.entity.Player;

public class WorldguardHook {

    public boolean canFly(Player player) {

        com.sk89q.worldedit.util.Location loc =
                BukkitAdapter.adapt(player.getLocation());

        RegionContainer container =
                WorldGuard.getInstance()
                        .getPlatform()
                        .getRegionContainer();

        RegionQuery query = container.createQuery();

        ApplicableRegionSet set =
                query.getApplicableRegions(loc);

        LocalPlayer localPlayer =
                WorldGuardPlugin.inst().wrapPlayer(player);

        return set.testState(
                localPlayer,
                WorldguardFlags.ONEWAYELYTRA
        );
    }

}
