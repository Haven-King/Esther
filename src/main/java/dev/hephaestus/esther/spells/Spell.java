package dev.hephaestus.esther.spells;

import dev.hephaestus.esther.Esther;
import dev.hephaestus.esther.util.ManaManager;
import dev.hephaestus.esther.util.ManaUser;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.ProjectileUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;
import sun.jvm.hotspot.opto.Block;

public abstract class Spell {
    public static enum Difficulty {
        TRIVIAL,
        EASY,
        MODERATE,
        HARD,
        IMPOSSIBLE
    }

    private final Difficulty difficulty;
    private final Identifier id;
    protected final int cost;
    private SoundEvent sound;

    public Spell(Identifier id, Difficulty difficulty, int cost) {
        this.difficulty = difficulty;
        this.id = id;
        this.cost = cost;
    }

    abstract void cast(ServerPlayerEntity player);

    public boolean canCast(ServerPlayerEntity player) {
        return Esther.MANA.get(player).getMana() >= this.cost;
    }

    public ActionResult castIfCapable(ServerPlayerEntity player) {
        if (canCast(player)) {
            Esther.log(player.getName().asString() + " cast " + id);
            Esther.MANA.get(player).useMana(this.cost);
            if (this.sound != null) player.playSound(sound, SoundCategory.AMBIENT, 1.0f, 0.0f);
            this.cast(player);
            return ActionResult.SUCCESS;
        } else {
            Esther.log(player.getName().asString() + " tried to cast " + id);
            switch(this.difficulty) {

            }
            return ActionResult.PASS;
        }
    }

    public void withSound(SoundEvent sound) {
        this.sound = sound;
    }

    public Identifier getId() {
        return this.id;
    }

    private static Vec3d getLooking(ServerPlayerEntity player) {
        float f = -MathHelper.sin(player.yaw * 0.017453292F) * MathHelper.cos(player.pitch * 0.017453292F);
        float g = -MathHelper.sin(player.pitch * 0.017453292F);
        float h = MathHelper.cos(player.yaw * 0.017453292F) * MathHelper.cos(player.pitch * 0.017453292F);

        return new Vec3d(f,g,h);
    }

    protected static BlockHitResult traceForBlock(ServerPlayerEntity player, int range) {
        BlockHitResult hit = player.world.rayTrace(new RayTraceContext(
                player.getCameraPosVec(1.0f),
                player.getCameraPosVec(1.0f).add(getLooking(player).multiply(range)),
                RayTraceContext.ShapeType.OUTLINE, RayTraceContext.FluidHandling.NONE, player
        ));

        return hit;
    }

    protected static EntityHitResult traceForEntity(ServerPlayerEntity player, int range) {
        Vec3d vec3d2 = player.getRotationVec(1.0F);

        EntityHitResult hit = ProjectileUtil.getEntityCollision(
                player.world,
                null,
                player.getCameraPosVec(1.0f),
                player.getCameraPosVec(1.0f).add(getLooking(player).multiply(range)),
                player.getCameraEntity().getBoundingBox().stretch(vec3d2.multiply(range)).expand(1.0D),
                (entity) -> !entity.isSpectator() && entity.isAlive() && entity.collides()
        );

        return hit;
    }
}
