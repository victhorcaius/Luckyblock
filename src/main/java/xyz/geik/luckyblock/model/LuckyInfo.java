package xyz.geik.luckyblock.model;

import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import xyz.geik.glib.utils.ColorAPI;
import xyz.geik.luckyblock.Main;

public class LuckyInfo {
   LuckyInfo.LuckyType type;
   String name;
   String dataName;
   int health;
   int playerMultiplier;
   List<String> lore;
   List<String> rewards;
   boolean knockback;
   boolean animation;

   public LuckyInfo(LuckyInfo.LuckyType type, String dataName, String name, int health, int playerMultiplier, List<String> lore, List<String> rewards, boolean knockback, boolean animation) {
      this.type = type;
      this.dataName = dataName;
      this.name = name;
      this.playerMultiplier = playerMultiplier;
      this.lore = lore;
      this.rewards = rewards;
      this.health = health;
      this.knockback = knockback;
      this.animation = animation;
   }

   @Contract("_ -> new")
   @NotNull
   public static LuckyInfo getLuckyInfo(String key) {
      if (Main.getLuckyblockFile().contains("lucky-blocks." + key)) {
         LuckyInfo.LuckyType type = LuckyInfo.LuckyType.EVENT;
         return new LuckyInfo(type, key, Main.getLuckyblockFile().getText("lucky-blocks." + key + ".name"), Main.getLuckyblockFile().getInt("lucky-blocks." + key + ".health"), Main.getLuckyblockFile().getInt("lucky-blocks." + key + ".player-extender"), Main.getLuckyblockFile().getTextList("lucky-blocks." + key + ".lore"), Main.getLuckyblockFile().getTextList("lucky-blocks." + key + ".rewards"), Main.getLuckyblockFile().getBoolean("lucky-blocks." + key + ".animation"), Main.getLuckyblockFile().getBoolean("lucky-blocks." + key + ".knockback"));
      } else {
         return null;
      }
   }

   public static String isDisplayNameExist(String name) {
      String result = null;
      Iterator var2 = Main.getLuckyblockFile().singleLayerKeySet("lucky-blocks").iterator();

      while(var2.hasNext()) {
         String key = (String)var2.next();
         result = Main.getLuckyblockFile().getText("lucky-blocks." + key + ".name").equalsIgnoreCase(ColorAPI.colorize(name)) ? key : null;
         if (result != null) {
            break;
         }
      }

      return result;
   }

   public void setType(LuckyInfo.LuckyType type) {
      this.type = type;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setDataName(String dataName) {
      this.dataName = dataName;
   }

   public void setHealth(int health) {
      this.health = health;
   }

   public void setPlayerMultiplier(int playerMultiplier) {
      this.playerMultiplier = playerMultiplier;
   }

   public void setLore(List<String> lore) {
      this.lore = lore;
   }

   public void setRewards(List<String> rewards) {
      this.rewards = rewards;
   }

   public void setKnockback(boolean knockback) {
      this.knockback = knockback;
   }

   public void setAnimation(boolean animation) {
      this.animation = animation;
   }

   public LuckyInfo.LuckyType getType() {
      return this.type;
   }

   public String getName() {
      return this.name;
   }

   public String getDataName() {
      return this.dataName;
   }

   public int getHealth() {
      return this.health;
   }

   public int getPlayerMultiplier() {
      return this.playerMultiplier;
   }

   public List<String> getLore() {
      return this.lore;
   }

   public List<String> getRewards() {
      return this.rewards;
   }

   public boolean isKnockback() {
      return this.knockback;
   }

   public boolean isAnimation() {
      return this.animation;
   }

   public static enum LuckyType {
      EVENT,
      PLAYER;

      // $FF: synthetic method
      private static LuckyInfo.LuckyType[] $values() {
         return new LuckyInfo.LuckyType[]{EVENT, PLAYER};
      }
   }
}
