package dev.hephaestus.esther.spells;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.potion.Potions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;

public class Venom extends Spell {
    public Venom(Identifier id, Difficulty difficulty, int cost) {
        super(id, difficulty, cost);
    }

    @Override
    void cast(ServerPlayerEntity player) {
        EntityHitResult hit = Spell.traceForEntity(player, 10);

        if (hit.getEntity() instanceof LivingEntity) {
            ((LivingEntity)hit.getEntity()).addStatusEffect(
                    new StatusEffectInstance(StatusEffects.POISON, 432, 1)
            );
        }
    }
}
