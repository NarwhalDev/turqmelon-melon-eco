package com.turqmelon.MelonEco;


/******************************************************************************
 *  Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com   *
 *  For more information, see LICENSE.TXT.                                    *
 ******************************************************************************/

import com.turqmelon.MelonEco.commands.*;
import com.turqmelon.MelonEco.data.DataStore;
import com.turqmelon.MelonEco.data.YamlStorage;
import com.turqmelon.MelonEco.listeners.JoinListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MelonEco extends JavaPlugin {

    private static DataStore dataStore = null;
    private static MelonEco instance;

    @Override
    public void onEnable() {

        if (!getDataFolder().exists()){
            getDataFolder().mkdir();
        }

        instance = this;
        dataStore = new YamlStorage("YAML", false, new File(getDataFolder(), "data.yml"));

        getDataStore().initalize();

        getDataStore().loadCurrencies();

        getServer().getPluginManager().registerEvents(new JoinListener(), this);

        getCommand("balance").setExecutor(new BalanceCommand());
        getCommand("baltop").setExecutor(new BalTopCommand());
        getCommand("ecogive").setExecutor(new EcoGiveCommand());
        getCommand("ecoset").setExecutor(new EcoSetCommand());
        getCommand("ecotake").setExecutor(new EcoTakeCommand());
        getCommand("pay").setExecutor(new PayCommand());
        getCommand("currencies").setExecutor(new CurrencyCommand());

    }

    @Override
    public void onDisable() {
        if (getDataStore() != null){
            getDataStore().close();
        }
    }

    public static MelonEco getInstance() {
        return instance;
    }

    public static DataStore getDataStore() {
        return dataStore;
    }
}
