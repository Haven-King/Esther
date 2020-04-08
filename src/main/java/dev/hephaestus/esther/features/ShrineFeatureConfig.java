package dev.hephaestus.esther.features;

import com.google.common.collect.ImmutableMap;
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
    private final String name;

    public ShrineFeatureConfig(Biome biome, String name) {
        this.biome = biome;
        this.name = name;
        this.bottomBlock = Registry.BLOCK.get(Esther.newID(name + "_shrine_bottom"));
        this.topBlock = Registry.BLOCK.get(Esther.newID(name + "_shrine_top"));
    }

    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        com.google.common.collect.ImmutableMap.Builder<T, T> builder = ImmutableMap.builder();
        builder.put(ops.createString("biome"), ops.createString(Objects.requireNonNull(Registry.BIOME.getId(this.biome)).toString()));
        builder.put(ops.createString("name"), ops.createString(name));
        Dynamic<T> dynamic = new Dynamic<>(ops, ops.createMap(builder.build()));
        return dynamic.merge(super.serialize(ops));
    }

    public static <T> ShrineFeatureConfig deserialize(Dynamic<T> dynamic) {
        Biome biome = Registry.BIOME.get(new Identifier(dynamic.get("biome").toString()));
        String name = dynamic.get("name").toString();
        return new ShrineFeatureConfig(biome, name);
    }

    public Block getTopBlock() {
        return topBlock;
    }

    public Block getBottomBlock() {
        return bottomBlock;
    }
}
