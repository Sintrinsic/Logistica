package me.pwns.logistica.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;

import javax.vecmath.Vector3d;

/**
 * Event fired when a player moves >= one block width of distance in any direction.
 * Does not necessarily correspond to standing in the area of a new block, so a player running diagonally would
 * trigger this event far more frequently than if recording the number of blocks they've touched.
 * A player wiggling around in a < 1 block area will not trigger this event.
 */
public class PlayerMovedEvent extends PlayerEvent {
    private Vector3d fromPos;
    private Vector3d toPos;
    private long distance;

    public PlayerMovedEvent(PlayerEntity player, Vector3d from, Vector3d to) {
        super(player);
        fromPos = from;
        toPos = to;
    }

    /**
     * Get the last saved position of the player. Corresponds to the ToPos property of the last time this event fired.
     */
    public Vector3d getFromPos() {
        return fromPos;
    }

    /**
     * Get the current location of the player.
     */
    public Vector3d getToPos() {
        return toPos;
    }

    public long getDistance() {
        return distance;
    }
}
