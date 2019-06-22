package me.pwns.logistica.util.time;

import me.pwns.logistica.util.time.callbacks.CallbackContainer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

@Mod.EventBusSubscriber
public class Scheduler {
    private static int tickCount = 0;
    private static SortedMap<Integer, ArrayList<CallbackContainer>> schedule = new TreeMap<>();

    /**
     * Create a timer which will call your callback in n number of seconds (20 server ticks).
     *
     * @param callback An object implementing CallbackContainer, will do whatever you want when doCallback is called.
     * @param seconds  The number of seconds in the future you'd like this callback to be called.
     */
    public static void createTimer(CallbackContainer callback, int seconds) {
        int futureTick = tickCount + (seconds * 20);
        if (!schedule.containsKey(futureTick)) {
            schedule.put(futureTick, new ArrayList<>());
        }
        schedule.get(futureTick).add(callback);
    }


    /**
     * Forge-called event listner. Don't call this.
     * Counts server ticks, and once per second, looks for scheduled callbacks that are before now, and calls them.
     * TODO: Ensure that working off of servertick won't somehow cause callbacks to fire into unloaded worlds.
     *
     * @param event Forge-provided ServerTickEvent.
     */
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.side == LogicalSide.CLIENT) {
            return;
        }
        tickCount++;
        if (tickCount % 20 == 0) {
            // headMap returns all the schedule items that are less than (older than) the current tickCount, so we can
            // do all their callbacks, and delete them.
            SortedMap<Integer, ArrayList<CallbackContainer>> elapsedItems = schedule.headMap(tickCount);
            for (ArrayList<CallbackContainer> item : elapsedItems.values()) {
                for (CallbackContainer callback : item) {
                    callback.doCallback();
                }
            }
            //elapsedItems is a view that is backed by the original map, so clearing this deletes those schedule items.
            elapsedItems.clear();

        }
    }

}
