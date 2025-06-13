package xyz.geik.luckyblock.commands;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import xyz.geik.luckyblock.Main;

public class TabComplete implements TabCompleter {
   public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
      if (sender instanceof Player) {
         Player player = (Player)sender;
         if (!player.hasPermission("luckyblock.admin")) {
            return Arrays.asList("");
         }
      }

      if (args.length == 1) {
         return Arrays.asList("give", "start", "reload");
      } else {
         if (args.length == 2) {
            if (args[0].equals("give")) {
               return (List)Bukkit.getOnlinePlayers().stream().map((target) -> {
                  return target.getName();
               }).collect(Collectors.toList());
            }

            if (args[0].equals("start")) {
               return (List)Main.getLuckyDataFile().singleLayerKeySet().stream().collect(Collectors.toList());
            }
         } else {
            if (args.length == 3 && args[0].equals("give")) {
               return (List)Main.getLuckyblockFile().singleLayerKeySet("lucky-blocks").stream().collect(Collectors.toList());
            }

            if (args.length == 4 && args[0].equals("give")) {
               return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8");
            }
         }

         return Arrays.asList("");
      }
   }
}
