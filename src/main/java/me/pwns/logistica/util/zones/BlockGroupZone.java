package me.pwns.logistica.util.zones;

import net.minecraft.block.BlockState;
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
    private Set<BlockState> blockList;
    private Set<PlayerEntity> players;
    private World world;

    public BlockGroupZone(Set<BlockState> blockSet, World world) {
        this.world = world;
        blockList = blockSet;
        players = new HashSet<>();
    }

    /**
     * @param position The position in the world that you wish to check for the presence of a zone.
     * @param world    The world that corresponds to the above position.
     * @return Whether or not any part of the player's body is within one of the blocks in this zone.
     */
    @Override
    public boolean isInside(Vector3d position, World world) {
        if (world != this.world) {
            return false;
        }
        BlockPos playerFeet = new BlockPos(position.x, position.y, position.z);
        BlockPos playerHead = playerFeet.add(0, 1, 0);
        BlockState feetBlock = world.getBlockState(playerFeet);
        BlockState headBlock = world.getBlockState(playerHead);
        return blockList.contains(feetBlock) || blockList.contains(headBlock);
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
