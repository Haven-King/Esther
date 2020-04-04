package dev.hephaestus.esther.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(ExplosiveProjectileEntity.class)
public class ExplosiveProjectileEntityMixin {
    @Shadow public LivingEntity owner;
    
    @Redirect(method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/entity/LivingEntity;DDDLnet/minecraft/world/World;)V", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextGaussian()D"))
    public double doThing(Random random) {
        if (this.owner instanceof PlayerEntity) return 0;
        else return random.nextGaussian();
    }
}
