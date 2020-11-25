package com.fhannenheim.spaghetti.blocks;

import com.fhannenheim.spaghetti.registries.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class Tomato extends CropsBlock {

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.makeCuboidShape(0.0D,0.0D,0.0D,16.0D,3.0D, 16.0D),
            Block.makeCuboidShape(0.0D,0.0D,0.0D,16.0D,9.0D, 16.0D),
            Block.makeCuboidShape(0.0D,0.0D,0.0D,16.0D,16.0D, 16.0D),
            Block.makeCuboidShape(0.0D,0.0D,0.0D,16.0D,16.0D, 16.0D),
    };

    public Tomato(Properties builder) {
        super(builder);
    }

    @Override
    protected IItemProvider getSeedsItem() {
        return ItemRegistry.TOMATO.get();
    }

    @Override
    public int getMaxAge() {
        return 3;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE_BY_AGE[state.get(this.getAgeProperty())];
    }
}
