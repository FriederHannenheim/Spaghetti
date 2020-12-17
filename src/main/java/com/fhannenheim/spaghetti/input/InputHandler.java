package com.fhannenheim.spaghetti.input;

import com.fhannenheim.spaghetti.Spaghetti;
import com.fhannenheim.spaghetti.SpaghettiType;
import com.fhannenheim.spaghetti.capabilities.SpaghettiCapability;
import com.fhannenheim.spaghetti.networking.EatSpaghetti;
import com.fhannenheim.spaghetti.networking.Networking;
import com.fhannenheim.spaghetti.registries.SoundRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class InputHandler {
    public boolean eatingSpaghetti = false;
    int tickCounter;
    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event){
        PlayerEntity player = Minecraft.getInstance().player;
        if(
                player != null &&
                event.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT &&
                event.getAction() == GLFW.GLFW_PRESS &&
                player.getHeldItem(Hand.MAIN_HAND).isEmpty() &&
                !eatingSpaghetti &&
                player.getCapability(SpaghettiCapability.SPAGHETTI_CAPABILITY).orElseThrow(()-> new IllegalStateException(Spaghetti.NO_CAP_ERROR)).getType() != SpaghettiType.NoSpaghetti
        ){
            eatingSpaghetti = true;
            Networking.sendToServer(new EatSpaghetti(true));
            return;
        }
        if(eatingSpaghetti){
            eatingSpaghetti = false;
            Networking.sendToServer(new EatSpaghetti(false));
        }
    }

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event){
        if(event.phase == TickEvent.Phase.START)
            return;
        if(eatingSpaghetti){
            tickCounter++;
            if (tickCounter >= 6){
                tickCounter = 0;
                PlayerEntity player = Minecraft.getInstance().player;
                if(player == null)
                    return;
                player.playSound(SoundRegistry.SLURP.get(),1,1);
            }
        }
    }
}
