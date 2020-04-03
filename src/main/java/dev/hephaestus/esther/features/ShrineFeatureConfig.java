package dev.hephaestus.esther.features;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import dev.hephaestus.esther.Esther;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;

import java.util.Objects;

public class ShrineFeatureConfig extends DefaultFeatureConfig {
    private final Block topBlock;
    private final Block bottomBlock;
    private final Biome biome;

    public ShrineFeatureConfig(Biome biome) {
        this.biome = biome;
        this.bottomBlock = Registry.BLOCK.get(Esther.newID(Objects.requireNonNull(Registry.BIOME.getId(biome)).getPath() + "_shrine_bottom"));
        this.topBlock = Registry.BLOCK.get(Esther.newID(Objects.requireNonNull(Registry.BIOME.getId(biome)).getPath() + "_shrine_top"));
    }

    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        return new Dynamic<>(ops, ops.createString(
                Objects.requireNonNull(Registry.BIOME.getId(this.biome)).toString()
        ));
    }

    public static <T> ShrineFeatureConfig deserialize(Dynamic<T> dynamic) {
        Biome biome = Registry.BIOME.get(new Identifier(dynamic.get("biome").toString()));
        return new ShrineFeatureConfig(biome);
    }

    public Block getTopBlock() {
        return topBlock;
    }

    public Block getBottomBlock() {
        return bottomBlock;
    }
}
