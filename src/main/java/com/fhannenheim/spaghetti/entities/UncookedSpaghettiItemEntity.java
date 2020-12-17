package com.fhannenheim.spaghetti.entities;

import com.fhannenheim.spaghetti.registries.ItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class UncookedSpaghettiItemEntity extends ItemEntity {
    public UncookedSpaghettiItemEntity(EntityType<? extends ItemEntity> p_i50217_1_, World p_i50217_2_) {
        super(p_i50217_1_, p_i50217_2_);
        createTag();
    }

    public UncookedSpaghettiItemEntity(EntityType<? extends ItemEntity> p_i50217_1_, World p_i50217_2_, double posX, double posY, double posZ, ItemStack stack) {
        super(p_i50217_1_, p_i50217_2_);
        this.setPosition(posX, posY, posZ);
        this.setItem(stack);
        this.lifespan = (stack.getItem() == null ? 6000 : stack.getEntityLifespan(p_i50217_2_));
        createTag();
    }
    void createTag(){
        CompoundNBT tag = this.getItem().getOrCreateTag();
        if(tag.getInt("cook_time") == 0)
            tag.putInt("cook_time",300);
        ItemStack newItem = this.getItem().copy();
        newItem.setTag(tag);
        this.setItem(newItem);
    }
    @Override
    public void tick() {
        super.tick();
        if (world.isRemote())
            return;
        BlockState inBlock = this.world.getBlockState(this.getPosition());
        if (inBlock.getBlock() == Blocks.CAULDRON) {
            if (inBlock.get(BlockStateProperties.LEVEL_0_3) == 3 &&
                    this.world.getBlockState(this.getPosition().down()).has(BlockStateProperties.LIT) &&
                    this.world.getBlockState(this.getPosition().down()).get(BlockStateProperties.LIT)) {

                CompoundNBT tag = this.getItem().getOrCreateTag();
                tag.putInt("cook_time", tag.getInt("cook_time") - 1);
                ItemStack newItem = this.getItem().copy();
                newItem.setTag(tag);
                this.setItem(newItem);
            }
        }
        if (this.getItem().getOrCreateTag().getInt("cook_time") <= 0) {
            world.addEntity(new ItemEntity(world, getPosX(), getPosY(), getPosZ(), new ItemStack(ItemRegistry.SPAGHETTI.get(), this.getDataManager().get(ITEM).getCount())));
            world.playSound(null, this.getPosition(), SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.PLAYERS, 2, 2f);
            this.remove();
        }
    }
    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
