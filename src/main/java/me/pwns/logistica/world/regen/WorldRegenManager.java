package me.pwns.logistica.world.regen;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 * TODO complete javadoc
 */
@Mod.EventBusSubscriber
public class WorldRegenManager {

    /** TODO complete javadoc
     * @param event This is the block event being listened for.
     */
    @SubscribeEvent
    public static void blockDestroyedListener(BlockEvent.BreakEvent event) {

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
        System.out.println(group);

    }

    // @SubscribeEvent TODO uncomment when ready
    public static void checkDestroyedQueue(TickEvent.WorldTickEvent tick) {

    }

    private static void cleanDestroyedBlockGroup(BlockState state, String string) {

    }

}
