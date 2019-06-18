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

    public void restoreState() {
        world.setBlockState(pos, state);
    }

    public World getWorld(){
        return world;
    }

    public BlockState getState(){
        return state;
    }

    public BlockPos getPos(){
        return pos;
    }

}
