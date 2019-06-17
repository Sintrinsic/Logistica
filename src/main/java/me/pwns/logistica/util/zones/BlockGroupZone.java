package me.pwns.logistica.util.zones;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.vecmath.Vector3d;
import java.util.HashSet;
import java.util.Set;

/**
 * A zone defined by a set of blocks.
 */
public class BlockGroupZone implements Zone {
    private Set<BlockPos> blockList;
    private Set<PlayerEntity> players;
    private World world;

    public BlockGroupZone(Set<BlockPos> blockSet, World world) {
        this.world = world;
        blockList = blockSet;
        players = new HashSet<>();
    }

    /**
     * @param position The position in the world that you wish to check for the presence of a zone.
     * @param world    The world that corresponds to the above position.
     * @return True if the position is inside the blockset. False if not.
     */
    @Override
    public boolean isInside(Vector3d position, World world) {
        if (world != this.world) {
            return false;
        }
        BlockPos targetBlock = new BlockPos(position.x, position.y, position.z);


        return blockList.contains(targetBlock);
    }

    /**
     * @return A list of players currently within this zone.
     */
    @Override
    public Set<PlayerEntity> getPlayers() {
        return players;
    }

    @Override
    public boolean addPlayer(PlayerEntity player) {
        if (players.contains(player)) {
            return false;
        }
        players.add(player);
        return true;
    }

    @Override
    public boolean removePlayer(PlayerEntity player) {
        if (!players.contains(player)) {
            return false;
        }
        players.remove(player);
        return true;
    }


}
