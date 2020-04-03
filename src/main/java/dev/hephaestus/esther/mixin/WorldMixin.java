package dev.hephaestus.esther.mixin;

import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class WorldMixin {
    @Shadow
    private int ambientDarkness;

    @Final
    @Shadow
    public Dimension dimension;

    @Inject(method = "isDay", at = @At("HEAD"), cancellable = true)
    public void isDayInjection(CallbackInfoReturnable<Boolean> ci) {
        if (this.dimension.getType() == DimensionType.OVERWORLD && this.ambientDarkness < 4)
            ci.setReturnValue(true);
    }
}
