package me.pwns.logistica.events;

import me.pwns.logistica.util.zones.Zone;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;


/**
 * Event fired with a player enters or exits a Zone.
 */
public abstract class PlayerZoneEvent extends PlayerEvent {
    private PlayerEntity player;
    private Zone zone;

    public PlayerZoneEvent(PlayerEntity player, Zone zone) {
        super(player);
        this.player = player;
        this.zone = zone;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public Zone getZone() {
        return zone;
    }
}