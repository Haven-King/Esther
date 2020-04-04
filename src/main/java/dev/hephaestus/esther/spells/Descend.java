package dev.hephaestus.esther.spells;

import dev.hephaestus.esther.Esther;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class Descend extends Spell {
    public Descend(Identifier id, Difficulty difficulty, int cost) {
        super(id, difficulty, cost);
    }

    @Override
    void cast(ServerPlayerEntity player) {
        Esther.COMPONENT.get(player).setCanFly(false);
        player.inventory.insertStack(new ItemStack(Items.BLAZE_ROD, 12));
    }

    @Override
    public boolean canCast(ServerPlayerEntity player) {
        return super.canCast(player) && Esther.COMPONENT.get(player).canFly();
    }
}
