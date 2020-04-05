package dev.hephaestus.esther.mixin.client;

import dev.hephaestus.esther.Esther;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Mutable
    @Final
    @Shadow
    private final MinecraftClient client;

    public InGameHudMixin(MinecraftClient client) {
        this.client = client;
    }

    @ModifyVariable(
        method = "renderExperienceBar(I)V",
        ordinal = 0,
        at = @At(
            value = "STORE"
        )
    )
    public String changeLevelDisplayValue(String variable) {
        assert this.client.player != null;
        return Esther.COMPONENT.get(this.client.player).getMana() + " / " + variable;
    }

    @ModifyVariable(
            method = "renderExperienceBar(I)V",
            ordinal = 2,
            at = @At(
                    value = "STORE"
            )
    )
    public int changeLevelDisplayPosition(int o) {
        return o + 5;
    }

    // Fixes sleeping in an imprint dimension, as
    @Inject(method = "setOverlayMessage(Lnet/minecraft/text/Text;Z)V", at = @At("HEAD"), cancellable = true)
    public void letsBeSafe(Text text, boolean bl, CallbackInfo ci) {
        if (text == null) ci.cancel();
    }
}
