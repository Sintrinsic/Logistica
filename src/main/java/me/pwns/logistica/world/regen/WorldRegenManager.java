package me.pwns.logistica.world.regen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO complete javadoc
 * TODO Add blockgroup joining. Currently creating a bunch of isolated groups.
 */
@Mod.EventBusSubscriber
public class WorldRegenManager {
    private static HashMap<BlockPos, BlockGroupContainer> destroyedBlockGroup = new HashMap<>();

    /**
     * Main event listener for WorldRegen.
     *
     * @param event BlockEvent.BreakEvent
     */
    @SubscribeEvent
    public static void blockDestroyedListener(BlockEvent.BreakEvent event) {
        if (event.getWorld().isRemote()) return;
        World world = event.getWorld().getWorld(); // TODO: Yes, it's weird. The first one makes it an IWorld. Issue?

        SavedBlockState block = new SavedBlockState(world, event.getState(), event.getPos());
        BlockGroupContainer neighborContainer = null;

        // TODO does it make sense to move retrieving neighbors to SavedBlockState?
        BlockPos[] neighbors = {
                event.getPos().up(),
                event.getPos().down(),
                event.getPos().north(),
                event.getPos().east(),
                event.getPos().south(),
                event.getPos().west()
        };

        // First, we check if any neighbor exists already in the hashmap.
        for (BlockPos neighbor: neighbors) {
            if (destroyedBlockGroup.containsKey(neighbor)) {
                neighborContainer = destroyedBlockGroup.get(neighbor);
                break; // If this is built right, there should be no need to check further, hence the break.
            }
        }

        // TODO find a way to make this if/else simplified. Switch possible?
        // Next, check to see if the newly changed block is in the hashmap
        if (destroyedBlockGroup.containsKey(event.getPos())) {
            // We need the container that's there regardless of what we're doing
            BlockGroupContainer blockContainer = destroyedBlockGroup.get(event.getPos());

            // Now we see if neighborContainer has been set, do the join if it has
            if (neighborContainer != null) {
                blockContainer.joinGroup(neighborContainer.getChildGroup());
                // TODO update hashmap, possibly through list > hashmap change for the neighbors
            }
            // If it hasn't been set, simply addBlock. Note, the container simply calls addBlock on child BlockGroup.
            else {
                blockContainer.addBlock(block);
            }
        }

        // If the new block isn't in the hashmap...
        else {

            // Check if the neighborContainer is null, if it isn't then add
            if (neighborContainer != null) {
                neighborContainer.addBlock(block);
                destroyedBlockGroup.put(event.getPos(), neighborContainer);
            }
            // Otherwise, block is new, none of the neighbors exist
            else {
                BlockGroupContainer newContainer = new BlockGroupContainer(block);
                destroyedBlockGroup.put(event.getPos(), newContainer);
                // TODO if we have a method to get the neighbors this should be less tedious.
                destroyedBlockGroup.put(event.getPos().up(), newContainer);
                destroyedBlockGroup.put(event.getPos().down(), newContainer);
                destroyedBlockGroup.put(event.getPos().north(), newContainer);
                destroyedBlockGroup.put(event.getPos().east(), newContainer);
                destroyedBlockGroup.put(event.getPos().south(), newContainer);
                destroyedBlockGroup.put(event.getPos().west(), newContainer);
            }
        }
    }

    private static void blockRemovedLogic(){
        // TODO move most of the logic for destroyed block listener here, duplicate listener for other modded blockstates
        // TODO ...or see if there's another event accounting for both breaking and creating?
        // TODO also, name this something not dumb if kept
    }

    public static void removeBlocks(Set<BlockPos> savedBlocks) {
        destroyedBlockGroup.keySet().removeAll(savedBlocks);
    }

}
