package dev.hephaestus.esther.util;

import dev.hephaestus.esther.Esther;
import dev.hephaestus.esther.spells.aura.Aura;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import nerdhub.cardinal.components.api.util.sync.EntitySyncedComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

interface IntComponent extends Component {
    int getMana();
    void setMana(int value);
    void useMana(int value);
    void resetMana();
}

public class EstherComponent implements IntComponent, EntitySyncedComponent {
    private int value = 0;
    private PlayerEntity player;
    private Map<Identifier, Boolean> aura = new HashMap<>();

    public EstherComponent(PlayerEntity player) {
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
    public void resetMana() {
        int using = 0;
        for (Map.Entry<Identifier, Boolean> entry : this.aura.entrySet()) {
            if (entry.getValue()) {
                using += Esther.SPELLS.get(entry.getKey()).getCost();
            }
        }

        setMana(player.experienceLevel - using);
    }

    public void regainMana() {
        int using = 0;
        for (Map.Entry<Identifier, Boolean> entry : this.aura.entrySet()) {
            if (entry.getValue()) {
                using += Esther.SPELLS.get(entry.getKey()).getCost();
            }
        }

        setMana(Integer.min(value + 1, player.experienceLevel - using));
    }

    @Override
    public void fromTag(CompoundTag tag) {
        this.setMana(tag.getInt(Esther.MOD_ID + "_mana"));

        CompoundTag aura = tag.getCompound(Esther.MOD_ID + "_aura");

        for (Aura a : Esther.SPELLS.getRegisteredAura()) {
            this.aura.put(a.getId(), false);
        }

        for (String t : aura.getKeys()) {
            this.aura.put(new Identifier(t), aura.getBoolean(t));
        }
    }

    @NotNull
    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putInt(Esther.MOD_ID + "_mana", this.value);

        CompoundTag aura = new CompoundTag();

        for (Map.Entry<Identifier, Boolean> entry : this.aura.entrySet()) {
            aura.putBoolean(entry.getKey().toString(), entry.getValue());
        }

        tag.put(Esther.MOD_ID + "_aura", aura);

        return tag;
    }

    @NotNull
    @Override
    public Entity getEntity() {
        return this.player;
    }

    @NotNull
    @Override
    public ComponentType<?> getComponentType() {
        return Esther.COMPONENT;
    }

    public boolean isActive(Identifier id) {
        return this.aura.getOrDefault(id, false);
    }
    public boolean isActive(Aura aura) {return isActive(aura.getId());}

    public void activate(Identifier id) {
        aura.put(id, true);
        this.sync();
    }

    public void activate(Aura aura) {
        activate(aura.getId());
    }

    public void deactivate(Identifier id) {
        aura.put(id, false);
        this.sync();
    }

    public void deactivate(Aura aura) {
        deactivate(aura.getId());
    }
}
