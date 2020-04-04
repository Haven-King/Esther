package dev.hephaestus.esther.mixin;

import dev.hephaestus.esther.Esther;
import dev.hephaestus.esther.spells.Spell;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow public ServerPlayerEntity player;
    @Shadow private boolean floating;

    @Inject(method = "onChatMessage(Lnet/minecraft/network/packet/c2s/play/ChatMessageC2SPacket;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getPlayerManager()Lnet/minecraft/server/PlayerManager;"), cancellable = true)
    public void castSpell(ChatMessageC2SPacket packet, CallbackInfo ci) {
        String string = packet.getChatMessage();
        Spell spell = Esther.SPELLS.get(string).getValue();

        if (spell != null) {
            spell.castIfCapable(player);
        }
    }

    @Inject(method = "onDisconnected", at = @At("HEAD"))
    public void disconnect(Text reason, CallbackInfo ci) {
        Esther.COMPONENT.get(this.player).setCanFly(false);
        player.inventory.insertStack(new ItemStack(Items.BLAZE_ROD, 12));
    }

    @Inject(method = "tick()V", at = @At("HEAD"))
    public void dontFloatMan(CallbackInfo ci) {
        if (Esther.COMPONENT.get(player).canFly()) this.floating = false;
    }
}
