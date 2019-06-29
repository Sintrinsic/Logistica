package me.pwns.logistica.world.regen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * TODO complete javadoc
 */
@Mod.EventBusSubscriber
public class WorldRegenManager {
    private static HashMap<BlockPos, BlockGroupContainer> changedBlocks = new HashMap<>();

    /**
     * Event listener block destruction for WorldRegen.
     *
     * @param event BlockEvent.BreakEvent
     */
    @SubscribeEvent
    public static void blockDestroyedListener(BlockEvent.BreakEvent event) {
        if (event.getWorld().isRemote()) return;
        World world = event.getWorld().getWorld(); // TODO: Yes, it's weird. The first one makes it an IWorld. Issue?

        SavedBlockState destroyedBlock = new SavedBlockState(world, event.getState(), event.getPos());
        BlockGroupContainer parentContainer = null;

        BlockPos[] impactedBlocks = getImpactedBlocks(destroyedBlock);

        // Establishing single parent container
        for (BlockPos block : impactedBlocks) {
            BlockGroupContainer currentContainer = changedBlocks.get(block);

            if (currentContainer != null) {
                BlockGroup childGroup = currentContainer.getChildGroup();
                if (parentContainer == null) {
                    System.out.println("Picked up container, and parent container is null.");
                    parentContainer = currentContainer;
                }
                else if (currentContainer != parentContainer && childGroup != null) {
                    System.out.println("Current and parent containers are different; current has non-null child.");
                    parentContainer.joinGroup(childGroup);
                }
            }
        }
        // If no containers found on existing containers in previous loop, set it now
        if (parentContainer == null) {
            System.out.println("Parent is still null. Creating a new BlockGroup and adding it.");
            parentContainer = new BlockGroupContainer();
            BlockGroup destroyedBlockGroup = new BlockGroup(destroyedBlock, parentContainer);
            //parentContainer.setChildGroup(destroyedBlockGroup);

        }
        else {
            parentContainer.addBlock(destroyedBlock);
        }

        for (BlockPos impactedBlock: impactedBlocks) {
            changedBlocks.put(impactedBlock, parentContainer);
        }

    }

    public static void removeBlocks(Set<BlockPos> savedBlocks) {
        changedBlocks.keySet().removeAll(savedBlocks);
    }

    private static BlockPos[] getImpactedBlocks(SavedBlockState blockState) {
        return new BlockPos[]{
                blockState.getPos(),
                blockState.getPos().north(),
                blockState.getPos().east(),
                blockState.getPos().south(),
                blockState.getPos().west(),
                blockState.getPos().up(),
                blockState.getPos().down()
        };
    }

}
