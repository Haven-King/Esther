package dev.hephaestus.esther.util;

import dev.hephaestus.esther.Esther;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.world.PersistentState;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// Sidebar: I love the name of this class
public class ManaManager extends PersistentState {
    private static final String SAVE_KEY = Esther.MOD_ID + "_mana";
    private final HashMap<UUID, Integer> mana = new HashMap<>();
    private final ServerWorld world;

    public ManaManager(ServerWorld world) {
        super(SAVE_KEY);
        this.world = world.getServer().getWorld(DimensionType.OVERWORLD);
    }

    public static ManaManager getInstance(ServerWorld world) {
        if (world.getDimension().getType() != DimensionType.OVERWORLD) {
            world = world.getServer().getWorld(DimensionType.OVERWORLD);
        }

        final ServerWorld serverWorld = world;

        return serverWorld.getPersistentStateManager().getOrCreate(() ->
                new ManaManager(serverWorld), SAVE_KEY);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        mana.clear();

        CompoundTag manaTag = tag.getCompound(SAVE_KEY);

        for (String key : manaTag.getKeys()) {
            mana.put(UUID.fromString(key), manaTag.getInt(key));
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        CompoundTag manaTag = new CompoundTag();


        for (Map.Entry<UUID, Integer> entry : mana.entrySet()) {
            manaTag.putInt(entry.getKey().toString(), entry.getValue());
        }

        tag.put(SAVE_KEY, manaTag);

        return tag;
    }

    // Accessors!
    public int getMana(ServerPlayerEntity player) {
        return getMana(player.getUuid());
    }

    public int getMana(UUID player) {
        return mana.getOrDefault(player, 0);
    }

    // Networking! Yay!
    public void update(ServerPlayerEntity player) {
        Esther.log(player.getUuidAsString() + " ~ " + getMana(player));

        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(getMana(player));

        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, Esther.UPDATE_MANA_PACKET_ID, buf);
    }

    // Modifiers! WOOHOOOO!!!
    public void setMana(ServerPlayerEntity player, int amount) {
        mana.put(player.getUuid(), amount);
        update(player);
        this.markDirty();
    }

    public void useMana(ServerPlayerEntity player, int amount) {
        setMana(player, getMana(player) - amount);
    }

    public void resetMana(ServerPlayerEntity player) {
        setMana(player, player.experienceLevel);
    }
}
