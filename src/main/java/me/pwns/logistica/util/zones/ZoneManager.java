package me.pwns.logistica.util.zones;


import me.pwns.logistica.events.PlayerEnterZoneEvent;
import me.pwns.logistica.events.PlayerExitZoneEvent;
import me.pwns.logistica.events.PlayerMovedEvent;
import me.pwns.logistica.events.PlayerZoneEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

/**
 * Listens to player movement events, and emits new events for when a player enters or exits any type of zone.
 * Todo: Verify that zones are testing isInside properly. Rough tests seem to have an inconsistent edge.
 */
@Mod.EventBusSubscriber
public class ZoneManager {
    private static ArrayList<Zone> zones = new ArrayList<>();


    @SubscribeEvent
    public static void playerMoved(PlayerMovedEvent event) {
        if (event.getEntityPlayer().world.isRemote()) {
            return;
        }
        World world = event.getEntityPlayer().world;
        for (Zone zone : zones) {
            boolean fromPosInside = zone.isInside(event.getFromPos(), world);
            boolean toPosInside = zone.isInside(event.getToPos(), world);
            // Need to capture both transitions into the zone, or movement in a zone if the player's not already marked
            // as entered (as would be the case if the zone popped up around them).
            if (toPosInside && (!fromPosInside || !zone.getPlayers().contains(event.getEntityPlayer()))) {
                zone.addPlayer(event.getEntityPlayer());
                PlayerZoneEvent zoneEvent = new PlayerEnterZoneEvent(event.getEntityPlayer(), zone);
                MinecraftForge.EVENT_BUS.post(zoneEvent);

            } else if (fromPosInside && !toPosInside) {
                zone.removePlayer(event.getEntityPlayer());
                PlayerZoneEvent zoneEvent = new PlayerExitZoneEvent(event.getEntityPlayer(), zone);
                MinecraftForge.EVENT_BUS.post(zoneEvent);
            }
        }

    }

    public static void addZone(Zone zone) {
        zones.add(zone);
    }

    public static void removeZone(Zone zone) {
        zones.remove(zone);
    }

}
