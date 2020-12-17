package com.fhannenheim.spaghetti.registries;

import com.fhannenheim.spaghetti.Spaghetti;
import com.fhannenheim.spaghetti.items.SpaghettiItem;
import com.fhannenheim.spaghetti.items.UncookedSpaghetti;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Spaghetti.MOD_ID);
    public static final RegistryObject<Item> TOMATO = ITEMS.register("tomato",() -> new BlockItem(BlockRegistry.TOMATO_CROP.get(), new Item.Properties().food(new Food.Builder().hunger(3).saturation(2).build()).group(ItemGroup.FOOD)));
    public static final RegistryObject<Item> UNCOOKED_SPAGHETTI = ITEMS.register("uncooked_spaghetti",() -> new UncookedSpaghetti(new Item.Properties().group(ItemGroup.FOOD)));
    public static final RegistryObject<Item> SPAGHETTI = ITEMS.register("spaghetti",() -> new SpaghettiItem(new Item.Properties().group(ItemGroup.FOOD)));
    public static final RegistryObject<Item> SPAGHETTI_W_SAUCE = ITEMS.register("spaghetti_tomato_sauce",() -> new SpaghettiItem(new Item.Properties().group(ItemGroup.FOOD)));
    public static final RegistryObject<Item> SPAGHETTI_W_SAUCE_N_CHEESE = ITEMS.register("spaghetti_tomato_sauce_cheese",() -> new SpaghettiItem(new Item.Properties().group(ItemGroup.FOOD)));
    public static final RegistryObject<Item> CHEESE = ITEMS.register("cheese",()-> new Item(new Item.Properties().food(new Food.Builder().hunger(2).saturation(2).build()).group(ItemGroup.FOOD)));
}
