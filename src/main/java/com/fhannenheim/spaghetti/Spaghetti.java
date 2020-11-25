package com.fhannenheim.spaghetti;

import com.fhannenheim.spaghetti.registries.BlockRegistry;
import com.fhannenheim.spaghetti.registries.EntityRegistry;
import com.fhannenheim.spaghetti.registries.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;


@Mod("spaghetti")
public class Spaghetti
{
    // TODO: MAKE SOLUTION FOR FUCKING HECK ITEM DUPLICATION
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "spaghetti";
    public Spaghetti() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BlockRegistry.BLOCKS.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        EntityRegistry.ENTITIES.register(modEventBus);
        modEventBus.addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void clientSetup(final FMLClientSetupEvent event){
        RenderTypeLookup.setRenderLayer(BlockRegistry.TOMATO_CROP.get(), RenderType.getCutout());
    }
}
