package com.example.plugin;

import com.example.plugin.command.*;
import org.bukkit.plugin.java.JavaPlugin;
import com.example.plugin.listeners.SSPlayerQuitListener;
import org.bukkit.event.Listener;
import com.example.plugin.command.togli;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ScreenShare extends JavaPlugin {

    private static ScreenShare instance;
    private File inventoryDataFile;

    @Override
    public void onEnable() {
        instance = this;
        inventoryDataFile = new File(getDataFolder(), "inventory_data.txt");

        getCommand("ss").setExecutor(new SSCommand(inventoryDataFile));
        getCommand("finish_ss").setExecutor(new SSFinish(inventoryDataFile));
        getCommand("togli").setExecutor(new togli(inventoryDataFile));
        getCommand("ridai").setExecutor(new ridai(inventoryDataFile));
        getLogger().info("Il plugin è stato abilitato.");

        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        if (!inventoryDataFile.exists()) {
            try {
                inventoryDataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Crea un'istanza del tuo listener PlayerQuitEvent
        SSPlayerQuitListener quitListener = new SSPlayerQuitListener(this, inventoryDataFile);

        // Registra il listener nel tuo plugin
        getServer().getPluginManager().registerEvents(quitListener, this);

        // Crea una cartella per gli eventi
        File eventsFolder = new File(getDataFolder(), "listeners");

        // Verifica se la cartella degli eventi esiste
        if (!eventsFolder.exists()) {
            eventsFolder.mkdirs();
        }

        // Registra tutti gli eventi presenti nella cartella degli eventi
        registerEventsFromFolder(eventsFolder);
    }

    private void registerEventsFromFolder(File folder) {
        // Verifica se la directory è valida
        if (!folder.isDirectory()) {
            return;
        }

        // Ottieni tutti i file nella directory
        File[] files = folder.listFiles();

        // Verifica se ci sono file
        if (files == null) {
            return;
        }

        // Itera attraverso tutti i file
        for (File file : files) {
            // Verifica se il file è una classe Java
            if (file.isFile() && file.getName().endsWith(".java")) {
                try {
                    // Ottieni il nome della classe senza l'estensione .java
                    String className = file.getName().replace(".java", "");

                    // Carica la classe del listener degli eventi
                    Class<?> eventClass = Class.forName("com.example.plugin.listeners." + className);

                    // Verifica se la classe implementa l'interfaccia Listener
                    if (Listener.class.isAssignableFrom(eventClass)) {
                        // Crea un'istanza del listener
                        Listener listener = null;
                        try {
                            listener = (Listener) eventClass.getDeclaredConstructor(ScreenShare.class, File.class).newInstance(this, inventoryDataFile);
                        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                            e.printStackTrace();
                        }

                        // Verifica se l'istanza del listener è stata creata correttamente
                        if (listener != null) {
                            // Registra il listener nel tuo plugin
                            getServer().getPluginManager().registerEvents(listener, this);
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static ScreenShare getInstance() {
        return instance;
    }
}
