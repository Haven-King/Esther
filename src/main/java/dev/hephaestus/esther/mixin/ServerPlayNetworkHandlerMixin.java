package dev.hephaestus.esther.mixin;

import dev.hephaestus.esther.Esther;
import dev.hephaestus.esther.spells.Spell;
import dev.hephaestus.esther.util.ManaManager;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "onChatMessage(Lnet/minecraft/network/packet/c2s/play/ChatMessageC2SPacket;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getPlayerManager()Lnet/minecraft/server/PlayerManager;"), cancellable = true)
    public void castSpell(ChatMessageC2SPacket packet, CallbackInfo ci) {
        String string = packet.getChatMessage();
        Spell spell = Esther.SPELLS.get(string).getValue();

        if (spell != null) {
            spell.castIfCapable(player);
        }
    }
//
//    @Inject(method = "<init>", at = @At("TAIL"))
//    public void updateMana(MinecraftServer minecraftServer, ClientConnection clientConnection, ServerPlayerEntity serverPlayerEntity, CallbackInfo ci) {
//        ManaManager.getInstance(minecraftServer.getWorld(DimensionType.OVERWORLD)).update(serverPlayerEntity);
//    }
}
