package me.pwns.logistica.util.zones;

import me.pwns.logistica.events.PlayerMovedEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.HashMap;

@Mod.EventBusSubscriber
public class PlayerMovementManager {
    private static HashMap<String, Vec3d> playerLastLocations = new HashMap<>();
    private static HashMap<String, MutableInt> playerTicks = new HashMap<>();

    @SubscribeEvent
    public static void onUpdate(TickEvent.PlayerTickEvent event) {
        if (event.player.world.isRemote) {
            return;
        }
        String playerName = event.player.getName().toString();

        // Increment player's timer and proceed once per second (20 tics)
        playerTicks.putIfAbsent(playerName, new MutableInt(0));
        MutableInt playerTick = playerTicks.get(playerName);
        if (playerTick.toInteger() != 20) {
            playerTick.increment();
            return;
        }
        playerTick.setValue(0);

        Vec3d currentLocation = new Vec3d(event.player.posX,
                event.player.posY + event.player.getYOffset(),
                event.player.posZ);

        playerLastLocations.putIfAbsent(playerName, currentLocation);
        playerLastLocations.get(playerName);

        Vec3d movementVector = currentLocation.subtract(playerLastLocations.get(playerName));
        if (movementVector.length() >= 1) {
            PlayerMovedEvent movedEvent = new PlayerMovedEvent(event.player,
                    playerLastLocations.get(playerName),
                    currentLocation);
            MinecraftForge.EVENT_BUS.post(movedEvent);

            playerLastLocations.put(playerName, currentLocation);
        }
    }
}
