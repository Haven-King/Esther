package dev.hephaestus.esther.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeArray;

public interface MutableBiomeArray {
    static MutableBiomeArray inject(BiomeArray biomeArray) {
        return (MutableBiomeArray) biomeArray;
    }

    void setBiome(int x, int z, Biome biome);

    default void setBiome(BlockPos pos, Biome biome) {
        this.setBiome(pos.getX(), pos.getZ(), biome);
    }
}
