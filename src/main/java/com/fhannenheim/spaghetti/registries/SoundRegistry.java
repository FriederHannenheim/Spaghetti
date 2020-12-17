package com.fhannenheim.spaghetti.registries;

import com.fhannenheim.spaghetti.Spaghetti;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS = new DeferredRegister<>(ForgeRegistries.SOUND_EVENTS, Spaghetti.MOD_ID);

    public static final RegistryObject<SoundEvent> SLURP = SOUNDS.register("entity.player.slurp", ()->new SoundEvent(new ResourceLocation(Spaghetti.MOD_ID,"entity.player.slurp")));

    public static final RegistryObject<SoundEvent> SPLASH = SOUNDS.register("entity.spaghetti.splash", ()->new SoundEvent(new ResourceLocation(Spaghetti.MOD_ID,"entity.spaghetti.splash")));
}
