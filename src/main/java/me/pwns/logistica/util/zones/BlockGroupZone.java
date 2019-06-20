package me.pwns.logistica.util.zones;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

/**
 * A zone defined by a set of blocks.
 */
public class BlockGroupZone extends Zone {
    private Set<BlockPos> blockList;

    public BlockGroupZone(Set<BlockPos> blockSet, World world, String name) {
        this.world = world;
        this.name = name;
        blockList = blockSet;
        players = new HashSet<>();
    }

    public boolean isInside(Vec3d position, World world) {
        if (world != this.world) {
            return false;
        }
        BlockPos targetBlock = new BlockPos(position);
        return blockList.contains(targetBlock);
    }

    public boolean addBlock(BlockPos block) {
        return blockList.add(block);
    }

    public boolean removeBlock(BlockPos block) {
        return blockList.remove(block);
    }
}
