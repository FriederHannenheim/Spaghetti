package com.fhannenheim.spaghetti.registries;

import com.fhannenheim.spaghetti.Spaghetti;
import com.fhannenheim.spaghetti.blocks.CustomCauldron;
import com.fhannenheim.spaghetti.blocks.Tomato;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Spaghetti.MOD_ID);

    public static final RegistryObject<Block> TOMATO_CROP = BLOCKS.register("tomato",
            () -> new Tomato(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().sound(SoundType.PLANT)));

    public static final RegistryObject<Block> CAULDRON = BLOCKS.register("cauldron",()->new CustomCauldron(Block.Properties.from(Blocks.CAULDRON).tickRandomly()));
}