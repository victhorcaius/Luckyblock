package xyz.geik.luckyblock.commands;

import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import xyz.geik.glib.utils.ColorAPI;
import xyz.geik.luckyblock.Main;
import xyz.geik.luckyblock.model.ConfigData;
import xyz.geik.luckyblock.model.Luckyblock;

public class Commands implements CommandExecutor {
   public boolean onCommand(@NotNull CommandSender sender, Command cmd, String label, String[] args) {
      if (!sender.hasPermission("luckyblock.admin")) {
         sender.sendMessage(Main.getMessageFile().getText("no-perm"));
         return true;
      } else if (args.length == 0) {
         this.sendWrongUsageMsg(sender);
         return false;
      } else {
         if (args.length == 4 && args[0].equalsIgnoreCase("give")) {
            if (!Bukkit.getOfflinePlayer(args[1]).isOnline()) {
               sender.sendMessage(Main.getMessageFile().getText("luckyblock.player-offline"));
               return true;
            }

            boolean var12 = false;

            int amount;
            try {
               amount = Integer.parseInt(args[3]);
               if (amount <= 0) {
                  throw new NumberFormatException();
               }
            } catch (NumberFormatException var11) {
               sender.sendMessage(Main.getMessageFile().getText("luckyblock.number-format-wrong"));
               return true;
            }

            Player player = Bukkit.getPlayer(args[1]);
            ConfigData data = (ConfigData)ConfigData.getConfigData().get(args[2]);
            if (data == null) {
               this.noLuckyBlockMsg(sender);
            }

            String name = ColorAPI.colorize(data.getDisplayName());
            ItemStack item = new ItemStack(Material.getMaterial(Main.getConfigFile().getString("blockColor") + "_STAINED_GLASS"), amount);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            meta.setLore(data.getLore());
            item.setItemMeta(meta);
            player.getInventory().addItem(new ItemStack[]{item});
            player.sendMessage(Main.getMessageFile().getText("luckyblock.taken").replaceAll("%luckyblock%", name));
            sender.sendMessage(Main.getMessageFile().getText("luckyblock.given").replaceAll("%player%", player.getName()).replaceAll("%luckyblock%", name));
         } else if (args[0].equalsIgnoreCase("start") && args.length == 2) {
            if (Bukkit.getOnlinePlayers().size() < Main.getLuckyblockFile().getInt("minOnlineForEvents")) {
               Bukkit.broadcastMessage(Main.getMessageFile().getText("luckyblock.no-enough-player"));
               return true;
            }

            Luckyblock luckyblock = (Luckyblock)Luckyblock.getLuckyBlocks().get(args[1]);
            System.out.println(Luckyblock.getLuckyBlocks());
            if (luckyblock == null) {
               return this.noLuckyBlockMsg(sender);
            }

            luckyblock.spawn();
         } else if (args[0].equalsIgnoreCase("reload") && args.length == 1) {
            if (!((List)Luckyblock.getLuckyBlocks().values().stream().filter((lb) -> {
               return lb.getAnimation().isInAnimation();
            }).collect(Collectors.toList())).isEmpty()) {
               sender.sendMessage(ColorAPI.colorize("&cIs there a luckyblock on animation!"));
               return false;
            }

            ConfigData.getConfigData().clear();
            ConfigData.loadAllConfigData();
            sender.sendMessage(Main.getMessageFile().getText("luckyblock.reloaded"));
         } else {
            this.sendWrongUsageMsg(sender);
         }

         return false;
      }
   }

   private boolean sendWrongUsageMsg(@NotNull CommandSender sender) {
      Main.getMessageFile().getTextList("luckyblock.command-list").forEach((text) -> {
         sender.sendMessage(text);
      });
      return true;
   }

   private boolean noLuckyBlockMsg(@NotNull CommandSender sender) {
      sender.sendMessage(Main.getMessageFile().getText("luckyblock.no-luckyblock"));
      return true;
   }
}
