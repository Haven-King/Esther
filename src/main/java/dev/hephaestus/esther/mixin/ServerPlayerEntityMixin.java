package dev.hephaestus.esther.mixin;

import com.mojang.authlib.GameProfile;
import dev.hephaestus.esther.Esther;
import dev.hephaestus.esther.util.ManaManager;
import dev.hephaestus.esther.util.ManaUser;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    @Shadow public abstract ServerWorld getServerWorld();

    public ServerPlayerEntityMixin(World world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "wakeUp", at = @At("TAIL"))
    public void wakeUpHook(boolean bl, boolean bl2, CallbackInfo ci) {
        Esther.debug("We woke!");
        ((ManaUser) this).resetMana();
        Esther.debug("" + ((ManaUser)this).getManaLevel());
//        ManaManager.getInstance(this.getServerWorld()).resetMana(this.getServerWorld().getServer().getPlayerManager().getPlayer(this.uuid));
    }
//
//    @Inject(method = "createSpawnPacket", at = @At("HEAD"))
//    public void updateMana(CallbackInfoReturnable<Packet<?>> cir) {
//        ManaManager.getInstance(this.getServerWorld()).resetMana(this.getServerWorld().getServer().getPlayerManager().getPlayer(this.uuid));
//    }

    @Inject(method = "addExperienceLevels", at = @At("TAIL"))
    public void resetMana(int levels, CallbackInfo ci) {
        ((ManaUser) this).resetMana();
//        ManaManager.getInstance(this.getServerWorld()).resetMana(this.getServerWorld().getServer().getPlayerManager().getPlayer(this.uuid));
    }
//
//    @Inject(method = "tick", at = @At("TAIL"))
//    private void whatIsMana(CallbackInfo ci) {
//        Esther.debug("" + ((ManaUser)this).getManaLevel());
//    }



//    @Environment(EnvType.SERVER)
//    @Inject(method = "<init>", at = @At("TAIL"))
//    public void initInjection(MinecraftServer server, ServerWorld world, GameProfile profile, ServerPlayerInteractionManager interactionManager, CallbackInfo ci) {
//        ManaManager.getInstance(this.getServerWorld()).update((ServerPlayerEntity)(Object)this);
//    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCreative() {
        return false;
    }
}
