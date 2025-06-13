package xyz.geik.luckyblock.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import xyz.geik.luckyblock.Main;
import xyz.geik.luckyblock.model.ConfigData;
import xyz.geik.luckyblock.model.Luckyblock;

public class Placeholders extends PlaceholderExpansion {
   public String getIdentifier() {
      return "luckyblock";
   }

   public String getAuthor() {
      return "Geik";
   }

   public String getVersion() {
      return "1.0";
   }

   public String onPlaceholderRequest(Player player, String identifier) {
      try {
         Luckyblock block = (Luckyblock)Luckyblock.getLuckyBlocks().get(identifier);
         if (block == null) {
            return Main.getMessageFile().getText("placeholders.error");
         } else {
            int health = block.getHealth();
            if (health == 0) {
               return Main.getMessageFile().getText("placeholders.block-death");
            } else {
               String progressBar = "";
               ConfigData data = (ConfigData)ConfigData.getConfigData().get(block.getDataName());
               health = (int)Math.ceil((double)(health * 30) / (double)data.getHealth());

               for(int i = 0; i < 30; ++i) {
                  if (health > i) {
                     progressBar = progressBar + "&a&l|";
                  } else {
                     progressBar = progressBar + "&7&l|";
                  }
               }

               return progressBar;
            }
         }
      } catch (Exception var8) {
         return "";
      }
   }
}
