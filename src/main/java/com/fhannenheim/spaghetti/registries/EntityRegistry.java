package com.fhannenheim.spaghetti.registries;

import com.fhannenheim.spaghetti.Spaghetti;
import com.fhannenheim.spaghetti.entities.SpaghettiEntity;
import com.fhannenheim.spaghetti.entities.UncookedSpaghettiItemEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, Spaghetti.MOD_ID);

    public static final RegistryObject<EntityType<UncookedSpaghettiItemEntity>> UNCOOKED_SPAGHETTI = ENTITIES.register(
            "uncooked_spaghetti",
            () -> EntityType.Builder.<UncookedSpaghettiItemEntity>create(UncookedSpaghettiItemEntity::new, EntityClassification.MISC).size(0.25F, 0.25F)
                    .build(new ResourceLocation(Spaghetti.MOD_ID,"uncooked_spaghetti").toString()));
    public static final RegistryObject<EntityType<SpaghettiEntity>> SPAGHETTI = ENTITIES.register(
            "spaghetti",
            () -> EntityType.Builder.<SpaghettiEntity>create(SpaghettiEntity::new, EntityClassification.MISC).size(0.25F, 0.25F)
                    .build(new ResourceLocation(Spaghetti.MOD_ID,"spaghetti").toString()));

}
