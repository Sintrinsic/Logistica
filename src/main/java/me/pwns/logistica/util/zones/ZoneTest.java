package me.pwns.logistica.util.zones;


import me.pwns.logistica.events.PlayerZoneEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.vecmath.Vector3d;

@Mod.EventBusSubscriber
public class ZoneTest {

    @SubscribeEvent
    public static void onDestroy(BreakEvent event) {
        World world = event.getPlayer().world;
        if (world.isRemote) {
            return;
        }

        System.out.println("Received destroyblockevent.");

        BlockPos position = event.getPos();

        Vector3d start = new Vector3d(position.getX(), position.getY() - 50, position.getZ());
        Vector3d end = new Vector3d(start.x - 10, start.y + 50, start.z - 10);
        Zone testZone = new CuboidZone(start, end, world);
        ZoneManager.addZone(testZone);
    }

    @SubscribeEvent
    public static void onZoneChange(PlayerZoneEvent event) {
        if (event.getEntityLiving().world.isRemote) {
            return;
        }

        String name = event.getPlayer().getName().getString();
        String testString = "WOW! Player " + name + " has entered the zone.";
        System.out.println(testString);
    }


}
