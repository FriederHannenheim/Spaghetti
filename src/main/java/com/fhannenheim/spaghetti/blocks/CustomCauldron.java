package com.fhannenheim.spaghetti.blocks;

import com.fhannenheim.spaghetti.registries.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.stream.Stream;

public class CustomCauldron extends Block {
    public static final EnumProperty<Contents> CONTENTS = EnumProperty.create("contents", Contents.class);
    private static final VoxelShape INSIDE = makeCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.or(makeCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), makeCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), INSIDE), IBooleanFunction.ONLY_FIRST);
    private static final VoxelShape CHEESE_SHAPE = Stream.of(
            Block.makeCuboidShape(0, 3, 0, 2, 16, 16),
            Block.makeCuboidShape(2, 3, 2, 14, 4, 14),
            Block.makeCuboidShape(14, 3, 0, 16, 16, 16),
            Block.makeCuboidShape(2, 3, 0, 14, 16, 2),
            Block.makeCuboidShape(2, 3, 14, 14, 16, 16),
            Block.makeCuboidShape(0, 0, 0, 4, 3, 2),
            Block.makeCuboidShape(0, 0, 2, 2, 3, 4),
            Block.makeCuboidShape(12, 0, 0, 16, 3, 2),
            Block.makeCuboidShape(14, 0, 2, 16, 3, 4),
            Block.makeCuboidShape(0, 0, 14, 4, 3, 16),
            Block.makeCuboidShape(0, 0, 12, 2, 3, 14),
            Block.makeCuboidShape(12, 0, 14, 16, 3, 16),
            Block.makeCuboidShape(14, 0, 12, 16, 3, 14),
            Block.makeCuboidShape(2, 7, 2, 14, 15, 14)
    ).reduce((v1, v2) -> {
        return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);
    }).get();

    public CustomCauldron(Block.Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(CONTENTS, Contents.MILK));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack handItem = player.getHeldItem(handIn);
        if (state.get(CONTENTS) == Contents.CHEESE && handItem.isEmpty()) {
            worldIn.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
            player.dropItem(new ItemStack(ItemRegistry.CHEESE.get(), 4), false);
            worldIn.playSound(null,pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS,.7f,1);
        } else if (handItem.getItem() == Items.BUCKET && state.get(CONTENTS) == Contents.MILK) {
            if (!player.abilities.isCreativeMode) {
                handItem.shrink(1);
                if (handItem.isEmpty()) {
                    player.setHeldItem(handIn, new ItemStack(Items.MILK_BUCKET));
                } else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.MILK_BUCKET))) {
                    player.dropItem(new ItemStack(Items.MILK_BUCKET), false);
                }
                worldIn.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
            }
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        super.tick(state, worldIn, pos, rand);
        if (!worldIn.isAreaLoaded(pos, 1))
            return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (state.get(CONTENTS) == Contents.MILK && RANDOM.nextInt() % 10 == 0) {
            worldIn.setBlockState(pos, this.withContents(Contents.CHEESE), 2);
        }
    }

    public BlockState withContents(Contents contents) {
        return this.getDefaultState().with(CONTENTS, contents);
    }

    @Override
    public boolean ticksRandomly(BlockState state) {
        return true;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(CONTENTS);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return state.get(CONTENTS) == Contents.MILK ? SHAPE : CHEESE_SHAPE;
    }

    public enum Contents implements IStringSerializable {
        MILK("milk"),
        CHEESE("cheese");
        private final String name;

        Contents(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        @Nonnull
        @Override
        public String getName() {
            return name;
        }
    }
}
