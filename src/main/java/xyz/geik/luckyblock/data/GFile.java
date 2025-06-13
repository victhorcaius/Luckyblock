package xyz.geik.luckyblock.data;

import de.leonhard.storage.Config;
import xyz.geik.glib.file.StorageAPI;

public class GFile {
   private static StorageAPI storage;
   private static Config messages;
   private static Config configFile;

   public static StorageAPI getStorage() {
      return storage;
   }

   public static Config getMessages() {
      return messages;
   }

   public static Config getConfig() {
      return configFile;
   }

   public GFile() {
      storage = new StorageAPI();
      configFile = storage.initConfig("config");
      messages = storage.initConfig("messages");
   }
}
