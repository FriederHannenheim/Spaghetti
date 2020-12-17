package com.fhannenheim.spaghetti.networking;

import com.fhannenheim.spaghetti.Spaghetti;
import com.fhannenheim.spaghetti.SpaghettiType;
import com.fhannenheim.spaghetti.capabilities.SpaghettiCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SyncSpaghettiWithClient {
    public UUID uuid;
    public CompoundNBT capability;

    public SyncSpaghettiWithClient(UUID uuid, CompoundNBT capability) {
        this.uuid = uuid;
        this.capability = capability;
    }

    public SyncSpaghettiWithClient(PacketBuffer buffer) {
        capability = buffer.readCompoundTag();
        uuid = buffer.readUniqueId();
    }
    public PacketBuffer encode(PacketBuffer buf){
        buf.writeCompoundTag(capability);
        buf.writeUniqueId(uuid);
        return buf;
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity minecraftPlayer = Minecraft.getInstance().player;
            if (minecraftPlayer != null) {
                PlayerEntity player = minecraftPlayer.world.getPlayerByUuid(uuid);
                assert player != null;
                player.getCapability(SpaghettiCapability.SPAGHETTI_CAPABILITY).ifPresent((cap)->{
                    cap.setType(SpaghettiType.valueOf(capability.getString("type")));
                    if(cap.getType() == SpaghettiType.NoSpaghetti)
                        Spaghetti.inputHandler.eatingSpaghetti = false;
                });
            }
        });
        return true;
    }
}
