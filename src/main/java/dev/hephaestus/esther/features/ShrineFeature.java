package dev.hephaestus.esther.features;

import com.mojang.datafixers.Dynamic;
import dev.hephaestus.esther.Esther;
import dev.hephaestus.esther.block.ShrineBlockBottom;
import dev.hephaestus.esther.block.ShrineBlockTop;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.Feature;

import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

public class ShrineFeature extends Feature<ShrineFeatureConfig> {

    public ShrineFeature(Function<Dynamic<?>, ? extends ShrineFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean generate(IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> generator, Random random, BlockPos pos, ShrineFeatureConfig config) {
        BlockPos setPos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, pos);

        if (world.getBlockState(setPos.down()).getBlock() instanceof LeavesBlock) {
            while (world.getBlockState(setPos.down()).getBlock() instanceof LeavesBlock || world.getBlockState(setPos.down()).getBlock() == Blocks.AIR) {
                setPos = setPos.down();
            }
        }


        if (world.getBlockState(setPos.down()).getBlock() == Blocks.GRASS_BLOCK) {
            if (Esther.DEBUG) {
                world.setBlockState(setPos.add(0,30,0), Blocks.GLOWSTONE.getDefaultState(), 3);
            }

            Esther.debug( "Shrine at " + setPos);


            world.setBlockState(setPos, config.getBottomBlock().getDefaultState(), 3);
            world.setBlockState(setPos.up(), config.getTopBlock().getDefaultState(), 3);

            return true;
        }

        return false;
    }

    public static void create(Biome biome, String name) {
        Esther.log("Registering new shrine for biome " + Registry.BIOME.getId(biome));

        String biome_path = Objects.requireNonNull(Registry.BIOME.getId(biome)).getPath();

        if (!Registry.BLOCK.getOrEmpty(Esther.newID(name + "_shrine_bottom")).isPresent()) {
            Block bottomBlock = Registry.register(
                    Registry.BLOCK,
                    Esther.newID(name + "_shrine_bottom"),
                    new ShrineBlockBottom(FabricBlockSettings.of(Material.STONE).strength(0.5f, 1.0f).nonOpaque().sounds(BlockSoundGroup.STONE).build(), biome)
            );

            Registry.register(Registry.BLOCK,
                    Esther.newID(name + "_shrine_top"),
                    new ShrineBlockTop(FabricBlockSettings.of(Material.STONE).strength(0.5f, 1.0f).nonOpaque().sounds(BlockSoundGroup.STONE).build())
            );
        }

        ShrineFeature shrine = Registry.register(Registry.FEATURE,
            Esther.newID(biome_path + "_shrine"), new ShrineFeature(ShrineFeatureConfig::deserialize));

        biome.addFeature(
            GenerationStep.Feature.SURFACE_STRUCTURES,
            shrine.configure(new ShrineFeatureConfig(biome, name)).createDecoratedFeature(Decorator.CHANCE_HEIGHTMAP.configure(new ChanceDecoratorConfig(
                50
            )))
        );
    }
}
