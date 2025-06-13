package xyz.geik.luckyblock.model;

import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import xyz.geik.luckyblock.Main;
import xyz.geik.luckyblock.api.AnimationEndEvent;

public class Animation {
   private boolean animate;
   private boolean inAnimation;

   public Animation(boolean animate) {
      this.animate = animate;
      if (animate) {
         this.inAnimation = false;
      }

   }

   public void spawn(Luckyblock luckyblock) {
      removeArmorStand(luckyblock.getLocation());
      if (this.isAnimate()) {
         this.setInAnimation(true);
         Location loc = luckyblock.getLocation().clone();
         loc.add(0.5D, 1.7D, 0.5D);
         ArmorStand stand = createLuckySkull(loc);
         this.animation(loc, stand, 0, luckyblock);
      } else {
         Bukkit.getPluginManager().callEvent(new AnimationEndEvent(luckyblock));
      }

   }

   private void animation(Location loc, ArmorStand stand, int count, Luckyblock luckyblock) {
      int countClone = count + 1;
      if (count == 50) {
         stand.remove();
         this.setInAnimation(false);
         Bukkit.getPluginManager().callEvent(new AnimationEndEvent(luckyblock));
      } else {
         loc.subtract(0.0D, 0.06D, 0.0D);
         stand.teleport(loc);
         Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            this.animation(loc, stand, countClone, luckyblock);
         }, 2L);
      }
   }

   private static void removeArmorStand(Location location) {
      try {
         ArmorStand armorStand = (ArmorStand)((List)location.getWorld().getNearbyEntities(location, 1.0D, 1.0D, 1.0D).stream().filter((type) -> {
            return type.getType().equals(EntityType.ARMOR_STAND);
         }).collect(Collectors.toList())).get(0);
         if (armorStand != null) {
            armorStand.remove();
         }
      } catch (Exception var2) {
      }
   }

   @NotNull
   public static ArmorStand createLuckySkull(Location loc) {
      ArmorStand stand = (ArmorStand)loc.getWorld().spawn(loc, ArmorStand.class);
      stand.setInvisible(true);
      stand.setGravity(false);
      stand.setSmall(false);
      stand.setInvulnerable(true);
      stand.setHelmet(Luckyblock.getSkull());
      return stand;
   }

   public boolean isAnimate() {
      return this.animate;
   }

   public boolean isInAnimation() {
      return this.inAnimation;
   }

   public void setAnimate(boolean animate) {
      this.animate = animate;
   }

   public void setInAnimation(boolean inAnimation) {
      this.inAnimation = inAnimation;
   }
}
