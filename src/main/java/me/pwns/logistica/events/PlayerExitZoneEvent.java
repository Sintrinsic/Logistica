package me.pwns.logistica.events;

import me.pwns.logistica.util.zones.Zone;
import net.minecraft.entity.player.PlayerEntity;


/**
 * Event fired with a player exits a Zone.
 */
public class PlayerExitZoneEvent extends PlayerZoneEvent {
    public PlayerExitZoneEvent(PlayerEntity player, Zone zone) {
        super(player, zone);
    }
}
