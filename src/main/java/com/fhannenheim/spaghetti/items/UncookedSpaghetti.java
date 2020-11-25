package com.fhannenheim.spaghetti.items;

import com.fhannenheim.spaghetti.entities.UncookedSpaghettiItemEntity;
import com.fhannenheim.spaghetti.registries.ItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;

public class UncookedSpaghetti extends Item {

    public UncookedSpaghetti(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        ItemEntity itemEntity = new UncookedSpaghettiItemEntity(world, location.getPosX(), location.getPosY(), location.getPosZ(), itemstack);
        itemEntity.setMotion(location.getMotion());
        itemEntity.setPickupDelay(40);
        return itemEntity;
    }
}
