package com.fhannenheim.spaghetti.entities;

import com.fhannenheim.spaghetti.SpaghettiType;
import com.fhannenheim.spaghetti.capabilities.SpaghettiCapability;
import com.fhannenheim.spaghetti.networking.Networking;
import com.fhannenheim.spaghetti.networking.SyncSpaghettiWithClient;
import com.fhannenheim.spaghetti.registries.ItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Dictionary;
import java.util.Hashtable;

public class SpaghettiEntity extends ProjectileItemEntity {
    private static final Dictionary<Item,SpaghettiType> TYPE_DICTIONARY = new Hashtable<Item,SpaghettiType>(){{
        put(ItemRegistry.SPAGHETTI.get(),SpaghettiType.Spaghetti);
        put(ItemRegistry.SPAGHETTI_W_SAUCE.get(),SpaghettiType.SpaghettiWSauce);
        put(ItemRegistry.SPAGHETTI_W_SAUCE_N_CHEESE.get(),SpaghettiType.SpaghettiWSauceNCheese);
    }};
    public SpaghettiEntity(EntityType<? extends SpaghettiEntity> p_i50159_1_, World p_i50159_2_) {
        super(p_i50159_1_, p_i50159_2_);
    }

    // The Entity doesn't render when I return SpaghettiEntity so I just use Snowball.
    public SpaghettiEntity(World worldIn, LivingEntity throwerIn) {
        super(EntityType.SNOWBALL, throwerIn, worldIn);
    }

    public SpaghettiEntity(World worldIn, double x, double y, double z) {
        super(EntityType.SNOWBALL, x, y, z, worldIn);
    }

    @OnlyIn(Dist.CLIENT)
    private IParticleData makeParticle() {
        ItemStack itemstack = this.func_213882_k();
        return (IParticleData)(itemstack.isEmpty() ? new ItemParticleData(ParticleTypes.ITEM, new ItemStack(ItemRegistry.SPAGHETTI.get())) : new ItemParticleData(ParticleTypes.ITEM, itemstack));
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            IParticleData iparticledata = this.makeParticle();

            for(int i = 0; i < 8; ++i) {
                this.world.addParticle(iparticledata, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.ENTITY) {
            Entity entity = ((EntityRayTraceResult)result).getEntity();
            int i = entity instanceof BlazeEntity ? 3 : 0;
            entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float)i);
            if(entity instanceof PlayerEntity){
                entity.getCapability(SpaghettiCapability.SPAGHETTI_CAPABILITY).ifPresent((cap)->{
                    if(TYPE_DICTIONARY.get(this.getItem().getItem()) != null){
                        cap.setType(TYPE_DICTIONARY.get(this.getItem().getItem()));
                    }
                    Networking.INSTANCE.send(PacketDistributor.ALL.noArg(), new SyncSpaghettiWithClient(entity.getUniqueID(),(CompoundNBT)SpaghettiCapability.SPAGHETTI_CAPABILITY.getStorage().writeNBT(
                            SpaghettiCapability.SPAGHETTI_CAPABILITY,cap, Direction.DOWN)));

                });
            }
        }
        if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte)3);
            this.remove();
        }

    }
    // Doesn't work when I return spaghetti. Minecraft rendering is weird
    @Override
    protected Item getDefaultItem() {
        return Items.SNOWBALL;
    }
}
