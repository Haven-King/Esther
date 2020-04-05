package dev.hephaestus.esther.spells.aura;

import dev.hephaestus.esther.Esther;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class EffectAura extends Aura {
    final StatusEffect effect;
    protected ItemStack components;

    public EffectAura(Identifier id, Difficulty difficulty, int cost, StatusEffect effect) {
        super(id, difficulty, cost);
        this.effect = effect;
    }

    public EffectAura(Identifier id, Difficulty difficulty, int cost, StatusEffect effect, ItemStack components) {
        super(id, difficulty, cost);
        this.effect = effect;
        this.components = components;
    }


    public void apply(ServerPlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(effect, 200, Integer.min(2, (cost - 10) / 10)));
    }

    @Override
    protected void activate(ServerPlayerEntity player) {
        super.activate(player);

        if (player.getMainHandStack().getItem() == Items.BLAZE_ROD && player.getMainHandStack().getCount() >= components.getCount()) {
            player.getMainHandStack().setCount(player.getMainHandStack().getCount() - components.getCount());
        } else if (player.getOffHandStack().getItem() == Items.BLAZE_ROD && player.getOffHandStack().getCount() >= components.getCount()) {
            player.getOffHandStack().setCount(player.getMainHandStack().getCount() - components.getCount());
        }

        apply(player);
    }

    @Override
    protected void deactivate(ServerPlayerEntity player) {
        super.deactivate(player);

        player.removeStatusEffect(StatusEffects.HASTE);
    }

    @Override
    protected boolean canActivate(ServerPlayerEntity player) {
        return !Esther.COMPONENT.get(player).isActive(this) && (
                (player.getMainHandStack().getItem() == components.getItem() && player.getMainHandStack().getCount() >= components.getCount()) ||
                (player.getOffHandStack().getItem() == components.getItem() && player.getOffHandStack().getCount() >= components.getCount())
        );
    }
}
