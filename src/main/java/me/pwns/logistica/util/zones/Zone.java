package me.pwns.logistica.util.zones;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Set;

/**
 * All the fun mandatory bits that make up a zone.
 */
public abstract class Zone {
    protected Set<PlayerEntity> players;
    protected World world;
    protected String name;

    /**
     * @param position The 3d position in the world to check.
     * @param world    The world that corresponds to the above position.
     * @return True if the position is inside the zone. False if not.
     */
    public abstract boolean isInside(Vec3d position, World world);

    /**
     * @return A list of players currently within this zone.
     */
    public Set<PlayerEntity> getPlayers() {
        return players;
    }

    public boolean addPlayer(PlayerEntity player) {
        return players.add(player);
    }

    public boolean removePlayer(PlayerEntity player) {
        return players.remove(player);
    }

    public String getName() {
        return name;
    }


}
