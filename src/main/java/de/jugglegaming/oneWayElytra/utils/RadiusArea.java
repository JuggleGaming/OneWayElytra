package de.jugglegaming.oneWayElytra.utils;

import org.bukkit.Location;

public class RadiusArea {

    private final String name;
    private final String world;
    private final double x, y, z;
    private final double radiusSquared;
    private final boolean message;

    public RadiusArea(String name, String world, double x, double y, double z, double radius, boolean message) {
        this.name = name;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.radiusSquared = radius * radius;
        this.message = message;
    }

    public boolean isInside(Location loc) {
        if (!loc.getWorld().getName().equals(world)) {
            return false;
        }

        double dx = loc.getX() - x;
        double dy = loc.getY() - y;
        double dz = loc.getZ() - z;

        return (dx * dx + dy * dy + dz * dz) <= radiusSquared;
    }



    public boolean isStartMessageEnabled() {
        return message;
    }
}
