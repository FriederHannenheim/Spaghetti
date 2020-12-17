package com.fhannenheim.spaghetti.render;

import com.fhannenheim.spaghetti.Spaghetti;
import com.fhannenheim.spaghetti.SpaghettiType;
import com.fhannenheim.spaghetti.capabilities.ISpaghettiCapability;
import com.fhannenheim.spaghetti.capabilities.SpaghettiCapability;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Dictionary;
import java.util.Hashtable;

@OnlyIn(Dist.CLIENT)
public class SpaghettiLayer<T extends LivingEntity, M extends BipedModel<T>, A extends BipedModel<T>> extends ArmorLayer<T, M, A> {
    private static final Dictionary<SpaghettiType,String> textures = new Hashtable<SpaghettiType,String>(){{
        put(SpaghettiType.Spaghetti,"textures/models/spaghetti.png");
        put(SpaghettiType.SpaghettiWSauce,"textures/models/spaghetti_w_sauce.png");
        put(SpaghettiType.SpaghettiWSauceNCheese,"textures/models/spaghetti_w_sauce_n_cheese.png");
    }};
    public SpaghettiLayer(IEntityRenderer<T, M> p_i50936_1_) {
        super(p_i50936_1_, (A) new BipedModel<>(0.5F), (A) new BipedModel<>(1.0F));
    }

    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        this.renderArmorPart(matrixStackIn, bufferIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, EquipmentSlotType.HEAD, packedLightIn);
    }

    private void renderArmorPart(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, T entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, EquipmentSlotType slotIn, int packedLightIn) {
        A a = this.getModelFromSlot(slotIn);
        ((BipedModel) this.getEntityModel()).setModelAttributes(a);
        a.setLivingAnimations(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks);
        this.setModelSlotVisible(a, slotIn);
        a.setRotationAngles(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        ISpaghettiCapability cap = entityLivingBaseIn.getCapability(SpaghettiCapability.SPAGHETTI_CAPABILITY).orElseThrow(() -> new IllegalStateException(Spaghetti.NO_CAP_ERROR));
        if (cap.getType() == SpaghettiType.NoSpaghetti) {
            return;
        }
        renderArmor(matrixStackIn, bufferIn, packedLightIn, false, a, 1.0F, 1.0F, 1.0F, new ResourceLocation(Spaghetti.MOD_ID, textures.get(cap.getType())));
    }

    private void renderArmor(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, boolean glintIn, A modelIn, float red, float green, float blue, ResourceLocation armorResource) {
        IVertexBuilder ivertexbuilder = ItemRenderer.getBuffer(bufferIn, RenderType.getEntityCutoutNoCull(armorResource), false, glintIn);
        modelIn.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
    }

    protected void setModelSlotVisible(A modelIn, EquipmentSlotType slotIn) {
        this.setModelVisible(modelIn);
        modelIn.bipedHead.showModel = true;
        modelIn.bipedHeadwear.showModel = true;
    }

    protected void setModelVisible(A model) {
        model.setVisible(false);
    }
}