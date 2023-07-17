package com.example.plugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.example.plugin.ScreenShare;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class togli implements CommandExecutor {

    private File inventoryDataFile;

    public togli(File inventoryDataFile) {
        this.inventoryDataFile = inventoryDataFile;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Questo comando può essere eseguito solo da un giocatore.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("Utilizzo: /togli <giocatore>");
            return true;
        }

        String playerName = args[0];
        Player targetPlayer = ScreenShare.getInstance().getServer().getPlayer(playerName);
        if (targetPlayer == null) {
            sender.sendMessage("Il giocatore " + playerName + " non è online.");
            return true;
        }

        saveInventoryData(targetPlayer);
        targetPlayer.getInventory().clear();
        return true;
    }

    private void saveInventoryData(Player player) {
        PlayerInventory inventory = player.getInventory();

        try (PrintWriter writer = new PrintWriter(new FileWriter(inventoryDataFile))) {
            writer.println("Player: " + player.getName());

            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack item = inventory.getItem(i);
                if (item != null) {
                    writer.println(i + ":" + item.getType().toString() + ":" + item.getAmount());
                }
            }

            writer.println("EndOfPlayer");
            writer.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
