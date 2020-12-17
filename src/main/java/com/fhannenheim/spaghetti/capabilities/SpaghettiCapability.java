package com.fhannenheim.spaghetti.capabilities;

import com.fhannenheim.spaghetti.SpaghettiType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class SpaghettiCapability {
    @CapabilityInject(ISpaghettiCapability.class)
    public static Capability<ISpaghettiCapability> SPAGHETTI_CAPABILITY = null;
    public static void register()
    {
        CapabilityManager.INSTANCE.register(ISpaghettiCapability.class, new DefaultSpaghettiStorage<>(), SpaghettiCapImpl::new);
    }

    private static class DefaultSpaghettiStorage<T extends ISpaghettiCapability> implements Capability.IStorage<T> {
        @Override
        public INBT writeNBT(Capability<T> capability, T instance, Direction side)
        {
            if (!(instance instanceof SpaghettiCapImpl))
                throw new RuntimeException("Cannot serialize to an instance that isn't the default implementation");
            CompoundNBT nbt = new CompoundNBT();
            SpaghettiCapImpl cap = (SpaghettiCapImpl) instance;
            nbt.putString("type", cap.getType().toString());
            return nbt;
        }

        @Override
        public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt)
        {
            if (!(instance instanceof SpaghettiCapImpl))
                throw new RuntimeException("Cannot deserialize to an instance that isn't the default implementation");
            CompoundNBT tags = (CompoundNBT) nbt;
            SpaghettiCapImpl cap = (SpaghettiCapImpl) instance;
            cap.setType(SpaghettiType.valueOf(tags.getString("type")));
        }
    }
}
