package me.pwns.logistica.world.regen;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class BlockGroup {
    private HashMap<BlockPos, SavedBlockState> blockStateList = new HashMap<>();
    private boolean hasBeenRestored = false;
    private int lastTouched;

    public BlockGroup(HashMap<BlockPos, SavedBlockState> blockArray) {
        this.blockStateList.putAll(blockArray);
    }

    public BlockGroup(SavedBlockState block) {
        addBlock(block);
    }

    public BlockGroup joinGroup(BlockGroup group) {
        this.blockStateList.putAll(group.blockStateList);
        return this;
    }

    private void addBlock(SavedBlockState block) {
        blockStateList.put(block.getPos(), block);
    }

    public void restore() {
        for(Map.Entry<BlockPos, SavedBlockState> entry: blockStateList.entrySet()) {
            SavedBlockState savedState = entry.getValue();
            savedState.getWorld().setBlockState(savedState.getPos(), savedState.getState());
            hasBeenRestored = true;
        };
    }

}
