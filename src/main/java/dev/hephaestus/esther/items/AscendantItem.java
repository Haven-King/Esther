package dev.hephaestus.esther.items;

import dev.hephaestus.esther.Esther;
import dev.hephaestus.esther.EstherDimensions;
import dev.hephaestus.esther.util.ImprintDimensionPlacer;
import dev.hephaestus.esther.util.ImprintDimensionReturner;
import dev.hephaestus.esther.util.ImprintManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class AscendantItem extends Item {
    public AscendantItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return false;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    public boolean hasEnchantmentGlint(ItemStack stack) {
        return stack.getSubTag(Esther.MOD_ID) != null;
    }

    public static void teleport(ServerPlayerEntity player, CompoundTag ascendant) {
        ImprintManager.Imprint imprint = ImprintManager.getInstance(player.getServerWorld()).getImprintByID(ascendant.getInt("imprint"));
        DimensionType returnDimension = DimensionType.byId(new Identifier(ascendant.getString("return_dimension")));
        BlockPos returnPosition;
        if (ascendant.getType("return_position") == 4) {
            returnPosition = BlockPos.fromLong(ascendant.getLong("return_position"));
        } else if (ascendant.getType("return_position") == 10) {
            CompoundTag posTag = ascendant.getCompound("return_position");
            returnPosition = new BlockPos(
                posTag.getInt("x"),
                posTag.getInt("y"),
                posTag.getInt("z")
            );
        } else {
            returnPosition = null;
        }


        if (player.getServerWorld().getDimension().getType() == EstherDimensions.IMPRINT && returnDimension != null && returnPosition != null) {
            FabricDimensions.teleport(player, returnDimension, new ImprintDimensionReturner(returnPosition));
            ascendant.remove("return_dimension");
            ascendant.remove("return_position");
        } else if (player.getServerWorld().getDimension().getType() != EstherDimensions.IMPRINT && imprint != null) {
            ascendant.putString("return_dimension", player.getEntityWorld().getDimension().getType().toString());

            CompoundTag posTag = new CompoundTag();

            posTag.putInt("x", player.getBlockPos().getX());
            posTag.putInt("y", player.getBlockPos().getY());
            posTag.putInt("z", player.getBlockPos().getZ());

            ascendant.put("return_position", posTag);

            FabricDimensions.teleport(
                    player,
                    EstherDimensions.IMPRINT,
                    new ImprintDimensionPlacer(
                            imprint.getId()
                    )
            );
        }
    }
}
