package com.fhannenheim.spaghetti.networking;

import com.fhannenheim.spaghetti.Spaghetti;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class EatSpaghetti {
    public boolean eating = true;
    public EatSpaghetti(boolean eating) {
        this.eating = eating;
    }

    public EatSpaghetti(PacketBuffer buffer) {
        this.eating = buffer.readBoolean();
    }

    public PacketBuffer encode(PacketBuffer buf){
        buf.writeBoolean(this.eating);
        return buf;
    }


    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity serverplayer = ctx.get().getSender();
            if(serverplayer == null)
                return;
            if(eating){
                Spaghetti.eatHandler.playersEating.put(serverplayer.getUniqueID().toString(),32);
                Spaghetti.eatHandler.playerSoundTickCount.put(serverplayer.getUniqueID().toString(),0);
            } else {
                Spaghetti.eatHandler.playersEating.remove(serverplayer);
                Spaghetti.eatHandler.playerSoundTickCount.remove(serverplayer);
            }
        });
        return true;
    }
}
