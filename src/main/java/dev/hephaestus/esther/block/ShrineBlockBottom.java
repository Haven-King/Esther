package dev.hephaestus.esther.block;

import dev.hephaestus.esther.Esther;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Objects;

public class ShrineBlockBottom extends Block {
    private final static VoxelShape shape = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 27.5, 16.0);
    private final Biome biome;

    public ShrineBlockBottom(Settings settings, Biome biome) {
        super(settings);
        this.biome = biome;
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext context) {
        return ShrineBlockBottom.shape;
    }

    @Override
    public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        super.onBlockRemoved(state, world, pos, newState, moved);
        if (world.getBlockState(pos.up()).getBlock() instanceof ShrineBlockTop) {
            world.removeBlock(pos.up(), false);
        }
    }

    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.BLOCK;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        world.setBlockState(pos.up(), Registry.BLOCK.get(Esther.newID(Objects.requireNonNull(Registry.BIOME.getId(biome)).getPath() + "_shrine_top")).getDefaultState());
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return ItemStack.EMPTY;
    }
}
