package me.pwns.logistica.init;

import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class LogisticaBlockBreakListener {

    @SubscribeEvent
    public static void blockDestroyed(BlockEvent.BreakEvent event) {
        System.out.println("Block destroyed");
    }

}
