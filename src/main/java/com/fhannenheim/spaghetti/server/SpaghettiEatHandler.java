package com.fhannenheim.spaghetti.server;

import com.fhannenheim.spaghetti.Spaghetti;
import com.fhannenheim.spaghetti.SpaghettiType;
import com.fhannenheim.spaghetti.capabilities.ISpaghettiCapability;
import com.fhannenheim.spaghetti.capabilities.SpaghettiCapability;
import com.fhannenheim.spaghetti.networking.Networking;
import com.fhannenheim.spaghetti.networking.SyncSpaghettiWithClient;
import com.fhannenheim.spaghetti.registries.ItemRegistry;
import com.fhannenheim.spaghetti.registries.SoundRegistry;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.*;

public class SpaghettiEatHandler {
    public Map<String,Integer> playersEating = new HashMap<>();
    public Map<String,Integer> playerSoundTickCount = new HashMap<>();
    private static final Dictionary<SpaghettiType,Item> ITEM_DICTIONARY = new Hashtable<SpaghettiType,Item>(){{
        put(SpaghettiType.Spaghetti,ItemRegistry.SPAGHETTI.get());
        put(SpaghettiType.SpaghettiWSauce,ItemRegistry.SPAGHETTI_W_SAUCE.get());
        put(SpaghettiType.SpaghettiWSauceNCheese,ItemRegistry.SPAGHETTI_W_SAUCE_N_CHEESE.get());
    }};
    private static final Dictionary<SpaghettiType,Integer> HUNGER_DICTIONARY = new Hashtable<SpaghettiType,Integer>(){{
        put(SpaghettiType.Spaghetti,3);
        put(SpaghettiType.SpaghettiWSauce,5);
        put(SpaghettiType.SpaghettiWSauceNCheese,7);
    }};
    @SubscribeEvent
    public void serverTick(TickEvent.ServerTickEvent event){
        for (String uuid : playersEating.keySet()){
            ServerPlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(UUID.fromString(uuid));
            if(player == null)
                continue;
            int eatTicksLeft = playersEating.get(uuid);
            Random random = player.world.rand;

            if (eatTicksLeft <= 0){
                playersEating.remove(uuid);
                playerSoundTickCount.remove(uuid);
                ISpaghettiCapability cap = player.getCapability(SpaghettiCapability.SPAGHETTI_CAPABILITY).orElseThrow(()-> new IllegalStateException(Spaghetti.NO_CAP_ERROR));
                int hunger = HUNGER_DICTIONARY.get(cap.getType());
                player.getFoodStats().addStats(hunger,hunger);
                cap.setType(SpaghettiType.NoSpaghetti);
                player.world.playSound(null,player.getPosition(),SoundEvents.ENTITY_PLAYER_BURP,SoundCategory.PLAYERS,1,player.world.rand.nextFloat() * 0.1F + 0.9F);
                Networking.INSTANCE.send(PacketDistributor.ALL.noArg(), new SyncSpaghettiWithClient(player.getUniqueID(),(CompoundNBT)SpaghettiCapability.SPAGHETTI_CAPABILITY.getStorage().writeNBT(
                        SpaghettiCapability.SPAGHETTI_CAPABILITY,cap, Direction.DOWN)));

                continue;
            }

            int tickCounter = playerSoundTickCount.get(uuid);
            tickCounter++;
            if (tickCounter >= 6){
                tickCounter = 0;
                player.playSound(SoundRegistry.SLURP.get(),1,1);
            }
            playerSoundTickCount.replace(uuid,tickCounter);

            playersEating.replace(uuid,eatTicksLeft-1);
            SpaghettiType type = player.getCapability(SpaghettiCapability.SPAGHETTI_CAPABILITY).orElseThrow(()->new IllegalStateException(Spaghetti.NO_CAP_ERROR)).getType();
            player.world.addParticle(new ItemParticleData(ParticleTypes.ITEM,new ItemStack(ITEM_DICTIONARY.get(type))),player.getPosX()+random.nextFloat()-0.5f,player.getPosY()+random.nextFloat()-0.5f,player.getPosZ()+random.nextFloat()-0.5f,0,0,0);
        }
    }
}
