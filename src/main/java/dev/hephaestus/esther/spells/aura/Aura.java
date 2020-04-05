package dev.hephaestus.esther.spells.aura;

import dev.hephaestus.esther.Esther;
import dev.hephaestus.esther.spells.Spell;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public abstract class Aura extends Spell {
    public Aura(Identifier id, Difficulty difficulty, int cost) {
        super(id, difficulty, cost);
    }

    protected void activate(ServerPlayerEntity player) {
        Esther.COMPONENT.get(player).activate(this.id);
    }

    protected void deactivate(ServerPlayerEntity player) {
        Esther.COMPONENT.get(player).deactivate(this.id);
    }

    protected abstract boolean canActivate(ServerPlayerEntity player);

    public void apply(ServerPlayerEntity player) {

    }

    @Override
    public void cast(ServerPlayerEntity player) {
        if (Esther.COMPONENT.get(player).isActive(this.id)) {
            deactivate(player);
        } else if (this.canCast(player)) {
            super.cast(player);
            activate(player);
        }
    }

    @Override
    public boolean canCast(ServerPlayerEntity player) {
        return Esther.COMPONENT.get(player).isActive(this.id) || (super.canCast(player) && canActivate(player));
    }
}
