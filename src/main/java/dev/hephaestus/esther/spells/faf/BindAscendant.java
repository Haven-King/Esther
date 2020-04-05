package dev.hephaestus.esther.spells.faf;

import dev.hephaestus.esther.Esther;
import dev.hephaestus.esther.EstherDimensions;
import dev.hephaestus.esther.block.ShrineBlockBottom;
import dev.hephaestus.esther.spells.Spell;
import dev.hephaestus.esther.util.ImprintManager;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class BindAscendant extends Spell {
    public BindAscendant(Identifier id, Difficulty difficulty, int level) {
        super(id, difficulty, level);
    }

    @Override
    public void cast(ServerPlayerEntity player) {

        ServerWorld world = player.getServerWorld();
        ItemStack ascendant = player.getStackInHand(player.getActiveHand());

        if (world.getDimension().getType() != EstherDimensions.IMPRINT) {
            boolean shrineNearby = false;

            for (int x = -15; x <= 15; ++x) {
                for (int y = -15; y <= 15; ++y) {
                    for (int z = -15; z <= 15; ++z) {
                        BlockPos l = player.getBlockPos().add(x, y, z);

                        if (world.getBlockState(l).getBlock() instanceof ShrineBlockBottom && l.isWithinDistance(player.getPos(), 15)) {
                            shrineNearby = true;
                        }
                    }
                }
            }

            if (shrineNearby) {
                super.cast(player);

                ImprintManager.Imprint imprint = ImprintManager.getInstance(world).getNextImprint();

                imprint.update(
                        player.getEntityWorld().getDimension().getType(),
                        player.getBlockPos()
                );

                ascendant.getOrCreateSubTag(Esther.MOD_ID)
                        .putInt("imprint", imprint.getId());

                Esther.log(ImprintManager.getInstance(world).toString());
            }
        }
    }

    @Override
    public boolean canCast(ServerPlayerEntity player) {
        return super.canCast(player) && player.getStackInHand(player.getActiveHand()).getItem() == Esther.ASCENDANT &&
            (!player.getStackInHand(player.getActiveHand()).hasTag() || !player.getStackInHand(player.getActiveHand()).getTag().contains(Esther.MOD_ID));
    }
}
