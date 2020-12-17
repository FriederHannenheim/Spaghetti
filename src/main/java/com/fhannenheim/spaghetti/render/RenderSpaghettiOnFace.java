package com.fhannenheim.spaghetti.render;

import com.fhannenheim.spaghetti.Spaghetti;
import com.fhannenheim.spaghetti.SpaghettiType;
import com.fhannenheim.spaghetti.capabilities.ISpaghettiCapability;
import com.fhannenheim.spaghetti.capabilities.SpaghettiCapability;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Dictionary;
import java.util.Hashtable;

public class RenderSpaghettiOnFace {
    private static final Dictionary<SpaghettiType,String> textures = new Hashtable<SpaghettiType,String>(){{
        put(SpaghettiType.Spaghetti,"textures/misc/face_spaghetti.png");
        put(SpaghettiType.SpaghettiWSauce,"textures/misc/face_spaghetti_w_sauce.png");
        put(SpaghettiType.SpaghettiWSauceNCheese,"textures/misc/face_spaghetti_w_sauce_n_cheese.png");
    }};
    @SubscribeEvent
    public static void renderSpaghettiOverlay(TickEvent.RenderTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.gameSettings.getPointOfView() != PointOfView.FIRST_PERSON || mc.currentScreen != null)
            return;
        ISpaghettiCapability cap = mc.player.getCapability(SpaghettiCapability.SPAGHETTI_CAPABILITY).orElseThrow(() -> new IllegalStateException(Spaghetti.NO_CAP_ERROR));
        if (cap.getType() == SpaghettiType.NoSpaghetti) {
            return;
        }
        int scaledWidth = mc.getMainWindow().getScaledWidth();
        int scaledHeight = mc.getMainWindow().getScaledHeight();
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.7F);
        RenderSystem.disableAlphaTest();
        mc.getTextureManager().bindTexture(new ResourceLocation(Spaghetti.MOD_ID, textures.get(cap.getType())));
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0.0D, (double)scaledHeight, -90.0D).tex(0.0F, 1.0F).endVertex();
        bufferbuilder.pos((double)scaledWidth, (double)scaledHeight, -90.0D).tex(1.0F, 1.0F).endVertex();
        bufferbuilder.pos((double)scaledWidth, 0.0D, -90.0D).tex(1.0F, 0.0F).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0F, 0.0F).endVertex();
        tessellator.draw();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableAlphaTest();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
