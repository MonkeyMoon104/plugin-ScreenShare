package com.example.plugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.example.plugin.ScreenShare;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ridai implements CommandExecutor {

    private File inventoryDataFile;

    public ridai(File inventoryDataFile) {
        this.inventoryDataFile = inventoryDataFile;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Utilizzo: /ridai <giocatore>");
            return true;
        }

        String playerName = args[0];
        Player targetPlayer = ScreenShare.getInstance().getServer().getPlayer(playerName);
        if (targetPlayer == null) {
            sender.sendMessage("Il giocatore " + playerName + " non è online.");
            return true;
        }

        restoreInventoryData(targetPlayer);
        return true;
    }

    private void restoreInventoryData(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(inventoryDataFile))) {
            String line;
            boolean readingPlayerData = false;

            while ((line = reader.readLine()) != null) {
                if (line.equals("Player: " + player.getName())) {
                    readingPlayerData = true;
                } else if (line.equals("EndOfPlayer")) {
                    readingPlayerData = false;
                } else if (readingPlayerData) {
                    String[] itemData = line.split(":");
                    int slot = Integer.parseInt(itemData[0]);
                    String itemType = itemData[1];
                    int amount = Integer.parseInt(itemData[2]);

                    inventory.setItem(slot, new ItemStack(org.bukkit.Material.valueOf(itemType), amount));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}