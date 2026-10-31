package de.jugglegaming.oneWayElytra.managers;

import de.jugglegaming.oneWayElytra.OneWayElytra;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FileManager {

    private final OneWayElytra oneWayElytra;

    private final ConsoleCommandSender ccs =
            Bukkit.getServer().getConsoleSender();

    private static YamlDocument config;
    private static YamlDocument messages;
    private static YamlDocument database;

    public FileManager(OneWayElytra oneWayElytra) {
        this.oneWayElytra = oneWayElytra;
    }

    public void loadConfig(OneWayElytra oneWayElytra) {
        try {
            UpdaterSettings.Builder updater = UpdaterSettings.builder()
                    .setVersioning(new BasicVersioning("config-version"))
                    .setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS);

            for (int version = 1; version <= 100; version++) {
                updater.addIgnoredRoute(String.valueOf(version), "locations", '.');
            }

            UpdaterSettings updaterSettings = updater.build();

            config = YamlDocument.create(
                    new File(oneWayElytra.getDataFolder(), "config.yml"),
                    Objects.requireNonNull(
                            getClass().getResourceAsStream("/config.yml")
                    ),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder()
                            .setAutoUpdate(true)
                            .build(),
                    DumperSettings.DEFAULT,
                    updaterSettings
            );

            config.save();

        } catch (IOException e) {
            ccs.sendMessage(
                    "§cCould not load config.yml. Shutting down this plugin..."
            );
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(oneWayElytra);
        }
    }

    public void loadDatabase(OneWayElytra oneWayElytra) {
        try {
            database = YamlDocument.create(
                    new File(oneWayElytra.getDataFolder(), "database.yml"),
                    Objects.requireNonNull(
                            getClass().getResourceAsStream("/database.yml")
                    ),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder()
                            .setAutoUpdate(true)
                            .build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder()
                            .setVersioning(
                                    new BasicVersioning("config-version")
                            )
                            .setOptionSorting(
                                    UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS
                            )
                            .build()
            );

            database.update();
            database.save();

        } catch (IOException e) {
            ccs.sendMessage(
                    "§cCould not load database.yml. Shutting down this plugin..."
            );
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(oneWayElytra);
        }
    }

    public void loadMessages(OneWayElytra oneWayElytra) {
        try {
            File languagesFolder =
                    new File(oneWayElytra.getDataFolder(), "languages");

            if (!languagesFolder.exists()) {
                languagesFolder.mkdirs();
            }

            messages = YamlDocument.create(
                    new File(
                            languagesFolder,
                            config.getString("language") + ".yml"
                    ),
                    Objects.requireNonNull(
                            getClass().getResourceAsStream(
                                    "/" + config.getString("language") + ".yml"
                            )
                    ),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder()
                            .setAutoUpdate(true)
                            .build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder()
                            .setVersioning(
                                    new BasicVersioning("config-version")
                            )
                            .setOptionSorting(
                                    UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS
                            )
                            .build()
            );

            messages.update();
            messages.save();

        } catch (IOException e) {
            ccs.sendMessage(
                    "§cCould not load messages-file. Shutting down this plugin..."
            );
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(oneWayElytra);
        }
    }

    public void saveConfig() {
        if (config == null) {
            return;
        }

        try {
            config.save();
        } catch (IOException e) {
            ccs.sendMessage("§cCould not save config.yml!");
            e.printStackTrace();
        }
    }

    public YamlDocument getConfig() {
        return config;
    }

    public YamlDocument getMessages() {
        return messages;
    }

    public YamlDocument getDatabase() {
        return database;
    }
}
