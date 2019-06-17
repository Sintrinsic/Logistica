package me.pwns.logistica.world.regen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
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

    @SubscribeEvent
    public static void blockDestroyedListener(BlockEvent.BreakEvent event) {
        if (event.getWorld().isRemote()) return;

    }

}
