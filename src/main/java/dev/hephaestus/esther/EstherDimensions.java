package dev.hephaestus.esther;

import dev.hephaestus.esther.dims.EmptyChunkGenerator;
import dev.hephaestus.esther.dims.ImprintDimension;
import dev.hephaestus.esther.dims.OverworldSourceDimension;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensionType;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;

import java.util.function.Supplier;

public class EstherDimensions {
    public static final FabricDimensionType IMPRINT;
    public static final FabricDimensionType OVERWORLD_SOURCE;
    public static final ChunkGeneratorType<ChunkGeneratorConfig, EmptyChunkGenerator> EMPTY_CHUNK_GENERATOR;

    static {
        IMPRINT = FabricDimensionType.builder()
                .defaultPlacer((entity, world, dim, offsetX, offsetZ) -> new BlockPattern.TeleportTarget(new Vec3d(entity.getBlockPos().getX(), world.getChunk(entity.getBlockPos().getX() >> 4, entity.getBlockPos().getZ() >> 4).sampleHeightmap(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, entity.getBlockPos().getX() & 15, entity.getBlockPos().getZ() & 15) + 1, entity.getBlockPos().getZ()), entity.getVelocity(), (int) entity.yaw))
                .factory(ImprintDimension::new)
                .skyLight(true)
                .buildAndRegister(Esther.newID("imprint"));

        OVERWORLD_SOURCE = FabricDimensionType.builder()
                .defaultPlacer((entity, world, dim, offsetX, offsetZ) -> new BlockPattern.TeleportTarget(new Vec3d(0,0,0), entity.getVelocity(), (int) entity.yaw))
                .factory(OverworldSourceDimension::new)
                .skyLight(true)
                .buildAndRegister(Esther.newID("overworld_source"));

        EMPTY_CHUNK_GENERATOR = FabricChunkGeneratorType.register(Esther.newID("empty"), EmptyChunkGenerator::new, ChunkGeneratorConfig::new, false);
    }

    public static void init() {}

    public static final class FabricChunkGeneratorType<C extends ChunkGeneratorConfig, T extends ChunkGenerator<C>> extends ChunkGeneratorType<C, T>
    {
        private final FabricChunkGeneratorFactory<C, T> factory;

        private FabricChunkGeneratorType(FabricChunkGeneratorFactory<C, T> factory, boolean buffetScreenOption, Supplier<C> settingsSupplier)
        {
            super(null, buffetScreenOption, settingsSupplier);
            this.factory = factory;
        }

        /**
         * Called to register and create new instance of the ChunkGeneratorType.
         * @param id registry ID of the ChunkGeneratorType
         * @param factory factory instance to provide a ChunkGenerator
         * @param settingsSupplier config supplier
         * @param buffetScreenOption whether or not the ChunkGeneratorType should appear in the buffet screen options page
         */
        public static <C extends ChunkGeneratorConfig, T extends ChunkGenerator<C>> FabricChunkGeneratorType<C, T> register(Identifier id, FabricChunkGeneratorFactory<C, T> factory, Supplier<C> settingsSupplier, boolean buffetScreenOption) {
            return Registry.register(Registry.CHUNK_GENERATOR_TYPE, id, new FabricChunkGeneratorType<>(factory, buffetScreenOption, settingsSupplier));
        }

        /**
         * Called to get an instance of the ChunkGeneratorType's ChunkGenerator.
         * @param world DimensionType's world instance
         * @param biomeSource BiomeSource to use while generating the world
         * @param config ChunkGenerator config instance
         */
        @Override
        public T create(World world, BiomeSource biomeSource, C config) {
            return factory.create(world, biomeSource, config);
        }

        /**
         * Responsible for creating the FabricChunkGeneratorType's ChunkGenerator instance.
         * Called when a new instance of a ChunkGenerator is requested in the ChunkGeneratorType.
         * @param <C> ChunkGeneratorConfig
         * @param <T> ChunkGenerator
         */
        @FunctionalInterface
        public interface FabricChunkGeneratorFactory<C extends ChunkGeneratorConfig, T extends ChunkGenerator<C>> {
            T create(World world, BiomeSource source, C config);
        }
    }
}
