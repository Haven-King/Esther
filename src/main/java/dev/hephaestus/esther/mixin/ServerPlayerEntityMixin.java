package dev.hephaestus.esther.mixin;

import com.mojang.authlib.GameProfile;
import dev.hephaestus.esther.Esther;
import dev.hephaestus.esther.EstherDimensions;
import dev.hephaestus.esther.spells.aura.Aura;
import dev.hephaestus.esther.util.ImprintManager;
import io.github.ladysnake.pal.VanillaAbilities;
import net.minecraft.command.arguments.EntityAnchorArgumentType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    @Shadow public abstract void lookAt(EntityAnchorArgumentType.EntityAnchor anchorPoint, Vec3d target);

    @Shadow public abstract void requestTeleport(double destX, double destY, double destZ);

    @Shadow public abstract ServerWorld getServerWorld();

    public ServerPlayerEntityMixin(World world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "wakeUp", at = @At("TAIL"))
    public void wakeUpHook(boolean bl, boolean bl2, CallbackInfo ci) {
        Esther.COMPONENT.get(this).resetMana();
    }

    @Inject(method = "onDeath", at = @At(value = "HEAD"), cancellable = true)
    public void onDeathDont(DamageSource source, CallbackInfo ci) {
        ServerPlayerEntity player = ((ServerPlayerEntity)(Object)this);
        if (player.dimension == EstherDimensions.IMPRINT) {
            BlockPos pos = ImprintManager.getInstance(player.getServerWorld()).getClosestImprint(player.getBlockPos()).getCenter();
            BlockPos respawnPos = player.getServerWorld().getTopPosition(Heightmap.Type.MOTION_BLOCKING, pos);
            player.requestTeleport(respawnPos.getX(), respawnPos.getY() + 1, respawnPos.getZ());
            player.setHealth(1);
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    public void estherTick(CallbackInfo ci) {
        if (this.touchingWater && Esther.COMPONENT.get(this).isActive((Aura) Esther.FLIGHT)) {
            Esther.COMPONENT.get(this).deactivate((Aura) Esther.FLIGHT);
            if (Esther.FLIGHT_SPELL.grants(this, VanillaAbilities.ALLOW_FLYING)) {
                Esther.FLIGHT_SPELL.revokeFrom(this, VanillaAbilities.ALLOW_FLYING);
            }
            this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.AMBIENT, 1.0f, 0.0f);
        }

        if (this.world.getTime() % 80L == 0L) {
            for (Aura a : Esther.SPELLS.getRegisteredAura()) {
                if (Esther.COMPONENT.get(this).isActive(a.getId())) a.apply((ServerPlayerEntity)(Object)this);
            }
        }

        if (this.dimension == EstherDimensions.IMPRINT) {
            ImprintManager.Imprint closest = ImprintManager.getInstance(this.getServerWorld()).getClosestImprint(this.getBlockPos());
            BlockPos center = closest.getCenter();
            BlockPos pos = this.getBlockPos();

//            int dx = 0, dy = 0, dz = 0;
//            int hDistance = 200;
//            int vDistance = 50;
//            if (pos.getX() > center.getX() + hDistance) dx = -(2 * hDistance);
//            if (pos.getX() < center.getX() - hDistance) dx = (2 * hDistance);
//
//            if (pos.getY() > center.getY() + vDistance) dy = -(2 * vDistance);
//            if (pos.getY() < center.getY() - vDistance) dy = (2 * vDistance);
//
//            if (pos.getZ() > center.getZ() + hDistance) dz = -(2 * hDistance);
//            if (pos.getZ() < center.getZ() - hDistance) dz = (2 * hDistance);

            int dx = pos.getX() - center.getX();
            int dy = pos.getY() - center.getY();
            int dz = pos.getZ() - center.getZ();

            BlockPos newPos = center.subtract(new BlockPos(dx, dy, dz));

            if (center.getManhattanDistance(new BlockPos(pos.getX(), center.getY(), pos.getZ())) > 200) {
                this.teleport(newPos.getX(), newPos.getY(), newPos.getZ());
                this.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, new Vec3d(center));
            }
        }

        if (this.world.getTime() % 200L == 0L) {
            Esther.COMPONENT.get(this).regainMana();
        }
    }

    @Shadow
    @Override
    public boolean isSpectator() {
        return false;
    }

    @Shadow
    @Override
    public boolean isCreative() {
        return false;
    }
}
