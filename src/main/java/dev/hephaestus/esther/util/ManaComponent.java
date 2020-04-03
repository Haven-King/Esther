package dev.hephaestus.esther.util;

import dev.hephaestus.esther.Esther;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import nerdhub.cardinal.components.api.util.sync.EntitySyncedComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

interface IntComponent extends Component {
    int getMana();
    void setMana(int value);
    void useMana(int value);
}

public class ManaComponent implements IntComponent, EntitySyncedComponent {
    private int value = 0;
    private PlayerEntity player;

    public ManaComponent(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public int getMana() {
        return this.value;
    }

    @Override
    public void setMana(int value) {
        this.value = value;
        this.sync();
    }

    @Override
    public void useMana(int value) {
        this.setMana(Integer.max(0, this.value - value));
    }

    @Override
    public void fromTag(CompoundTag tag) {
        this.setMana(tag.getInt("mana"));
    }

    @NotNull
    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putInt("mana", this.value);
        return tag;
    }

    @NotNull
    @Override
    public Entity getEntity() {
        return this.player;
    }

    @Override
    public ComponentType<?> getComponentType() {
        return Esther.MANA;
    }
}
