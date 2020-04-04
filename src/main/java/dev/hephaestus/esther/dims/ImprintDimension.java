package dev.hephaestus.esther.dims;

import dev.hephaestus.esther.EstherDimensions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.BiomeSourceType;
import net.minecraft.world.biome.source.FixedBiomeSourceConfig;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;

import javax.annotation.Nullable;

public class ImprintDimension extends Dimension {
    private static float[] backgroundColor = {1.0f, 1.0f, 1.0f,1.0f};

    public ImprintDimension(World world, DimensionType type) {
        super(world, type, 0f);
    }

    @Override
    public ChunkGenerator<?> createChunkGenerator() {
        ChunkGeneratorConfig chunkGeneratorConfig = new ChunkGeneratorConfig();
        FixedBiomeSourceConfig biomeConfig = BiomeSourceType.FIXED.getConfig(world.getLevelProperties()).setBiome(Biomes.THE_VOID);

        return EstherDimensions.EMPTY_CHUNK_GENERATOR.create(this.world, BiomeSourceType.FIXED.applyConfig(biomeConfig), chunkGeneratorConfig);
    }

    @Nullable
    @Override
    public BlockPos getSpawningBlockInChunk(ChunkPos chunkPos, boolean checkMobSpawnValidity) {
        return null;
    }

    @Nullable
    @Override
    public BlockPos getTopSpawningBlockPosition(int x, int z, boolean checkMobSpawnValidity) {
        return null;
    }

    @Override
    public float getSkyAngle(long timeOfDay, float tickDelta) {
        double d = MathHelper.fractionalPart((double)timeOfDay / 24000.0D - 0.25D);
        double e = 0.5D - Math.cos(d * 3.141592653589793D) / 2.0D;
        return (float)(d * 2.0D + e) / 3.0F;
    }

    @Nullable
    @Override
    @Environment(EnvType.CLIENT)
    public float[] getBackgroundColor(float skyAngle, float tickDelta) {
        return backgroundColor;
    }

    @Override
    public boolean hasVisibleSky() {
        return true;
    }

    @Override
    public Vec3d getFogColor(float skyAngle, float tickDelta) {
        return new Vec3d(1f,1f,1f);
    }

    @Override
    public boolean canPlayersSleep() {
        return true;
    }

    @Override
    public boolean isFogThick(int x, int z) {
        return true;
    }

    @Override
    public DimensionType getType() {
        return EstherDimensions.IMPRINT;
    }
}
