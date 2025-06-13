package xyz.geik.luckyblock.handlers;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.geik.glib.effects.DeployFirework;
import xyz.geik.luckyblock.Main;
import xyz.geik.luckyblock.api.AnimationEndEvent;
import xyz.geik.luckyblock.model.Animation;
import xyz.geik.luckyblock.model.Luckyblock;

public class AnimationEvent implements Listener {
   @EventHandler
   public void animationEvent(AnimationEndEvent event) {
      Luckyblock luckyblock = event.getLuckyblock();
      luckyblock.setHealth(luckyblock.calcHealth());
      Location loc = luckyblock.getLocation().clone();
      Block block = loc.getBlock();
      block.setType(Luckyblock.getMaterial());
      Animation.createLuckySkull(loc.clone().add(0.5D, -1.25D, 0.5D));
      DeployFirework.spawn(loc.add(0.0D, 3.5D, 0.0D), 3, Color.YELLOW);
      loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1.0F, 1.0F);
      Bukkit.broadcastMessage(Main.getMessageFile().getText("luckyblock.event-start"));
   }
}
