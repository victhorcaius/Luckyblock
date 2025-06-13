package xyz.geik.luckyblock.handlers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.geik.luckyblock.Main;
import xyz.geik.luckyblock.api.LuckyKillEvent;
import xyz.geik.luckyblock.model.ConfigData;
import xyz.geik.luckyblock.model.Luckyblock;

public class KillLuckyEvent implements Listener {
   @EventHandler
   public void killLuckyEvent(@NotNull LuckyKillEvent event) {
      ConfigData data = (ConfigData)ConfigData.getConfigData().get(event.getLuckyblock().getDataName());
      Luckyblock luckyblock = event.getLuckyblock();
      luckyblock.setHealth(0);
      if (data.getType().equals(ConfigData.LuckyType.PLAYER)) {
         luckyblock.getLocation().getBlock().setType(Material.AIR);
      } else if (data.getType().equals(ConfigData.LuckyType.EVENT)) {
         Luckyblock.createExplosion(luckyblock.getLocation(), Sound.ENTITY_WITHER_DEATH);
         Bukkit.broadcastMessage(Main.getMessageFile().getText("luckyblock.event-finish"));
         luckyblock.getLocation().getBlock().setType(Material.BEDROCK);
      }

   }
}
