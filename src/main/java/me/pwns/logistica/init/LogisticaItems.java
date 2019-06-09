package me.pwns.logistica.init;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import me.pwns.logistica.Constants;

import java.lang.ref.Reference;

@Mod.EventBusSubscriber(modid = Constants.modID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LogisticaItems {
    public static final Item whale_semen = null;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(

                new Item(new Item.Properties()).setRegistryName(Constants.modID, "whale_semen")

        );
    }
}
