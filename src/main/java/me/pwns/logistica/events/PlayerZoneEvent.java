package me.pwns.logistica.events;

import me.pwns.logistica.util.zones.Zone;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;


/**
 * Event fired with a player enters or exits a Zone.
 */
public class PlayerZoneEvent extends PlayerEvent {
    private PlayerEntity player;
    private Zone zone;
    private ZoneEventType eventType;

    public PlayerZoneEvent(PlayerEntity player, Zone zone, ZoneEventType eventType) {
        super(player);
        this.player = player;
        this.zone = zone;
        this.eventType = eventType;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public Zone getZone() {
        return zone;
    }

    /**
     * @return The type of zone change for the event: Enter or Exit.
     */
    public ZoneEventType getEventType() {
        return eventType;
    }
}
