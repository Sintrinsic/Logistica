package me.pwns.logistica.world.regen;

import me.pwns.logistica.events.PlayerEnterZoneEvent;
import me.pwns.logistica.events.PlayerExitZoneEvent;
import me.pwns.logistica.util.time.Scheduler;
import me.pwns.logistica.util.time.callbacks.BlockRegenCallback;
import me.pwns.logistica.util.zones.BlockGroupZone;
import me.pwns.logistica.util.zones.Zone;
import me.pwns.logistica.util.zones.ZoneManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Groups together adjacent blocks by their current BlockState (see SavedBlockState, in order to  facilitate replacing a
 * specific BlockPos with the previous BlockState
 */

public class BlockGroup {
    private HashMap<BlockPos, SavedBlockState> blockStateList = new HashMap<>();
    private HashSet<BlockPos> blockNeighbors = new HashSet<>();
    private int lastTouched;
    private BlockGroupZone zone;
    private World world;
    private int occupants;
    private int regenTimeout = 100;
    private BlockGroupContainer parent;
    private boolean orphan = false; // Don't want to end up attempting to join this if it's already been joined.

    /**
     * Constructor.
     *
     * @param block this is a SavedBlockState that has World, BlockPos, and BlockState as arguments

    public BlockGroup(SavedBlockState block) {
        this.world = block.getWorld(); // Although other blocks are added later, shouldn't ever be jumping worlds.
        this.zone = new BlockGroupZone(new HashSet<BlockPos>(blockStateList.keySet()),
                block.getWorld(),
                block.getState().toString());
        addBlock(block);
    }*/

    public BlockGroup(SavedBlockState block, BlockGroupContainer parent) {
        this.world = block.getWorld(); // Although other blocks are added later, shouldn't ever be jumping worlds.
        this.zone = new BlockGroupZone(new HashSet<BlockPos>(blockStateList.keySet()),
                                        block.getWorld(),
                                        block.getState().toString());
        System.out.println(zone.getName());
        this.parent = parent;
        this.parent.setChildGroup(this);
        addBlock(block);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void setParent(BlockGroupContainer parent) {
        this.parent = parent;
    }

    public BlockGroupContainer getParent() {
        return parent;
    }

    /**
     * @param group Another BlockGroup. Joins another BlockGroup whenever two groups interact.
     * @return "this," to be explicit which group is being kept.
     */
    public BlockGroup joinGroup(BlockGroup group) {
        this.blockStateList.putAll(group.blockStateList); // Explicit "this" for clarity's sake.
        this.blockNeighbors.addAll(group.blockNeighbors);
        this.blockStateList.forEach((pos, stat) -> this.zone.addBlock(pos));
        // TODO change occupants to playerlist rather than count
        group.getParent().setChildGroup(this);
        group.tearDown();
        touch();
        return this;
    }

    public void touch() {
        lastTouched = ((int) world.getGameTime());
        // Only scheduling the regen if nobody is inside zone. This will prevent huge mining operations from creating
        //thousands of events
        if (occupants == 0) {
            Scheduler.createTimer(new BlockRegenCallback(this.parent), 11);
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

    public Zone getZone() {return this.zone;}

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
        if (this.isOrphan()) return;
        int regentime = lastTouched + regenTimeout;
        int worldTime = (int) world.getGameTime();
        if (occupants > 0 || worldTime < regentime) {
            return;
        }
        // First, iterate over the blockStateList, and place the block back in the world.
        blockStateList.forEach((pos, state) -> state.restoreState());

        // Now we need to clean up WorldRegenManager of both restored blocks and their neighbors.
        Set<BlockPos> allBlockPos = new HashSet<>();
        allBlockPos.addAll(blockStateList.keySet());
        allBlockPos.addAll(blockNeighbors);
        WorldRegenManager.removeBlocks(allBlockPos);
        tearDown();
    }

    /**
     * Teardown method invoked both from group join and from
     */
    public void tearDown() {
        this.setOrphan(true);
        ZoneManager.removeZone(this.zone);
        if (parent.getChildGroup() == this) {
            this.parent.removeChildGroup();
        }
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void enterZoneListener(PlayerEnterZoneEvent event) {
        System.out.println("enterZoneListener firing");
        if (this.isOrphan()) return;
        if (event.getPlayer().getEntityWorld().isRemote() || event.getZone() != zone) return;
        this.touch();
        occupants++;
        System.out.println("enterZoneListener completed");
    }

    @SubscribeEvent
    public void exitZoneListener(PlayerExitZoneEvent event) {
        System.out.println("exitZoneListener firing");
        if (event.getPlayer().getEntityWorld().isRemote() || event.getZone() != zone) return;
        occupants--;
        this.touch();
        System.out.println("exitZoneListener completed");
    }

    public boolean isOrphan() {
        return orphan;
    }

    public void setOrphan(boolean orphan) {
        this.orphan = orphan;
    }

}
