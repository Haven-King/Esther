package dev.hephaestus.esther.util;

import dev.hephaestus.esther.Esther;
import net.fabricmc.fabric.api.dimension.v1.EntityPlacer;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;

public class ImprintDimensionPlacer implements EntityPlacer {
    private final int imprintId;

    public ImprintDimensionPlacer(int imprintId) {
        this.imprintId = imprintId;
    }

    @Override
    public BlockPattern.TeleportTarget placeEntity(Entity teleported, ServerWorld destination, Direction portalDir, double horizontalOffset, double verticalOffset) {
        if (imprintId == -1 && Esther.DEBUG)
            return new BlockPattern.TeleportTarget(new Vec3d(0,100,0), new Vec3d(0,0,0), 0);
        BlockPos destinationPosition;

        ImprintManager.Imprint imprint = ImprintManager.getInstance(destination).getImprintByID(imprintId);

        imprint.build(destination);

        destinationPosition = imprint.getCenter();

        destinationPosition = destination.getTopPosition(Heightmap.Type.MOTION_BLOCKING, destinationPosition);

        return new BlockPattern.TeleportTarget(new Vec3d(destinationPosition), new Vec3d(0,0,0), 0);
    }
}
