package dev.hephaestus.esther.mixin;

import dev.hephaestus.esther.util.MutableBiomeArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeArray;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BiomeArray.class)
public class BiomeArrayMixin implements MutableBiomeArray {
    @Shadow
    private Biome[] data;

    private static final int HORIZONTAL_SECTION_COUNT = (int) Math.round(Math.log(16.0D) / Math.log(2.0D)) - 2;
    private static final int VERTICAL_SECTION_COUNT = (int)Math.round(Math.log(256.0D) / Math.log(2.0D)) - 2;
    private static final int HORIZONTAL_BIT_MASK = (1 << HORIZONTAL_SECTION_COUNT) - 1;
    private static final int VERTICAL_BIT_MASK = (1 << VERTICAL_SECTION_COUNT) - 1;

    @Override
    public void setBiome(int x, int z, Biome biome) {
        for (int y = 0; y < VERTICAL_BIT_MASK; ++y) {
            int l = x & HORIZONTAL_BIT_MASK;
            int m = MathHelper.clamp(y, 0, VERTICAL_BIT_MASK);
            int n = z & HORIZONTAL_BIT_MASK;
            int i = m << HORIZONTAL_SECTION_COUNT + HORIZONTAL_SECTION_COUNT
                    | n << HORIZONTAL_SECTION_COUNT
                    | l;

            this.data[i] = biome;
        }
    }
}
