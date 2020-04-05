package dev.hephaestus.esther.spells.aura;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class Flight extends EffectAura {
    public Flight(Identifier id, Difficulty difficulty, int cost, ItemStack components) {
        super(id, difficulty, cost, null, components);
    }

    @Override
    protected void activate(ServerPlayerEntity player) {
        super.activate(player);

        player.abilities.allowFlying = true;

        player.playSound(SoundEvents.ENTITY_BLAZE_AMBIENT, 1.0f, 0.0f);
        player.sendAbilitiesUpdate();
    }

    @Override
    protected void deactivate(ServerPlayerEntity player) {
        super.deactivate(player);

        if (!player.isCreative()) {
            player.abilities.allowFlying = false;
            player.abilities.flying = false;
            player.sendAbilitiesUpdate();
        }

        player.playSound(SoundEvents.ENTITY_BLAZE_DEATH, 1.0f, 0.0f);
        player.inventory.insertStack(new ItemStack(Items.BLAZE_ROD, 12));
    }

    @Override
    public void apply(ServerPlayerEntity player) {
    }
}
