package me.pwns.logistica.world.regen;

import me.pwns.logistica.events.PlayerZoneEvent;
import me.pwns.logistica.events.ZoneEventType;
import me.pwns.logistica.util.zones.BlockGroupZone;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

/**
 * Groups together adjacent blocks by their current BlockState (see SavedBlockState, in order to  facilitate replacing a
 * specific BlockPos with the previous BlockState
 */
public class BlockGroup {
    private HashMap<BlockPos, SavedBlockState> blockStateList = new HashMap<>();
    private HashSet<BlockPos> blockNeighbors = new HashSet<>();  // TODO right collection type?
    private boolean hasBeenRestored = false;
    private float lastTouched;
    private BlockGroupZone zone;
    private World world;
    private boolean isOccupied;


    /** Constructor.
     * @param block this is a SavedBlockState that has World, BlockPos, and BlockState as arguments
     */
    public BlockGroup(SavedBlockState block) {
        addBlock(block);
        this.world = block.getWorld(); // Although other blocks are added later, shouldn't ever be jumping worlds.
        this.zone = new BlockGroupZone(blockStateList.keySet(), block.getWorld());
    }

    /**
     * @param group Another BlockGroup. Joins another BlockGroup whenever two groups interact.
     * @return "this," to be explicit which group is being kept.
     */
    public BlockGroup joinGroup(BlockGroup group) {
        // TODO probably remove this
        this.blockStateList.putAll(group.blockStateList); // Explicit "this" for clarity's sake.
        group.blockStateList.forEach((pos, savedState) -> zone.addBlock(pos));  // Add the blocks to the zone.
        return this;
    }

    /** Adds a block to the block list.
     * @param block SavedBlockState block.
     */
    public void addBlock(SavedBlockState block) {
        blockStateList.put(block.getPos(), block);
        blockNeighbors.add(block.getPos().up());
        blockNeighbors.add(block.getPos().down());
        blockNeighbors.add(block.getPos().north());
        blockNeighbors.add(block.getPos().east());
        blockNeighbors.add(block.getPos().south());
        blockNeighbors.add(block.getPos().west());
        zone.addBlock(block.getPos());
        lastTouched = world.getGameTime();

    }

    /**
     * Helper method to determine when this BlockGroup last was entered.
     * @return this.lastTouched
     */
    public float getLastTouched() {
        return this.lastTouched;
    }

    /**
     * Helper method to determine if this BlockGroup currently has a player entity within its zone.
     * @return this.isOccupied
     */
    public boolean isOccupied() {
        return this.isOccupied;
    }

    /**
     * Called whenever this BlockGroup needs to be restored to its previous state.
     */
    public void restore() {

        // First, iterate over the blockStateList, and place the block back in the world.
        for(Map.Entry<BlockPos, SavedBlockState> entry: blockStateList.entrySet()) {
            SavedBlockState savedState = entry.getValue();
            savedState.getWorld().setBlockState(savedState.getPos(), savedState.getState());
            hasBeenRestored = true; // Currently not used, included for future proofing.
        };

        // Now we need to clean up WorldRegenManager of both restored blocks and their neighbors.
        Set<BlockPos> allBlocksPos = blockStateList.keySet();
        allBlocksPos.addAll(blockNeighbors);
        WorldRegenManager.removeBlocks(allBlocksPos);
    }

    /**
     * This does the bulk of the work, listening for players leaving/entering them.
     * @param event Listens to PlayerZoneEvents for players exiting and entering zone
     */
    @SubscribeEvent
    public void zoneListener(PlayerZoneEvent event) {
        if (event.getPlayer().getEntityWorld().isRemote() || event.getZone() != zone) return;
        if (event.getEventType() == ZoneEventType.ENTERING) {
            lastTouched = world.getGameTime();
            isOccupied = true;
        }
        else if (event.getEventType() == ZoneEventType.EXITING) {
            // Whenever a player leaves a zone, WorldRegenManager will start watching and look at lastTouched
            isOccupied = false;
            // TODO: Logic for countdown to restore goes here.
        }
    }

}
