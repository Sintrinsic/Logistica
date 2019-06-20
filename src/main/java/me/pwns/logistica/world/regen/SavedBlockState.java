package me.pwns.logistica.world.regen;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SavedBlockState {

    private World world;
    private BlockState state;
    private BlockPos pos;

    public SavedBlockState(World world, BlockState state, BlockPos pos) {
        this.world = world;
        this.state = state;
        this.pos = pos;
    }

    /**
     * This is used to restore this saved block state to the world.
     */
    public void restoreState() {
        world.setBlockState(pos, state);
    }

    /**
     * Helper method to get the world this block was in.
     *
     * @return World
     */
    public World getWorld() {
        return world;
    }

    /**
     * Helper method to get the actual state.
     *
     * @return BlockState
     */
    public BlockState getState() {
        return state;
    }

    /**
     * Helper method to get the block position
     *
     * @return BlockPos
     */
    public BlockPos getPos() {
        return pos;
    }

}
