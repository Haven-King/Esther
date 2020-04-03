package dev.hephaestus.esther.mixin;

import dev.hephaestus.esther.Esther;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Shadow public int experienceLevel;
//    int manaLevel;
//
//    @Inject(method = "readCustomDataFromTag", at = @At(value =  "TAIL"))
//    private void readCustomDataFromTag(CompoundTag tag, CallbackInfo ci) {
//        if (tag.contains("manaLevel")) {
//            this.manaLevel = tag.getInt("manaLevel");
//        } else {
//            this.manaLevel = this.experienceLevel;
//        }
//    }
//
//    @Inject(method = "writeCustomDataToTag", at = @At(value = "TAIL"))
//    private void writeCustomDataToTag(CompoundTag tag, CallbackInfo ci) {
//        tag.putInt("manaLevel", this.manaLevel);
//    }

    @Inject(method = "addExperienceLevels", at = @At(value = "TAIL"))
    private void addExperienceLevels(int levels, CallbackInfo ci) {
        Esther.MANA.get((PlayerEntity)(Object)this).setMana(this.experienceLevel);
    }
}
