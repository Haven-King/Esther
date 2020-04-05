package dev.hephaestus.esther.client;

import dev.hephaestus.esther.Esther;
import dev.hephaestus.esther.spells.aura.Aura;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;

public class FlightRodsFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    private static final Identifier SKIN = new Identifier("textures/entity/blaze.png");
    private final FlightRodsEntityModel<T> rodsModel = new FlightRodsEntityModel<>();

    public FlightRodsFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, T livingEntity, float f, float g, float h, float j, float k, float l) {
        if (Esther.COMPONENT.get(livingEntity).isActive((Aura) Esther.FLIGHT)) {
            matrixStack.push();
            matrixStack.translate(-0.0725, 0.0D, 0.0D);
            this.getContextModel().copyStateTo(this.rodsModel);

            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(rodsModel.getLayer(SKIN));

            this.rodsModel.setAngles(livingEntity, f, g, j, k, l);
            this.rodsModel.render(matrixStack, vertexConsumer, LightmapTextureManager.pack(15, livingEntity.world.getLightLevel(LightType.SKY, new BlockPos(livingEntity.getCameraPosVec(j)))), OverlayTexture.DEFAULT_UV,  1, 1, 1, 1);
            matrixStack.pop();
        }
    }


}
