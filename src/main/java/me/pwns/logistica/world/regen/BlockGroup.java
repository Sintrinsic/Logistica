package me.pwns.logistica.world.regen;

import me.pwns.logistica.events.PlayerEnterZoneEvent;
import me.pwns.logistica.events.PlayerExitZoneEvent;
import me.pwns.logistica.util.time.Scheduler;
import me.pwns.logistica.util.time.callbacks.BlockRegenCallback;
import me.pwns.logistica.util.zones.BlockGroupZone;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Groups together adjacent blocks by their current BlockState (see SavedBlockState, in order to  facilitate replacing a
 * specific BlockPos with the previous BlockState
 */
public class BlockGroup {
    private HashMap<BlockPos, SavedBlockState> blockStateList = new HashMap<>();
    private HashSet<BlockPos> blockNeighbors = new HashSet<>();  // TODO right collection type?
    private boolean hasBeenRestored = false;
    private int lastTouched;
    private BlockGroupZone zone;
    private World world;
    private int occupants;
    private int regenTimeout = 100;
    private BlockGroupContainer parent;

    /**
     * Constructor.
     *
     * @param block this is a SavedBlockState that has World, BlockPos, and BlockState as arguments
     */
    public BlockGroup(SavedBlockState block, BlockGroupContainer parent) {
        this.parent = parent;
        this.world = block.getWorld(); // Although other blocks are added later, shouldn't ever be jumping worlds.
        this.zone = new BlockGroupZone(new HashSet<BlockPos>(blockStateList.keySet()),
                block.getWorld(),
                block.getState().toString());
        addBlock(block);
    }

    /**
     * @param group Another BlockGroup. Joins another BlockGroup whenever two groups interact.
     * @return "this," to be explicit which group is being kept.
     */
    public BlockGroup joinGroup(BlockGroup group) {
        // TODO possibly move logic here to parent
        this.blockStateList.putAll(group.blockStateList); // Explicit "this" for clarity's sake.
        this.blockNeighbors.addAll(group.blockNeighbors);
        // TODO adding occupants? Any other things needing to be joined?
        group.blockStateList.forEach((pos, savedState) -> zone.addBlock(pos));  // Add the blocks to the zone.
        return this;
    }

    private void touch() {
        lastTouched = ((int) world.getGameTime());
        // Only scheduling the regen if nobody is inside zone. This will prevent huge mining operations from creating
        //thousands of events
        if (occupants == 0) {
            Scheduler.createTimer(new BlockRegenCallback(this), 11);
        }
    }

    /**
     * Adds a block to the block list.
     *
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
        touch();
    }

    /**
     * Helper method to determine when this BlockGroup last was entered.
     *
     * @return this.lastTouched
     */
    public float getLastTouched() {
        return this.lastTouched;
    }

    /**
     * Helper method to determine if this BlockGroup currently has a player entity within its zone.
     *
     * @return this.isOccupied
     */
    public boolean isOccupied() {
        return occupants > 0;
    }

    /**
     * Called whenever this BlockGroup needs to be restored to its previous state.
     * TODO: Figure out why the scheduler is firing at about twice the speed it should.
     */
    public void restore() {
        // preventing regen if the timeout hasn't been hit. Will happen often with scheduler.
        int regentime = lastTouched + regenTimeout;
        int worldTime = (int) world.getGameTime();
        if (occupants > 0 || worldTime < regentime) {
            return;
        }
        // First, iterate over the blockStateList, and place the block back in the world.
        blockStateList.forEach((pos, state) -> state.restoreState());
        hasBeenRestored = true; // Currently not used, included for future proofing.

        // Now we need to clean up WorldRegenManager of both restored blocks and their neighbors.
        Set<BlockPos> allBlockPos = new HashSet<>();
        allBlockPos.addAll(blockStateList.keySet());
        allBlockPos.addAll(blockNeighbors);

        WorldRegenManager.removeBlocks(allBlockPos);
    }

    @SubscribeEvent
    public void enterZoneListener(PlayerEnterZoneEvent event) {
        if (event.getPlayer().getEntityWorld().isRemote() || event.getZone() != zone) return;
            this.touch();
            occupants++;
    }

    @SubscribeEvent
    public void exitZoneListener(PlayerExitZoneEvent event) {
        if (event.getPlayer().getEntityWorld().isRemote() || event.getZone() != zone) return;
        occupants--;
        this.touch();

    }
}
