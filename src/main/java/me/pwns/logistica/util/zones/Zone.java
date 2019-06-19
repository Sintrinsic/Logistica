package me.pwns.logistica.util.zones;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import javax.vecmath.Vector3d;
import java.util.Set;

/**
 * Base class for zones, which define arbitrary areas within the world that track player occupancy and potentially
 * other properties.
 */
public interface Zone {
    boolean isInside(Vector3d position, World world);

    Set<PlayerEntity> getPlayers();

    boolean addPlayer(PlayerEntity player);

    boolean removePlayer(PlayerEntity player);

    String getName();

}
