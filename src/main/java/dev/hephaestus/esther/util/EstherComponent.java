package dev.hephaestus.esther.util;

import dev.hephaestus.esther.Esther;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import nerdhub.cardinal.components.api.util.sync.EntitySyncedComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import org.apache.logging.log4j.core.jmx.Server;
import org.jetbrains.annotations.NotNull;

interface IntComponent extends Component {
    int getMana();
    void setMana(int value);
    void useMana(int value);
    void resetMana();
}

interface BoolComponent extends Component {
    boolean canFly();
    void setCanFly(boolean active);
}

public class EstherComponent implements IntComponent, BoolComponent, EntitySyncedComponent {
    private int value = 0;
    private boolean flying = false;
    private PlayerEntity player;

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
        this.setMana(player.experienceLevel - (this.flying ? 15 : 0));
    }

    @Override
    public void fromTag(CompoundTag tag) {
        this.setMana(tag.getInt(Esther.MOD_ID + "_mana"));
        this.setCanFly(tag.getBoolean(Esther.MOD_ID + "_flying"));
    }

    @NotNull
    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putInt(Esther.MOD_ID + "_mana", this.value);
        tag.putBoolean(Esther.MOD_ID + "_flying", this.flying);
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

    @Override
    public boolean canFly() {
        return this.flying;
    }

    @Override
    public void setCanFly(boolean active) {
        this.flying = active;

        if (player instanceof ServerPlayerEntity && !player.isCreative()) {
            player.abilities.allowFlying = active;
            if (!active) player.abilities.flying = false;
        } else if (player instanceof ClientPlayerEntity && MinecraftClient.getInstance().interactionManager.getCurrentGameMode() != GameMode.CREATIVE) {
            player.abilities.allowFlying = active;
            if (!active) player.abilities.flying = false;
        }

        player.sendAbilitiesUpdate();
        this.sync();
    }
}
