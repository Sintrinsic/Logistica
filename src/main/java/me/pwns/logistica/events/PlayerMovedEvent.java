package me.pwns.logistica.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * Event fired when a player moves >= one block width of distance in any direction.
 * Does not necessarily correspond to standing in the area of a new block, so a player running diagonally would
 * trigger this event far more frequently than if recording the number of blocks they've touched.
 * A player wiggling around in a < 1 block area will not trigger this event.
 */
public class PlayerMovedEvent extends PlayerEvent {
    private Vec3d fromPos;
    private Vec3d toPos;

    public PlayerMovedEvent(PlayerEntity player, Vec3d from, Vec3d to) {
        super(player);
        fromPos = from;
        toPos = to;
    }

    /**
     * Get the last saved position of the player. Corresponds to the ToPos property of the last time this event fired.
     */
    public Vec3d getFromPos() {
        return fromPos;
    }

    /**
     * Get the current location of the player.
     */
    public Vec3d getToPos() {
        return toPos;
    }
}
