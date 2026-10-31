
package de.jugglegaming.oneWayElytra.managers;

import de.jugglegaming.oneWayElytra.OneWayElytra;
import de.jugglegaming.oneWayElytra.utils.RadiusArea;
import dev.dejvokep.boostedyaml.YamlDocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AreaManager {

    private final YamlDocument config;
    private final List<RadiusArea> areas = new ArrayList<>();
    private OneWayElytra oneWayElytra;

    public AreaManager(YamlDocument config, OneWayElytra oneWayElytra) {
        this.config = config;
        this.oneWayElytra = oneWayElytra;

        migrateLocations();
        loadAreas(oneWayElytra.getFileManager().getConfig());
    }

    private void migrateLocations() {
        boolean defaultMessage = config.getBoolean("jumpToFlyMessage", false);

        if (config.getSection("locations") == null) {
            return;
        }

        for (String key : config.getSection("locations")
                .getRoutesAsStrings(false)) {

            String path = "locations." + key + ".jumpToFlyMessage";

            if (!config.contains(path)) {
                config.set(path, defaultMessage);
            }
        }

        try {
            config.save();
        } catch (IOException e) {
            System.err.println(
                    "[OneWayElytra] Could not migrate locations!"
            );
            e.printStackTrace();
        }
    }

    public void loadAreas(YamlDocument config) {
        areas.clear();

        if (this.config.getSection("locations") == null) {
            return;
        }

        for (String key : this.config.getSection("locations")
                .getRoutesAsStrings(false)) {

            String path = "locations." + key;

            String world = this.config.getString(path + ".world");

            double x = this.config.getDouble(path + ".x");
            double y = this.config.getDouble(path + ".y");
            double z = this.config.getDouble(path + ".z");
            double radius = this.config.getDouble(path + ".radius");

            boolean jumpToFlyMessage =
                    this.config.getBoolean(path + ".jumpToFlyMessage", false);

            areas.add(new RadiusArea(
                    key,
                    world,
                    x,
                    y,
                    z,
                    radius,
                    jumpToFlyMessage
            ));
        }
    }

    public boolean isInAnyArea(org.bukkit.Location location) {
        for (RadiusArea area : areas) {
            if (area.isInside(location)) {
                return true;
            }
        }

        return false;
    }

    public boolean isInAnyStartMessageArea(
            org.bukkit.Location location
    ) {
        for (RadiusArea area : areas) {
            if (area.isStartMessageEnabled()
                    && area.isInside(location)) {
                return true;
            }
        }

        return false;
    }

    public List<RadiusArea> getAreas() {
        return areas;
    }
}
