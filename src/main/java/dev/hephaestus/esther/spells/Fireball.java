package dev.hephaestus.esther.spells;

import dev.hephaestus.esther.Esther;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Fireball extends Spell {
    public Fireball(Identifier id, Difficulty difficulty, int cost) {
        super(id, difficulty, cost);
    }

    @Override
    void cast(ServerPlayerEntity player) {
        Vec3d vec3d = player.getRotationVec(1.0F);
        float f = -MathHelper.sin(player.yaw * 0.017453292F) * MathHelper.cos(player.pitch * 0.017453292F);
        float g = -MathHelper.sin(player.pitch * 0.017453292F);
        float h = MathHelper.cos(player.yaw * 0.017453292F) * MathHelper.cos(player.pitch * 0.017453292F);
        player.world.playLevelEvent(null, 1016, new BlockPos(player), 0);
        FireballEntity fireballEntity = new FireballEntity(player.world, player, f, g, h);
        fireballEntity.explosionPower = 1 + player.experienceLevel / 15;
        fireballEntity.updatePosition(player.getX() + vec3d.x, player.getBodyY(0.5D) + 0.25D, player.getZ() + vec3d.z);
        player.world.spawnEntity(fireballEntity);
    }
}
