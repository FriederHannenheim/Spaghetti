package com.fhannenheim.spaghetti;

import com.fhannenheim.spaghetti.capabilities.ISpaghettiCapability;
import com.fhannenheim.spaghetti.capabilities.SpaghettiCapability;
import com.fhannenheim.spaghetti.capabilities.SpaghettiCapabilityProvider;
import com.fhannenheim.spaghetti.entities.SpaghettiEntity;
import com.fhannenheim.spaghetti.input.InputHandler;
import com.fhannenheim.spaghetti.networking.Networking;
import com.fhannenheim.spaghetti.networking.SyncSpaghettiWithClient;
import com.fhannenheim.spaghetti.registries.BlockRegistry;
import com.fhannenheim.spaghetti.registries.EntityRegistry;
import com.fhannenheim.spaghetti.registries.ItemRegistry;
import com.fhannenheim.spaghetti.registries.SoundRegistry;
import com.fhannenheim.spaghetti.render.CustomItemRenderer;
import com.fhannenheim.spaghetti.render.RenderSpaghettiOnFace;
import com.fhannenheim.spaghetti.render.SpaghettiLayer;
import com.fhannenheim.spaghetti.render.SpriteRenderFactory;
import com.fhannenheim.spaghetti.server.SpaghettiEatHandler;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.ProjectileDispenseBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.BasicTrade;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;


@Mod("spaghetti")
public class Spaghetti {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "spaghetti";
    public static final String MOD_VER = "1.0";
    public static final String NO_CAP_ERROR = "No capabillity was registered on the player. Only report this if it crashed your world.";
    public static SpaghettiEatHandler eatHandler;
    public static InputHandler inputHandler;

    public Spaghetti() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BlockRegistry.BLOCKS.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        EntityRegistry.ENTITIES.register(modEventBus);
        SoundRegistry.SOUNDS.register(modEventBus);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            modEventBus.addListener(this::clientSetup);
            inputHandler = new InputHandler();
            MinecraftForge.EVENT_BUS.register(inputHandler);
        });
        ;
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.addListener(RenderSpaghettiOnFace::renderSpaghettiOverlay);
        MinecraftForge.EVENT_BUS.register(this);
        Networking.registerMessages();
    }

    @OnlyIn(Dist.CLIENT)
    public void clientSetup(final FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(BlockRegistry.TOMATO_CROP.get(), RenderType.getCutout());
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.UNCOOKED_SPAGHETTI.get(), CustomItemRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.SPAGHETTI.get(), new SpriteRenderFactory());
        Map<String, PlayerRenderer> skinMap = Minecraft.getInstance().getRenderManager().getSkinMap();

        for (PlayerRenderer render : skinMap.values()) {
            render.addLayer(new SpaghettiLayer<>(render));
        }
    }

    public void commonSetup(final FMLCommonSetupEvent event) {
        SpaghettiCapability.register();
        eatHandler = new SpaghettiEatHandler();
        MinecraftForge.EVENT_BUS.register(eatHandler);
        for (Item item : new Item[]{ItemRegistry.SPAGHETTI.get(), ItemRegistry.SPAGHETTI_W_SAUCE.get(), ItemRegistry.SPAGHETTI_W_SAUCE_N_CHEESE.get()}) {
            DispenserBlock.registerDispenseBehavior(item, new ProjectileDispenseBehavior() {
                @Override
                protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                    SpaghettiEntity entity = new SpaghettiEntity(worldIn, position.getX(), position.getY(), position.getZ());
                    entity.setItem(new ItemStack(item));
                    return entity;
                }
            });
        }
    }

    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject().getType() == EntityType.PLAYER) {
            event.addCapability(new ResourceLocation(MOD_ID, "spaghetcap"), new SpaghettiCapabilityProvider());
        }
    }

    @SubscribeEvent
    public void addVillagerTrades(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.FARMER) {
            event.getTrades().get(1).add(new BasicTrade(new ItemStack(Items.EMERALD), new ItemStack(ItemRegistry.TOMATO.get()), 16, 3, 0));
        }

    }

    @SubscribeEvent
    public void playerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        // Send spaghetti data of the joining player to all players
        ISpaghettiCapability cap = event.getPlayer().getCapability(SpaghettiCapability.SPAGHETTI_CAPABILITY).orElseThrow(() -> new IllegalStateException(Spaghetti.NO_CAP_ERROR));
        Networking.INSTANCE.send(PacketDistributor.ALL.noArg(), new SyncSpaghettiWithClient(event.getPlayer().getUniqueID(), (CompoundNBT) SpaghettiCapability.SPAGHETTI_CAPABILITY.getStorage().writeNBT(SpaghettiCapability.SPAGHETTI_CAPABILITY, cap, Direction.DOWN)));
    }

    @SubscribeEvent
    public void blockRightClick(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.CAULDRON) {
            if (player.getHeldItem(event.getHand()).getItem() == Items.MILK_BUCKET) {
                event.getWorld().setBlockState(event.getPos(), BlockRegistry.CAULDRON.get().getDefaultState());
                player.setHeldItem(event.getHand(), new ItemStack(Items.BUCKET));
            }
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
            itemRegistryEvent.getRegistry().register(new BlockItem(BlockRegistry.CAULDRON.get(), new Item.Properties()).setRegistryName("cauldron"));

        }
    }
}
