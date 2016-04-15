package com.turqmelon.MelonEco.commands;

/******************************************************************************
 *  Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com   *
 *  For more information, see LICENSE.TXT.                                    *
 ******************************************************************************/

import com.turqmelon.MelonEco.MelonEco;
import com.turqmelon.MelonEco.utils.Account;
import com.turqmelon.MelonEco.utils.AccountManager;
import com.turqmelon.MelonEco.utils.Currency;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class EcoTakeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {


        new BukkitRunnable(){
            @Override
            public void run() {
                if (!sender.hasPermission("eco.take")){
                    sender.sendMessage("§c§l[Eco] §cYou don't have permission to take.");
                    return ;
                }

                if (args.length < 2){
                    sender.sendMessage("§c§l[Eco] §cAllows you to take currency from players.");
                    sender.sendMessage("§c§l[Eco] §cUsage: §f/ecotake <Account> <Amount> [Currency]");
                    return ;
                }

                Currency currency = AccountManager.getDefaultCurrency();

                if (args.length == 3){
                    currency = AccountManager.getCurrency(args[2]);
                }

                if (currency != null){

                    double amount;

                    if (currency.isDecimalSupported()){
                        try {

                            amount = Double.parseDouble(args[1]);
                            if (amount <= 0){
                                throw new NumberFormatException();
                            }

                        }catch(NumberFormatException ex){
                            sender.sendMessage("§c§l[Eco] §cPlease provide a valid amount.");
                            return ;
                        }
                    }
                    else{
                        try {

                            amount = Integer.parseInt(args[1]);
                            if (amount <= 0){
                                throw new NumberFormatException();
                            }

                        }catch(NumberFormatException ex){
                            sender.sendMessage("§c§l[Eco] §cPlease provide a valid amount.");
                            return ;
                        }
                    }


                    Account target  = AccountManager.getAccount(args[0]);
                    if (target != null){

                        target.setBalance(currency, target.getBalance(currency)-amount);

                        MelonEco.getDataStore().saveAccount(target);

                        sender.sendMessage("§a§l[Eco] §aYou took " + currency.getColor() + currency.format(amount) + "§a from " + target.getDisplayName() + ".");

                    }
                    else{
                        sender.sendMessage("§c§l[Eco] §cTarget account not found.");
                    }

                }
                else{
                    sender.sendMessage("§c§l[Eco] §cUnknown currency.");
                }
            }
        }.runTaskAsynchronously(MelonEco.getInstance());


        return true;
    }
}
