package de.jugglegaming.oneWayElytra.utils;

import de.jugglegaming.oneWayElytra.OneWayElytra;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Tools {

  private OneWayElytra oneWayElytra;

  public Tools(OneWayElytra oneWayElytra){
    oneWayElytra = oneWayElytra;
  }

  public String replaceVariables(String text) {
    return ChatColor.translateAlternateColorCodes('&', text)
              .replaceAll("%>>%", "»")
              .replaceAll("%<<%", "«")
              .replaceAll("%ue%", "ü")
              .replaceAll("%ae%", "ä")
              .replaceAll("%oe%", "ö")
              .replaceAll("%ss%", "ß")
              .replaceAll("%coin%", "⛂")
              .replaceAll("%coins%", "⛃");
  }

public String replaceVariables(String text, String location) {
    return ChatColor.translateAlternateColorCodes('&', text)
            .replaceAll("%>>%", "»")
            .replaceAll("%<<%", "«")
            .replaceAll("%ue%", "ü")
            .replaceAll("%ae%", "ä")
            .replaceAll("%oe%", "ö")
            .replaceAll("%ss%", "ß")
            .replaceAll("%coin%", "⛂")
            .replaceAll("%coins%", "⛃")
            .replaceAll("%location%", location + "");
}

  public String replaceVariables(String text, String location, boolean jumpToFlyMessageBool) {
    return ChatColor.translateAlternateColorCodes('&', text)
            .replaceAll("%>>%", "»")
            .replaceAll("%<<%", "«")
            .replaceAll("%ue%", "ü")
            .replaceAll("%ae%", "ä")
            .replaceAll("%oe%", "ö")
            .replaceAll("%ss%", "ß")
            .replaceAll("%coin%", "⛂")
            .replaceAll("%coins%", "⛃")
            .replaceAll("%location%", location + "")
            .replaceAll("%messageboolean%", jumpToFlyMessageBool + "");
  }

  public String replaceVariables(String text, Integer integer) {
    return ChatColor.translateAlternateColorCodes('&', text)
            .replaceAll("%>>%", "»")
            .replaceAll("%<<%", "«")
            .replaceAll("%ue%", "ü")
            .replaceAll("%ae%", "ä")
            .replaceAll("%oe%", "ö")
            .replaceAll("%ss%", "ß")
            .replaceAll("%coin%", "⛂")
            .replaceAll("%coins%", "⛃")
            .replaceAll("%radius%", integer + "")
            .replaceAll("%multiplier%", integer + "")
            .replaceAll("%boostMultiplier%", integer + "")
            .replaceAll("%boostmultiplier%", integer + "");
  }
  public String replaceVariables(String text, Integer integer, String location) {
        return ChatColor.translateAlternateColorCodes('&', text)
                .replaceAll("%>>%", "»")
                .replaceAll("%<<%", "«")
                .replaceAll("%ue%", "ü")
                .replaceAll("%ae%", "ä")
                .replaceAll("%oe%", "ö")
                .replaceAll("%ss%", "ß")
                .replaceAll("%coin%", "⛂")
                .replaceAll("%coins%", "⛃")
                .replaceAll("%radius%", integer + "")
                .replaceAll("%multiplier%", integer + "")
                .replaceAll("%boostMultiplier%", integer + "")
                .replaceAll("%boostmultiplier%", integer + "")
                .replaceAll("%location%", location + "");
  }

  public String replaceVariables(Player player, String text) {
    return ChatColor.translateAlternateColorCodes('&', text)
              .replaceAll("%>>%", "»")
              .replaceAll("%<<%", "«")
              .replaceAll("%ue%", "ü")
              .replaceAll("%ae%", "ä")
              .replaceAll("%oe%", "ö")
              .replaceAll("%ss%", "ß")
              .replaceAll("%coin%", "⛂")
              .replaceAll("%coins%", "⛃")
              .replaceAll("%player%", player.getName());
  }

  public String replaceVariables(String text, Double inputDouble) {
    return ChatColor.translateAlternateColorCodes('&', text)
                    .replaceAll("%>>%", "»")
                    .replaceAll("%<<%", "«")
                    .replaceAll("%ue%", "ü")
                    .replaceAll("%ae%", "ä")
                    .replaceAll("%oe%", "ö")
                    .replaceAll("%ss%", "ß")
                    .replaceAll("%coin%", "⛂")
                    .replaceAll("%coins%", "⛃")
                    .replaceAll("%money%", inputDouble.toString())
                    .replaceAll("%multiplier%", inputDouble + "")
                    .replaceAll("%boostMultiplier%", inputDouble + "")
                    .replaceAll("%boostmultiplier%", inputDouble + "");
  }

  public String replaceVariables(Player player, String text, Double money) {
    return ChatColor.translateAlternateColorCodes('&', text)
                    .replaceAll("%>>%", "»")
                    .replaceAll("%<<%", "«")
                    .replaceAll("%ue%", "ü")
                    .replaceAll("%ae%", "ä")
                    .replaceAll("%oe%", "ö")
                    .replaceAll("%ss%", "ß")
                    .replaceAll("%coin%", "⛂")
                    .replaceAll("%coins%", "⛃")
                    .replaceAll("%money%", money.toString())
                    .replaceAll("%player%", player.getName());
  }

  public String replaceVariables(String text, int time) {
    return ChatColor.translateAlternateColorCodes('&', text)
            .replaceAll("%>>%", "»")
            .replaceAll("%<<%", "«")
            .replaceAll("%ue%", "ü")
            .replaceAll("%ae%", "ä")
            .replaceAll("%oe%", "ö")
            .replaceAll("%ss%", "ß")
            .replaceAll("%coin%", "⛂")
            .replaceAll("%coins%", "⛃")
            .replaceAll("%time%", time + "");
  }



  public boolean isInt(String s) {
    try {
      Integer.parseInt(s);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  public boolean isDouble(String string) {
    try {
      Double.parseDouble(string);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

}
