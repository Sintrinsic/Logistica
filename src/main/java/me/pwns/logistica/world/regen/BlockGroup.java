package me.pwns.logistica.world.regen;

import me.pwns.logistica.events.PlayerZoneEvent;
import me.pwns.logistica.events.ZoneEventType;
import me.pwns.logistica.util.zones.BlockGroupZone;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Groups together adjacent blocks by their current BlockState (see SavedBlockState, in order to  facilitate replacing a
 * specific BlockPos with the previous BlockState
 */
public class BlockGroup {
    private HashMap<BlockPos, SavedBlockState> blockStateList = new HashMap<>();
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
     * @param group Another Blockgroup. Joins another blockgroup whenever two groups interact.
     * @return "this," to be explicit which group is being kept.
     */
    public BlockGroup joinGroup(BlockGroup group) {
        this.blockStateList.putAll(group.blockStateList); // Explicit "this" for clarity's sake.
        group.blockStateList.forEach((pos, savedState) -> zone.addBlock(pos));  // Add the blocks to the zone.
        return this;
    }

    /** Adds a block to the block list.
     * @param block SavedBlockState block.
     */
    private void addBlock(SavedBlockState block) {
        blockStateList.put(block.getPos(), block);
        zone.addBlock(block.getPos());
    }

    /**
     * Called whenever the blockgroup needs to be restored to its previous state.
     */
    public void restore() {
        for(Map.Entry<BlockPos, SavedBlockState> entry: blockStateList.entrySet()) {
            SavedBlockState savedState = entry.getValue();
            savedState.getWorld().setBlockState(savedState.getPos(), savedState.getState());
            hasBeenRestored = true; // Currently not used, included for future proofing.
        };
    }

    /**
     * @param event Listens to PlayerZoneEvents for players exiting and entering zone
     */
    @SubscribeEvent
    public void zoneListener(PlayerZoneEvent event) {
        if (event.getPlayer().getEntityWorld().isRemote()) return;
        if (event.getZone() != zone) return;
        if (event.getEventType() == ZoneEventType.ENTERING) {
            lastTouched = world.getGameTime();
            isOccupied = true;
        }
        else if (event.getEventType() == ZoneEventType.EXITING) {
            // Whenever a player leaves a zone, WorldRegenManager will start watching and look at lastTouched
            isOccupied = false;
        }
    }


}
