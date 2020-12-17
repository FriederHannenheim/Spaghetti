package com.fhannenheim.spaghetti.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SpaghettiCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {

    private final ISpaghettiCapability inst = SpaghettiCapability.SPAGHETTI_CAPABILITY.getDefaultInstance();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return LazyOptional.of(()->inst).cast();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) SpaghettiCapability.SPAGHETTI_CAPABILITY.getStorage().writeNBT(SpaghettiCapability.SPAGHETTI_CAPABILITY,inst,null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        SpaghettiCapability.SPAGHETTI_CAPABILITY.getStorage().readNBT(SpaghettiCapability.SPAGHETTI_CAPABILITY,inst,null, nbt);
    }
}
