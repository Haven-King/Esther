package dev.hephaestus.esther.util;

import dev.hephaestus.esther.Esther;
import dev.hephaestus.esther.EstherDimensions;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import jdk.internal.jline.internal.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashMap;
import java.util.Map;

public class ImprintManager extends PersistentState {
    private static final String SAVE_KEY = Esther.MOD_ID + "_imprints";
    private final ServerWorld world;
    private final Int2ObjectMap<Imprint> imprints = new Int2ObjectArrayMap<>();

    public static ImprintManager getInstance(ServerWorld world) {
        if (world.getDimension().getType() != DimensionType.OVERWORLD) {
            world = world.getServer().getWorld(DimensionType.OVERWORLD);
        }

        final ServerWorld serverWorld = world;

        return serverWorld.getPersistentStateManager().getOrCreate(() ->
                new ImprintManager(serverWorld), SAVE_KEY);
    }

    public ImprintManager(ServerWorld world) {
        super(SAVE_KEY);
        this.world = world;
        this.markDirty();
    }

    public Imprint getNextImprint() {
        final Imprint imprint = new Imprint(imprints.size());
        imprints.put(imprint.id, imprint);
        this.markDirty();
        return imprint;
    }

    public Imprint getImprintByID(int id) {
        if (!imprints.containsKey(id)) {
            throw new RuntimeException("Could not find Imprint with id: " + id);
        }

        return imprints.get(id);
    }

    @Nullable
    public Imprint getClosestImprint(BlockPos pos) {
        return imprints.getOrDefault(Math.max(pos.getX() / 1000, imprints.size() - 1), null);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        imprints.clear();
        CompoundTag imprintsTag = tag.getCompound(SAVE_KEY);
        for (String key : imprintsTag.getKeys()) {
            int id = Integer.parseInt(key);
            imprints.put(id, new Imprint(id, imprintsTag.getCompound(key)));
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        CompoundTag imprintsTag = new CompoundTag();

        for (Int2ObjectMap.Entry<Imprint> entry : imprints.int2ObjectEntrySet()) {
            imprintsTag.put(entry.getIntKey() + "", entry.getValue().toTag());
        }

        tag.put(SAVE_KEY, imprintsTag);

        return tag;
    }

    public static class Imprint {
        private final int id;
        private BlockPos originPosition;
        private DimensionType originDimension;
        private boolean built = false;

        public Imprint(int id) {
            this.id = id;
        }

        public Imprint(int id, CompoundTag tag) {
            this(id);
            fromTag(tag);
        }

        public void update(DimensionType dimension, BlockPos origin) {
            this.originPosition = origin;

            if (dimension == DimensionType.OVERWORLD) {
                this.originDimension = EstherDimensions.OVERWORLD_SOURCE;
            }
        }

        public BlockPos getCenter() {
            return new BlockPos(400 * (id % 1000) - originPosition.getX() % 16, originPosition.getY(), - originPosition.getZ() % 16);
        }

        public void fromTag(CompoundTag tag) {
            this.originPosition = BlockPos.fromLong(tag.getLong("origin"));
            this.originDimension = DimensionType.byRawId(tag.getInt("dimension"));
            this.built = tag.getBoolean("built");
        }

        public CompoundTag toTag() {
            CompoundTag tag = new CompoundTag();
            tag.putLong("origin", originPosition.asLong());
            tag.putInt("dimension", originDimension.getRawId());
            tag.putBoolean("built", built);

            return tag;
        }

        public void build(ServerWorld destination) {
            if (!built) {
                int radius = 32;

                BlockPos center = getCenter();
                Esther.log("Building new imprint of " +
                        originDimension + "#" +
                        originPosition + " at " + center);

                ServerWorld originWorld = destination.getServer().getWorld(originDimension);

                Map<Chunk, Pair<BlockPos, Biome>> chunks = new HashMap<>();

                for (int x = -radius; x <= radius; ++x) {
                    for (int y = -radius; y <= radius; ++y) {
                        for (int z = -radius; z <= radius; ++z) {
                            BlockPos destinationPosition = center.add(x, y, z);
                            Chunk chunk = destination.getChunk(destinationPosition);
//                            chunks.put(chunk, new Pair<>(destinationPosition, originWorld.getBiome(originPosition.add(x, y, z))));

                            if (destinationPosition.isWithinDistance(center, radius)) {
                                destination.setBlockState(destinationPosition, destination.getServer().getWorld(originDimension).getBlockState(originPosition.add(x, y, z)));

                                // I set the biome for every. single. block. For *some* reason, doing it once for each chunk just doesn't work.
                                MutableBiomeArray biomeArray = MutableBiomeArray.inject(chunk.getBiomeArray());
                                assert biomeArray != null;
                                biomeArray.setBiome(destinationPosition, originWorld.getBiome(originPosition.add(x, y, z)));
                                chunk.setShouldSave(true);
                            } else if (destinationPosition.isWithinDistance(center, radius + 2))
                                destination.setBlockState(destinationPosition, Esther.SPACE.getDefaultState());
                        }
                    }
                }

//                System.out.println(chunks.size());
//
//                for (Map.Entry<Chunk, Pair<BlockPos, Biome>> entry : chunks.entrySet()) {
//                    MutableBiomeArray biomeArray = MutableBiomeArray.inject(entry.getKey().getBiomeArray());
//                    assert biomeArray != null;
//                    biomeArray.setBiome(entry.getValue().getLeft(), entry.getValue().getRight());
//                    entry.getKey().setShouldSave(true);
//                }

                Esther.log("Done building imprint");
            }

            built = true;
        }

        public int getId() {
            return this.id;
        }
    }
}
