package me.pwns.logistica.world.regen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * TODO complete javadoc
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
        BlockPos[] neighbors = getNeighbors(block);

        // TODO does it make sense to move retrieving neighbors to SavedBlockState?
        // Get all the neighbors and stick it in a hash with the containers
        HashMap<BlockPos, BlockGroupContainer> neighborContainers = new HashMap<>();
        for(BlockPos n: neighbors) {
            neighborContainers.put(n, null);
        }

        // First, we check if any neighbor exists already in the destroyedBlockGroup.
        for (Map.Entry<BlockPos, BlockGroupContainer> entry: neighborContainers.entrySet()) {
            if (destroyedBlockGroup.containsKey(entry.getKey()) && neighborContainer == null ) {
                System.out.println("Neighbor detected");
                neighborContainer = destroyedBlockGroup.get(entry.getKey());
            }
        }
        // Have to do this after, otherwise the previous for loop will miss setting the container
        if (neighborContainer != null) {
            for (Map.Entry<BlockPos, BlockGroupContainer> entry: neighborContainers.entrySet()){
                neighborContainers.put(entry.getKey(), neighborContainer);
            }
        }

        BlockGroupContainer blockGroupContainer = null;

        // CONDITION 1: Neither destroyed blocks or its neighbors are in the destroyedblocks list (DEFAULT)
        if (!destroyedBlockGroup.containsKey(block.getPos()) && neighborContainer == null) {
            blockGroupContainer = new BlockGroupContainer(block);
            System.out.println("Block and neighbors are all new. Making new container");
        }

        // CONDITION 2: Destroyed block isn't in the list, but at least one of its neighbors is (ADDBLOCK TO NEIGHBOR)
        else if (!destroyedBlockGroup.containsKey(block.getPos()) && neighborContainer != null) {
            System.out.println("Block is new, but neighbors aren't. Adding to existing container");
            blockGroupContainer = neighborContainer;
        }
        // CONDITION 3: Destroyed block is on the list, but none of its neighbors are (ADDBLOCK TO EXISTING)
        else if (destroyedBlockGroup.containsKey(block.getPos()) && neighborContainer == null) {
            System.out.println("Block isn't new, but neighbors are. Adding to existing container.");
            blockGroupContainer = destroyedBlockGroup.get(block.getPos());
        }

        // CONDITION 4: Destroyed block is on the list, and at least one of its neighbors are (JOIN GROUPS)
        else if (destroyedBlockGroup.containsKey(block.getPos()) && neighborContainer != null) {
            System.out.println("Block exists in a container and so do neighbors. Joining groups");
            blockGroupContainer = destroyedBlockGroup.get(block.getPos());
            blockGroupContainer.joinGroup(neighborContainer.getChildGroup());

        }

        // Done outside of conditionals to reduce duplicated code.
        blockGroupContainer.addBlock(block);
        destroyedBlockGroup.put(block.getPos(), blockGroupContainer);
        for (BlockPos neighbor : neighbors) {
            destroyedBlockGroup.put(neighbor, blockGroupContainer);
        };

    }

    public static void removeBlocks(Set<BlockPos> savedBlocks) {
        destroyedBlockGroup.keySet().removeAll(savedBlocks);
    }

    private static BlockPos[] getNeighbors (SavedBlockState blockState) {
        System.out.println("Getting block neighbors");
        return new BlockPos[]{
                blockState.getPos().north(),
                blockState.getPos().east(),
                blockState.getPos().south(),
                blockState.getPos().west(),
                blockState.getPos().up(),
                blockState.getPos().down()
        };
    }

}
