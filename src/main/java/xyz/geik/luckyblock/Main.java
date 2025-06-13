package xyz.geik.luckyblock;

import de.leonhard.storage.Config;
import de.leonhard.storage.Json;
import dev.dbassett.skullcreator.SkullCreator;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.geik.glib.GLibAPI;
import xyz.geik.glib.file.StorageAPI;
import xyz.geik.luckyblock.commands.Commands;
import xyz.geik.luckyblock.commands.TabComplete;
import xyz.geik.luckyblock.handlers.AnimationEvent;
import xyz.geik.luckyblock.handlers.KillLuckyEvent;
import xyz.geik.luckyblock.handlers.Listeners;
import xyz.geik.luckyblock.model.ConfigData;
import xyz.geik.luckyblock.model.Luckyblock;
import xyz.geik.luckyblock.utils.Placeholders;

public class Main extends JavaPlugin {
   private static Main instance;
   private static Config config;
   private static Config messages;
   private static Config luckyblocks;
   private static Json luckyData;

   public static Main getInstance() {
      return instance;
   }

   public void onEnable() {
      instance = this;
      new GLibAPI(this);
      loadFiles();
      handlerRegister();
      registerCommands();
      Luckyblock.setSkull(SkullCreator.itemFromBase64(getConfigFile().getString("skull")));
      ConfigData.loadAllConfigData();
      Luckyblock.loadAllLuckyBlocks();
      if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
         (new Placeholders()).register();
      }

   }

   public void onDisable() {
      Iterator var1 = Luckyblock.getLuckyBlocks().values().iterator();

      while(var1.hasNext()) {
         Luckyblock luckyBlock = (Luckyblock)var1.next();
         luckyBlock.killLucky();
      }

   }

   public static Config getConfigFile() {
      return config;
   }

   public static Config getMessageFile() {
      return messages;
   }

   public static Config getLuckyblockFile() {
      return luckyblocks;
   }

   public static Json getLuckyDataFile() {
      return luckyData;
   }

   private static void loadFiles() {
      config = (new StorageAPI()).initConfig("config");
      messages = (new StorageAPI()).initConfig("messages");
      luckyblocks = (new StorageAPI()).initConfig("luckyblocks");
      luckyData = (new StorageAPI()).initJson("lucky-data");
   }

   private static void handlerRegister() {
      PluginManager pluginManager = Bukkit.getPluginManager();
      pluginManager.registerEvents(new Listeners(), getInstance());
      pluginManager.registerEvents(new KillLuckyEvent(), getInstance());
      pluginManager.registerEvents(new AnimationEvent(), getInstance());
   }

   private static void registerCommands() {
      instance.getCommand("luckyblock").setExecutor(new Commands());
      instance.getCommand("luckyblock").setTabCompleter(new TabComplete());
   }

   public static String color(String text) {
      return ChatColor.translateAlternateColorCodes('&', text);
   }
}
