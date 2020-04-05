package dev.hephaestus.esther.block;

import dev.hephaestus.esther.Esther;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        if (world.getBlockState(pos.up()).getBlock() instanceof ShrineBlockTop) {
            world.breakBlock(pos.up(), false, player);
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        world.setBlockState(pos.up(), Registry.BLOCK.get(Esther.newID(Objects.requireNonNull(Registry.BIOME.getId(biome)).getPath() + "_shrine_top")).getDefaultState());
    }
}
