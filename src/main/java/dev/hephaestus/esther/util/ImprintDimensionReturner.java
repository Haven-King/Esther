package dev.hephaestus.esther.util;

import net.fabricmc.fabric.api.dimension.v1.EntityPlacer;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class ImprintDimensionReturner implements EntityPlacer {
    private final BlockPos pos;

    public ImprintDimensionReturner(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public BlockPattern.TeleportTarget placeEntity(Entity teleported, ServerWorld destination, Direction portalDir, double horizontalOffset, double verticalOffset) {
        return new BlockPattern.TeleportTarget(new Vec3d(pos), new Vec3d(0,0,0), 0);
    }
}
