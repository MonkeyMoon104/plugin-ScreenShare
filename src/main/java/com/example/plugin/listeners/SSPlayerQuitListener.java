package com.example.plugin.listeners;

import com.example.plugin.MainUtils;
import com.example.plugin.ScreenShare;
import com.example.plugin.command.ridai;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;

public class SSPlayerQuitListener implements Listener {

    private ridai ridaiCommand;
    private File inventoryDataFile;
    private final ScreenShare plugin;

    public SSPlayerQuitListener(ScreenShare plugin, File inventoryDataFile) {
        this.plugin = plugin;
        this.inventoryDataFile = inventoryDataFile;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player target = event.getPlayer();
        Player executor = target.getKiller();

        // Verifica se il giocatore è coinvolto in un controllo SS
        if (MainUtils.getInstance().getPlayerLocations().containsKey(target.getUniqueId())) {
            // Esegui le operazioni correlate al controllo SS
            plugin.getServer().broadcastMessage("Il giocatore " + target.getName() + " si è disconnesso durante un controllo SS!");

            // Riporta il giocatore all'ultima posizione
            Location lastLocation = MainUtils.getInstance().getPlayerLocations().get(target.getUniqueId());
            if (lastLocation != null) {
                target.teleport(lastLocation);
                MainUtils.getInstance().getPlayerLocations().remove(target.getUniqueId());
            }

            // Esegui l'azione per il ban del giocatore
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "ban " + target.getName() + " Sei stato permanentemente bannato per disconnessione durante un controllo SS.");

            // Rimuovi i dati del giocatore dal MainUtils
            MainUtils.getInstance().clearPlayerData(target);
        }
    }

}
