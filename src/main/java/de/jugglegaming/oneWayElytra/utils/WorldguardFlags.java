package de.jugglegaming.oneWayElytra.utils;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.FlagValueChangeHandler;

public class WorldguardFlags extends FlagValueChangeHandler {

    protected WorldguardFlags(Session session, Flag flag) {
        super(session, flag);
    }

    public static StateFlag OWE_START ;
    public static StateFlag OWE_ENTRY ;
    public static StateFlag OWE_ITEM ;

    public static void load(){
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag startFlag = new StateFlag("owe-start", false);
            registry.register(startFlag);
            OWE_START = startFlag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("owe-start");
            if (existing instanceof StateFlag) {
                OWE_START = (StateFlag) existing;
            }
        }
        try {
            StateFlag entryFlag = new StateFlag("owe-entry", true);
            registry.register(entryFlag);
            OWE_ENTRY = entryFlag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("owe-entry");
            if (existing instanceof StateFlag) {
                OWE_ENTRY = (StateFlag) existing;
            }
        }
        try {
            StateFlag itemFlag = new StateFlag("owe-item", true);
            registry.register(itemFlag);
            OWE_ITEM = itemFlag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("owe-item");
            if (existing instanceof StateFlag) {
                OWE_ITEM = (StateFlag) existing;
            }
        }
    }

    @Override
    protected void onInitialValue(LocalPlayer localPlayer, ApplicableRegionSet applicableRegionSet, Object o) {

    }

    @Override
    protected boolean onSetValue(LocalPlayer localPlayer, Location location, Location location1, ApplicableRegionSet applicableRegionSet, Object o, Object t1, MoveType moveType) {
        return false;
    }

    @Override
    protected boolean onAbsentValue(LocalPlayer localPlayer, Location location, Location location1, ApplicableRegionSet applicableRegionSet, Object o, MoveType moveType) {
        return false;
    }
}
