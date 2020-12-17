package com.fhannenheim.spaghetti.items;

import com.fhannenheim.spaghetti.entities.UncookedSpaghettiItemEntity;
import com.fhannenheim.spaghetti.registries.EntityRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class UncookedSpaghetti extends Item {

    public UncookedSpaghetti(Properties properties) {
        super(properties);

    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (!flagIn.isAdvanced())
            return;
        try{
            tooltip.add(new StringTextComponent("Cook time: " + stack.getTag().getInt("cook_time")).applyTextStyle(TextFormatting.GRAY));
        } catch (NullPointerException ignored){}
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        UncookedSpaghettiItemEntity entity = new UncookedSpaghettiItemEntity(EntityRegistry.UNCOOKED_SPAGHETTI.get(),
                world,location.getPosX(),location.getPosY(),location.getPosZ(),itemstack);
        entity.setPosition(location.getPosX(),location.getPosY(),location.getPosZ());
        entity.setMotion(location.getMotion());
        entity.setPickupDelay(40);
        return entity;
    }
}
