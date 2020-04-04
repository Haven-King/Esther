package dev.hephaestus.esther.spells;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;

public class FlightRodsEntityModel<T extends LivingEntity> extends AnimalModel<T> {
    private final ModelPart[] rods;

    public FlightRodsEntityModel() {
        rods = new ModelPart[12];

        for(int i = 0; i < rods.length; ++i) {
            rods[i] = new ModelPart(this, 0, 16);
            rods[i].addCuboid(0.0F, 0.0F, 0.0F, 2.0F, 8.0F, 2.0F);
        }
    }

    @Override
    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return Arrays.asList(rods);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float customAngle, float headYaw, float headPitch) {
        float f = customAngle * 3.1415927F * -0.1F;

        int k;
        for(k = 0; k < 4; ++k) {
            this.rods[k].pivotY = -2.0F + MathHelper.cos(((float)(k * 2) + customAngle) * 0.25F);
            this.rods[k].pivotX = MathHelper.cos(f) * 9.0F;
            this.rods[k].pivotZ = MathHelper.sin(f) * 9.0F;
            ++f;
        }

        f = 0.7853982F + customAngle * 3.1415927F * 0.03F;

        for(k = 4; k < 8; ++k) {
            this.rods[k].pivotY = 2.0F + MathHelper.cos(((float)(k * 2) + customAngle) * 0.25F);
            this.rods[k].pivotX = MathHelper.cos(f) * 7.0F;
            this.rods[k].pivotZ = MathHelper.sin(f) * 7.0F;
            ++f;
        }

        f = 0.47123894F + customAngle * 3.1415927F * -0.05F;

        for(k = 8; k < 12; ++k) {
            this.rods[k].pivotY = 11.0F + MathHelper.cos(((float)k * 1.5F + customAngle) * 0.5F);
            this.rods[k].pivotX = MathHelper.cos(f) * 5.0F;
            this.rods[k].pivotZ = MathHelper.sin(f) * 5.0F;
            ++f;
        }
    }
}
