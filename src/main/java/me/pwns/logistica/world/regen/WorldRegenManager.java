package me.pwns.logistica.world.regen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

/**
 * TODO complete javadoc
 */
@Mod.EventBusSubscriber
public class WorldRegenManager {
    private static HashMap<BlockPos, BlockGroup> destroyedBlockGroup = new HashMap<>();

    @SubscribeEvent
    public static void blockDestroyedListener(BlockEvent.BreakEvent event) {
        if (event.getWorld().isRemote()) return;
        World world = event.getWorld().getWorld(); // TODO: Yes, it's weird. The first one makes it an IWorld. Issue?

        SavedBlockState block = new SavedBlockState(world, event.getState(), event.getPos());
        BlockGroup group = new BlockGroup(block);
        destroyedBlockGroup.put(event.getPos(), group);
        destroyedBlockGroup.put(event.getPos().up(), group);
        destroyedBlockGroup.put(event.getPos().down(), group);
        destroyedBlockGroup.put(event.getPos().north(), group);
        destroyedBlockGroup.put(event.getPos().east(), group);
        destroyedBlockGroup.put(event.getPos().south(), group);
        destroyedBlockGroup.put(event.getPos().west(), group);


    }

    public static void removeBlocks(Set<BlockPos> savedBlocks){
        destroyedBlockGroup.keySet().removeAll(savedBlocks);
    }

}
