package dev.hephaestus.esther;

import dev.hephaestus.esther.util.ManaUser;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.render.RenderLayer;

import static dev.hephaestus.esther.Esther.SPACE;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientSidePacketRegistry.INSTANCE.register(
            Esther.UPDATE_MANA_PACKET_ID,
                ((ctx, buf) -> {
                    ManaUser player = ((ManaUser) ctx.getPlayer());
                    int mana = buf.readInt();
                    ctx.getTaskQueue().execute(() -> {
                        if (player != null) player.setMana(mana);
                    });
                })
        );
        BlockRenderLayerMap.INSTANCE.putBlock(SPACE, RenderLayer.getCutout());
    }
}