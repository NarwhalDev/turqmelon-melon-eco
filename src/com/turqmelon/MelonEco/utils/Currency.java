package com.turqmelon.MelonEco.utils;


import org.bukkit.ChatColor;

import java.text.NumberFormat;
import java.util.UUID;

/******************************************************************************
 *  Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com   *
 *  For more information, see LICENSE.TXT.                                    *
 ******************************************************************************/
public class Currency {

    private UUID uuid;
    private String singular;
    private String plural;
    private String symbol = null;
    private ChatColor color = ChatColor.WHITE;
    private boolean decimalSupported = false;

    private boolean payable = true;
    private boolean defaultCurrency = false;

    private double defaultBalance = 0;

    public Currency(UUID uuid, String singular, String plural) {
        this.uuid = uuid;
        this.singular = singular;
        this.plural = plural;
    }

    public void setSingular(String singular) {
        this.singular = singular;
    }

    public void setPlural(String plural) {
        this.plural = plural;
    }

    public void setDefaultBalance(double defaultBalance) {
        this.defaultBalance = defaultBalance;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getSingular() {
        return singular;
    }

    public String getPlural() {
        return plural;
    }

    public double getDefaultBalance() {
        return defaultBalance;
    }

    public String format(double amount){
        StringBuilder amt = new StringBuilder();
        if (getSymbol() != null){
            amt.append(getSymbol());
        }
        if (isDecimalSupported()){
            NumberFormat nf = NumberFormat.getCurrencyInstance();
            amt.append(nf.format(amount).substring(1));
        }
        else{
            String v = String.valueOf(amount);
            String[] vv = v.split(".");
            if (vv.length > 0){
                v = vv[0];
            }
            amt.append(NumberFormat.getInstance().format(Double.parseDouble(v)));
        }
        amt.append(" ");
        if (amount != 1){
            amt.append(getPlural());
        }
        else{
            amt.append(getSingular());
        }
        return amt.toString();
    }

    public boolean isDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(boolean defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public boolean isPayable() {
        return payable;
    }

    public void setPayable(boolean payable) {
        this.payable = payable;
    }

    public boolean isDecimalSupported() {
        return decimalSupported;
    }

    public void setDecimalSupported(boolean decimalSupported) {
        this.decimalSupported = decimalSupported;
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
