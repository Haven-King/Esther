package dev.hephaestus.esther.dims;

import dev.hephaestus.esther.EstherDimensions;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.OverworldDimension;

public class OverworldSourceDimension extends OverworldDimension {
    public OverworldSourceDimension(World world, DimensionType type) {
        super(world, type);
    }

    @Override
    public DimensionType getType() {
        return EstherDimensions.OVERWORLD_SOURCE;
    }
}
