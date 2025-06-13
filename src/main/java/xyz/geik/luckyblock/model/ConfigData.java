package xyz.geik.luckyblock.model;

import de.leonhard.storage.shaded.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import xyz.geik.glib.utils.ColorAPI;
import xyz.geik.luckyblock.Main;

public class ConfigData {
   private static HashMap<String, ConfigData> configData = new HashMap();
   private ConfigData.LuckyType type;
   private String displayName;
   private String dataName;
   private int health;
   private int playerMultiplier;
   private List<String> lore;
   private List<String> rewards;
   private boolean knockback;
   private boolean animation;

   public static HashMap<String, ConfigData> getConfigData() {
      return configData;
   }

   public ConfigData(ConfigData.LuckyType type, String dataName, String displayName, int health, int playerMultiplier, List<String> lore, List<String> rewards, boolean animation, boolean knockback) {
      this.type = type;
      this.displayName = displayName;
      this.dataName = dataName;
      this.health = health;
      this.playerMultiplier = playerMultiplier;
      this.lore = lore;
      this.rewards = rewards;
      this.animation = animation;
      this.knockback = knockback;
      getConfigData().put(dataName, this);
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

   @NotNull
   public static void loadAllConfigData() {
      getConfigData().clear();
      Main.getLuckyblockFile().singleLayerKeySet("lucky-blocks").forEach((data) -> {
         ConfigData.LuckyType type = ConfigData.LuckyType.EVENT;
         new ConfigData(type, data, Main.getLuckyblockFile().getText("lucky-blocks." + data + ".name"), Main.getLuckyblockFile().getInt("lucky-blocks." + data + ".health"), Main.getLuckyblockFile().getInt("lucky-blocks." + data + ".player-extender"), Main.getLuckyblockFile().getTextList("lucky-blocks." + data + ".lore"), Main.getLuckyblockFile().getTextList("lucky-blocks." + data + ".rewards"), Main.getLuckyblockFile().getBoolean("lucky-blocks." + data + ".animation"), Main.getLuckyblockFile().getBoolean("lucky-blocks." + data + ".knockback"));
      });
   }

   public ConfigData.LuckyType getType() {
      return this.type;
   }

   public String getDisplayName() {
      return this.displayName;
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

   public void setType(ConfigData.LuckyType type) {
      this.type = type;
   }

   public void setDisplayName(String displayName) {
      this.displayName = displayName;
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

   public static enum LuckyType {
      EVENT,
      PLAYER;

      // $FF: synthetic method
      private static ConfigData.LuckyType[] $values() {
         return new ConfigData.LuckyType[]{EVENT, PLAYER};
      }
   }
}
