package dev.hephaestus.esther.mixin;

import dev.hephaestus.esther.util.ManaUser;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin implements ManaUser {
    private int manaLevel;

    @Override
    public int getManaLevel() {
        return this.manaLevel;
    }

    @Override
    public void setMana(int mana) {
        this.manaLevel = mana;
    }

    @Override
    public void useMana(int mana) {
        this.manaLevel = Integer.max(0, this.manaLevel - mana);
    }

    @Override
    public void resetMana() {

    }
}
