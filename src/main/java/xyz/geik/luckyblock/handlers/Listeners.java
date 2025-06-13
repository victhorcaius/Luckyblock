package xyz.geik.luckyblock.handlers;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.geik.glib.utils.ColorAPI;
import xyz.geik.luckyblock.Main;
import xyz.geik.luckyblock.model.ConfigData;
import xyz.geik.luckyblock.model.Luckyblock;

public class Listeners implements Listener {
   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onBlockPlace(BlockPlaceEvent event) {
      if (!event.isCancelled()) {
         if (event.getBlock().getType().name().contains("STAINED_GLASS")) {
            Location blockLocation = event.getBlock().getLocation();
            if (event.getItemInHand() != null && event.getItemInHand().getItemMeta() != null && event.getItemInHand().getItemMeta().getDisplayName() != null) {
               String luckyKey = ConfigData.isDisplayNameExist(event.getItemInHand().getItemMeta().getDisplayName());
               if (luckyKey != null) {
                  Luckyblock luckyBlock = new Luckyblock(luckyKey, luckyKey, event.getBlock().getLocation());
                  luckyBlock.save();
                  luckyBlock.spawn();
               }
            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onBlockBreak(BlockBreakEvent event) {
      if (!event.isCancelled()) {
         Player player = event.getPlayer();
         Location blockLocation = event.getBlock().getLocation();
         Luckyblock luckyBlock;
         if (event.getBlock().getType() == Material.BEDROCK && player.isOp()) {
            luckyBlock = (Luckyblock)((List)Luckyblock.getLuckyBlocks().values().stream().filter((luckyblock) -> {
               return luckyblock.getLocation().equals(blockLocation);
            }).collect(Collectors.toList())).stream().findFirst().get();
            if (luckyBlock == null) {
               return;
            }

            Luckyblock.removeLuckyBlock(luckyBlock, event.getPlayer());
         }

         if (event.getBlock().getType().name().contains("STAINED_GLASS")) {
            try {
               luckyBlock = (Luckyblock)((List)Luckyblock.getLuckyBlocks().values().stream().filter((luckyblock) -> {
                  return luckyblock.getLocation().equals(blockLocation);
               }).collect(Collectors.toList())).stream().findFirst().get();
               if (luckyBlock == null) {
                  return;
               }

               if (luckyBlock.getHealth() < 1) {
                  return;
               }

               event.setCancelled(true);
               if (luckyBlock.getAnimation().isInAnimation()) {
                  return;
               }

               ConfigData info = (ConfigData)ConfigData.getConfigData().get(luckyBlock.getDataName());
               luckyBlock.setHealth(luckyBlock.getHealth() - 1);
               Random r = new Random();
               double randomValue = 100.0D * r.nextDouble();
               double total = 0.0D;
               Iterator var11 = info.getRewards().iterator();

               while(var11.hasNext()) {
                  String reward = (String)var11.next();
                  String[] parts = reward.split(";");
                  total += Double.parseDouble(parts[1]);
                  if (total > randomValue) {
                     String sender;
                     String[] materialParts;
                     int amount;
                     if (parts[0].equals("COMMAND")) {
                        sender = parts[2];
                        String[] commands = parts[3].split("\\|\\|");
                        materialParts = commands;
                        int var17 = commands.length;

                        for(amount = 0; amount < var17; ++amount) {
                           String command = materialParts[amount];
                           command = command.replaceAll("%player%", player.getName());
                           if (sender.equals("PLAYER")) {
                              player.performCommand(command);
                           }

                           if (sender.equals("CONSOLE")) {
                              Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                           }
                        }
                     }

                     String itemName;
                     if (parts[0].equals("ITEM")) {
                        short materialDurability = 0;
                        if (parts[2].contains(":")) {
                           materialParts = parts[2].split(":");
                           sender = materialParts[0].toUpperCase();
                           materialDurability = Short.parseShort(materialParts[1]);
                        } else {
                           sender = parts[2].toUpperCase();
                        }

                        Material material = Material.getMaterial(sender);
                        itemName = parts[3];
                        amount = Integer.parseInt(parts[4]);
                        ItemStack rewardItemStack = new ItemStack(material, amount);
                        ItemMeta rewardItemMeta = rewardItemStack.getItemMeta();
                        if (itemName.contains("&")) {
                           rewardItemMeta.setDisplayName(ColorAPI.colorize(itemName));
                        }

                        if (parts[2].contains(":")) {
                           rewardItemStack.setDurability(materialDurability);
                        }

                        rewardItemStack.setItemMeta(rewardItemMeta);
                        player.getInventory().addItem(new ItemStack[]{rewardItemStack});
                        player.sendMessage(Main.getMessageFile().getText("luckyblock.reward-item").replaceAll("%reward%", itemName.replaceAll("&.", "")));
                     }

                     if (parts[0].equals("POTION")) {
                        sender = parts[2];
                        int level = Integer.parseInt(parts[3]);
                        int duration = Integer.parseInt(parts[4]) * 20;
                        itemName = parts[5];
                        player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(sender), duration, level - 1));
                        player.sendMessage(Main.getMessageFile().getText("luckyblock.reward-potion").replaceAll("%reward%", itemName));
                     }

                     if (parts[0].equals("EXP")) {
                        int exp = Integer.parseInt(parts[2]);
                        player.giveExp(exp);
                        player.sendMessage(Main.getMessageFile().getText("luckyblock.reward-exp").replaceAll("%reward%", String.valueOf(exp)));
                     }

                     if (parts[0].equals("EMPTY")) {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 1.0F, 1.0F);
                        player.sendMessage(Main.getMessageFile().getText("luckyblock.reward-empty"));
                     }
                     break;
                  }
               }

               if (luckyBlock.getHealth() == 0) {
                  luckyBlock.killLucky();
                  return;
               }

               if (info.isKnockback() && (luckyBlock.getHealth() == info.getHealth() / 2 || luckyBlock.getHealth() == info.getHealth() / 4)) {
                  Luckyblock.createExplosion(blockLocation, Sound.ENTITY_GENERIC_EXPLODE);
               }
            } catch (Exception var21) {
               return;
            }
         }

      }
   }
}
