package com.example.plugin.command;

import com.example.plugin.ScreenShare;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import com.example.plugin.MainUtils;
import org.bukkit.scheduler.BukkitRunnable;
import net.md_5.bungee.api.ChatColor;

import java.io.File;

public class SSCommand implements CommandExecutor { ;
    private File inventoryDataFile;

    public SSCommand(File inventoryDataFile) {
        this.inventoryDataFile = inventoryDataFile;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);

            if (target != null) {
                Player executor = (Player) sender;
                MainUtils.getInstance().getPlayerLocations().put(target.getUniqueId(), target.getLocation());
                MainUtils.getInstance().getPlayerLocations().put(executor.getUniqueId(), executor.getLocation());

                        target.getLocation().setYaw(0f);
                        target.getLocation().setPitch(0f);
                        target.teleport(new Location(target.getWorld(), -21, 114, -48));

                executor.teleport(new Location(executor.getWorld(), -49, 114, -48));

                target.setRotation(90, 0);
                executor.setRotation(270, 0);

                String message = ChatColor.BOLD + "SEI STATO PORTATO IN SS, NON DISCONNETTERTI ALTRIMENTI VERRAI BANNATO PERMANENTEMENTE";
                target.sendMessage(ChatColor.RED + message);

                sender.sendMessage("Le coordinate dei giocatori sono state memorizzate e teletrasportati.");
                return true;
            } else {
                sender.sendMessage("Giocatore non trovato.");
            }
        }
        return false;
    }

}


