package dev.hephaestus.esther.spells;

import dev.hephaestus.esther.Esther;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class Flight extends Spell {
    public Flight(Identifier id, Difficulty difficulty, int cost) {
        super(id, difficulty, cost);
    }

    @Override
    void cast(ServerPlayerEntity player) {
        Esther.COMPONENT.get(player).setCanFly(true);

        int n = 12;
        for (ItemStack stack : player.inventory.main) {
            if (n < 1) {
                break;
            } else if (stack.getItem() == Items.BLAZE_ROD) {
                if (stack.getCount() >= 12) {
                    stack.decrement(12);
                } else {
                    n -= stack.getCount();
                    stack.setCount(0);
                }
            }
        }
    }

    @Override
    public boolean canCast(ServerPlayerEntity player) {
        return super.canCast(player) && !Esther.COMPONENT.get(player).canFly() && player.inventory.contains(new ItemStack(Items.BLAZE_ROD, 12));
    }
}
