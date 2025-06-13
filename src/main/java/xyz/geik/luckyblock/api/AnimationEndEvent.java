package xyz.geik.luckyblock.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import xyz.geik.luckyblock.model.Luckyblock;

public class AnimationEndEvent extends Event {
   private Luckyblock luckyblock;
   private static final HandlerList HANDLERS = new HandlerList();

   public AnimationEndEvent(Luckyblock luckyblock) {
      this.luckyblock = luckyblock;
   }

   public HandlerList getHandlers() {
      return HANDLERS;
   }

   public static HandlerList getHandlerList() {
      return HANDLERS;
   }

   public Luckyblock getLuckyblock() {
      return this.luckyblock;
   }
}
