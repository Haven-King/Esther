package dev.hephaestus.esther.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class ShrineBlockTop extends Block {
    private final static VoxelShape shape = Block.createCuboidShape(0.0, -16.0, 0.0, 16.0, 11.5, 16.0);

    public ShrineBlockTop(Settings settings) {
        super(settings);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext context) {
        return ShrineBlockTop.shape;
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        if (world.getBlockState(pos.down()).getBlock() instanceof ShrineBlockBottom) {
            world.breakBlock(pos.down(), false, player);
        }
    }
}
