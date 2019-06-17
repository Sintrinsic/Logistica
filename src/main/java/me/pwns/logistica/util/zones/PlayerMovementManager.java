package me.pwns.logistica.util.zones;

import me.pwns.logistica.events.PlayerMovedEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.vecmath.Vector3d;
import java.util.HashMap;

@Mod.EventBusSubscriber
public class PlayerMovementManager {
    private static HashMap<PlayerEntity, Vector3d> playerLastLocations = new HashMap<>();
    private static int tick = 0;

    @SubscribeEvent
    public static void onUpdate(TickEvent.PlayerTickEvent event) {
        if (event.player.world.isRemote) {
            return;
        }
        if (tick != 20) {
            tick++;
            return;
        }
        tick = 0;
        Vector3d currentLocation = new Vector3d(event.player.posX,
                event.player.posY + event.player.getYOffset(),
                event.player.posZ);


        if (!playerLastLocations.containsKey(event.player)) {
            playerLastLocations.put(event.player, currentLocation);
        }
        playerLastLocations.get(event.player);

        Vector3d difference = new Vector3d(currentLocation);
        difference.sub(playerLastLocations.get(event.player));
        if (difference.length() >= 1) {
            PlayerMovedEvent movedEvent = new PlayerMovedEvent(event.player, playerLastLocations.get(event.player), currentLocation);
            MinecraftForge.EVENT_BUS.post(movedEvent);
            System.out.println("Player moved. Fired event.");

            playerLastLocations.put(event.player, currentLocation);
        }
    }
}
