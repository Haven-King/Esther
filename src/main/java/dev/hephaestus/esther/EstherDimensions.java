package dev.hephaestus.esther;

import dev.hephaestus.esther.dims.EmptyChunkGenerator;
import dev.hephaestus.esther.dims.ImprintDimension;
import dev.hephaestus.esther.dims.OverworldSourceDimension;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensionType;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;

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
}
