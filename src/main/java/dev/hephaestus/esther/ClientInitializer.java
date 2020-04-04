package dev.hephaestus.esther;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

import static dev.hephaestus.esther.Esther.SPACE;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(SPACE, RenderLayer.getCutout());
    }
}