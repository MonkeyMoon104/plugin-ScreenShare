package com.example.plugin;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainUtils {
    private static MainUtils instance;
    private final Map<UUID, Location> playerLocations = new HashMap<>();
    private final Map<UUID, BukkitRunnable> playerTeleportTasks = new HashMap<>();

    private MainUtils() {}

    public static MainUtils getInstance() {
        if (instance == null) {
            instance = new MainUtils();
        }
        return instance;
    }

    public Map<UUID, Location> getPlayerLocations() {
        return playerLocations;
    }

    public Map<UUID, BukkitRunnable> getPlayerTeleportTasks() {
        return playerTeleportTasks;
    }

    public void savePlayerData(Player player) {
        playerLocations.put(player.getUniqueId(), player.getLocation());
    }

    public void restorePlayerData(Player player) {
        Location location = playerLocations.get(player.getUniqueId());
        if (location != null) {
            player.teleport(location);
        }

        cancelTeleportTask(player);
        clearPlayerData(player);
    }



    public void cancelTeleportTask(Player player) {
        BukkitRunnable task = playerTeleportTasks.get(player.getUniqueId());
        if (task != null) {
            task.cancel();
            playerTeleportTasks.remove(player.getUniqueId());
        }
    }

    public void clearPlayerData(Player player) {
        playerLocations.remove(player.getUniqueId());
    }
    public Player getPlayer(String name) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }

}
