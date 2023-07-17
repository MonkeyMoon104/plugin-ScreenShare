package com.example.plugin.command;

import com.example.plugin.MainUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.ChatColor;

import java.io.File;

public class SSFinish implements CommandExecutor {

    private File inventoryDataFile;

    public SSFinish(File inventoryDataFile) {
        this.inventoryDataFile = inventoryDataFile;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            Player target = MainUtils.getInstance().getPlayer(args[0]);

            if (target != null) {
                Location targetLocation = MainUtils.getInstance().getPlayerLocations().get(target.getUniqueId());
                Location executorLocation = MainUtils.getInstance().getPlayerLocations().get(((Player) sender).getUniqueId());

                target.teleport(targetLocation);

                ((Player) sender).teleport(executorLocation);

                BukkitRunnable task = MainUtils.getInstance().getPlayerTeleportTasks().get(target.getUniqueId());
                if (task != null) {
                    task.cancel();
                    MainUtils.getInstance().getPlayerTeleportTasks().remove(target.getUniqueId());
                }

                MainUtils.getInstance().getPlayerLocations().remove(target.getUniqueId());
                MainUtils.getInstance().getPlayerLocations().remove(((Player) sender).getUniqueId());

                sender.sendMessage("I giocatori sono stati riposizionati alle coordinate precedenti.");
                return true;
            } else {
                sender.sendMessage("Giocatore non trovato.");
            }
        }
        return false;
    }
}


