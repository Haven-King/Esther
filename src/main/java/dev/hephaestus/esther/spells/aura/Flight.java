package dev.hephaestus.esther.spells.aura;

import dev.hephaestus.esther.Esther;
import io.github.ladysnake.pal.VanillaAbilities;
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

        Esther.FLIGHT_SPELL.grantTo(player, VanillaAbilities.ALLOW_FLYING);

        player.playSound(SoundEvents.ENTITY_BLAZE_AMBIENT, 1.0f, 0.0f);
    }

    @Override
    protected void deactivate(ServerPlayerEntity player) {
        super.deactivate(player);

        if (Esther.FLIGHT_SPELL.grants(player, VanillaAbilities.ALLOW_FLYING)) {
            Esther.FLIGHT_SPELL.revokeFrom(player, VanillaAbilities.ALLOW_FLYING);
        }

        player.playSound(SoundEvents.ENTITY_BLAZE_DEATH, 1.0f, 0.0f);
        player.inventory.insertStack(new ItemStack(Items.BLAZE_ROD, 12));
    }

    @Override
    public void apply(ServerPlayerEntity player) {
    }
}
