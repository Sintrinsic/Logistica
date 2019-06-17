package me.pwns.logistica.world.regen;

import net.minecraft.block.BlockState;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

/**
 * TODO complete javadoc
 */
@Mod.EventBusSubscriber
public class WorldRegenManager {

    private static HashMap<BlockState, BlockGroup> destroyedBlockGroup = new HashMap<>();
    private static HashMap<BlockGroup, Integer> destroyedBlockGroupQueue = new HashMap<>();

    /** TODO complete javadoc
     * @param event This is the block event being listened for.
     */
    @SubscribeEvent
    public static void blockDestroyedListener(BlockEvent.BreakEvent event) {
        if (event.getWorld().isRemote()) return;
        HashMap<BlockState, BlockState> blockGroupList = new HashMap<>();

        // TODO this is ugly as hell, fix it.
        BlockState brokenBlockState = event.getState();
        BlockState downBlock = event.getWorld().getBlockState(event.getPos().down());
        BlockState upBlock = event.getWorld().getBlockState(event.getPos().up());
        BlockState eastBlock = event.getWorld().getBlockState(event.getPos().east());
        BlockState westBlock = event.getWorld().getBlockState(event.getPos().west());
        BlockState northBlock = event.getWorld().getBlockState(event.getPos().north());
        BlockState southBlock = event.getWorld().getBlockState(event.getPos().south());
        blockGroupList.put(event.getWorld().getBlockState(event.getPos().down()), downBlock);
        blockGroupList.put(event.getWorld().getBlockState(event.getPos().up()), upBlock);
        blockGroupList.put(event.getWorld().getBlockState(event.getPos().east()), eastBlock);
        blockGroupList.put(event.getWorld().getBlockState(event.getPos().west()), westBlock);
        blockGroupList.put(event.getWorld().getBlockState(event.getPos().north()), northBlock);
        blockGroupList.put(event.getWorld().getBlockState(event.getPos().south()), southBlock);
        blockGroupList.put(event.getState(), brokenBlockState);


        BlockGroup group = new BlockGroup(blockGroupList);
        destroyedBlockGroupQueue.put(group, 1);

    }

    @SubscribeEvent
    public static void destroyedBlockQueueListener(TickEvent.WorldTickEvent tick) {
        if (tick.world.isRemote()) return;
        if (!destroyedBlockGroupQueue.isEmpty()) {
            System.out.println("DEBUG: Queue not empty. Doing the things.");

            for (Map.Entry<BlockGroup, Integer> entry : destroyedBlockGroupQueue.entrySet()) {
                System.out.println("For condition met");
                BlockGroup key = entry.getKey();
                int value = entry.getValue() + 1;
                if (value >= 10) {
                    System.out.println("Value of " + key + "is over 10");
                    if (checkPlayerPresent(key)) {
                        destroyedBlockGroupQueue.put(key, 1);
                    } else { cleanDestroyedBlockGroup(key); }
                }
            }

            System.out.println("New queue: " + destroyedBlockGroupQueue);
        }

    }

    private static boolean checkPlayerPresent(BlockGroup group){
        return false; // TODO update to be real
    }

    private static void cleanDestroyedBlockGroup(BlockGroup key) {
        destroyedBlockGroupQueue.remove(key);
    }

}
