package me.pwns.logistica.events;

import me.pwns.logistica.util.zones.Zone;
import net.minecraft.entity.player.PlayerEntity;


/**
 * Event fired with a player enters a Zone.
 */
public class PlayerEnterZoneEvent extends PlayerZoneEvent {
    public PlayerEnterZoneEvent(PlayerEntity player, Zone zone) {
        super(player, zone);
    }
}
