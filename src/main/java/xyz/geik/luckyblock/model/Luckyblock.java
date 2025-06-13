package xyz.geik.luckyblock.model;

import dev.dbassett.skullcreator.SkullCreator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import xyz.geik.glib.serializer.SerializerAPI;
import xyz.geik.luckyblock.Main;
import xyz.geik.luckyblock.api.LuckyKillEvent;

public class Luckyblock {
   private static ItemStack skull = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWM0OTcwZWE5MWFiMDZlY2U1OWQ0NWZjZTc2MDRkMjU1NDMxZjJlMDNhNzM3YjIyNjA4MmM0Y2NlMWFjYTFjNCJ9fX0=");
   private static Material material = Material.getMaterial(Main.getConfigFile().getString("blockColor") + "_STAINED_GLASS");
   private static HashMap<String, Luckyblock> luckyblocks = new HashMap();
   private String dataName;
   private UUID uuid;
   private int health = 0;
   private Location location;
   private Animation animation;

   public static ItemStack getSkull() {
      return skull;
   }

   public static void setSkull(ItemStack head) {
      skull = head;
   }

   public static Material getMaterial() {
      return material;
   }

   public static HashMap<String, Luckyblock> getLuckyBlocks() {
      return luckyblocks;
   }

   public Luckyblock(String dataName, UUID uuid, Location location) {
      this.dataName = dataName;
      this.uuid = uuid;
      this.animation = new Animation(((ConfigData)ConfigData.getConfigData().get(dataName)).isAnimation());
      this.location = location;
      luckyblocks.put(uuid.toString(), this);
   }

   public void spawn() {
      ConfigData configData = (ConfigData)ConfigData.getConfigData().get(this.dataName);
      if (!configData.getType().equals(ConfigData.LuckyType.PLAYER)) {
         if (this.getHealth() > 0) {
            this.setHealth(this.calcHealth());
         } else {
            this.getAnimation().spawn(this);
         }

      }
   }

   public void killLucky() {
      Bukkit.getPluginManager().callEvent(new LuckyKillEvent(this));
   }

   public void save() {
      String uuid = this.getUuid().toString();
      Main.getLuckyDataFile().set(uuid + ".name", this.getDataName());
      Main.getLuckyDataFile().set(uuid + ".location", SerializerAPI.getLocSterilizer().getString(this.getLocation(), "/"));
      Main.getLuckyDataFile().set(uuid + ".health", this.getHealth());
   }

   public static void removeLuckyBlock(Luckyblock block, Player breaker) {
      Main.getLuckyDataFile().remove(block.getUuid().toString());
      block.killLucky();
      getLuckyBlocks().remove(block.getUuid());
      breaker.sendMessage(Main.getMessageFile().getText("block-removed"));
   }

   public int calcHealth() {
      int baseHealth = ((ConfigData)ConfigData.getConfigData().get(this.dataName)).getHealth();
      int playerMultiplier = ((ConfigData)ConfigData.getConfigData().get(this.dataName)).getPlayerMultiplier();
      return playerMultiplier > 0 ? baseHealth + playerMultiplier * Bukkit.getOnlinePlayers().size() : baseHealth;
   }

   public static void createExplosion(@NotNull Location location, Sound sound) {
      World world = location.getWorld();
      if (world != null) {
         world.playSound(location, sound, 1.0F, 1.0F);
         Iterator var3 = world.getNearbyEntities(location, 5.0D, 5.0D, 5.0D).iterator();

         while(var3.hasNext()) {
            Entity entity = (Entity)var3.next();
            if (entity instanceof Player) {
               Vector vector = entity.getLocation().getDirection().multiply(-0.75D);
               vector.setY(0.5D);
               entity.setVelocity(vector);
            }
         }

      }
   }

   public static void loadAllLuckyBlocks() {
      Main.getLuckyDataFile().singleLayerKeySet().forEach((uuid) -> {
         Location location = SerializerAPI.getLocSterilizer().getLocation(Main.getLuckyDataFile().getString(uuid + ".location"), "/");
         String dataName = Main.getLuckyDataFile().getString(uuid + ".name");
         new Luckyblock(dataName, UUID.fromString(uuid), location);
         location.getBlock().setType(Material.BEDROCK);
      });
   }

   public static Luckyblock getLuckyFromFile(String uuid) {
      String key = Main.getLuckyDataFile().getText(uuid + ".name");
      Location loc = SerializerAPI.getLocSterilizer().getLocation(Main.getLuckyDataFile().getString(uuid + ".location"), "/");
      if (!Main.getLuckyDataFile().contains("lucky-blocks." + key)) {
         return null;
      } else {
         Luckyblock block = new Luckyblock(key, UUID.fromString(uuid), loc);
         block.setHealth(Main.getLuckyDataFile().getInt(uuid + ".health"));
         return block;
      }
   }

   public String getDataName() {
      return this.dataName;
   }

   public UUID getUuid() {
      return this.uuid;
   }

   public int getHealth() {
      return this.health;
   }

   public Location getLocation() {
      return this.location;
   }

   public Animation getAnimation() {
      return this.animation;
   }

   public void setDataName(String dataName) {
      this.dataName = dataName;
   }

   public void setUuid(UUID uuid) {
      this.uuid = uuid;
   }

   public void setHealth(int health) {
      this.health = health;
   }

   public void setLocation(Location location) {
      this.location = location;
   }

   public void setAnimation(Animation animation) {
      this.animation = animation;
   }
}
