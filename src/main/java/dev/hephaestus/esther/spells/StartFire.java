package dev.hephaestus.esther.spells;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;

public class StartFire extends Spell {
    public StartFire(Identifier id, Difficulty difficulty, int cost) {
        super(id, difficulty, cost);
    }

    @Override
    void cast(ServerPlayerEntity player) {
        EntityHitResult hitEntity = Spell.traceForEntity(player, 6);
        if (hitEntity != null && hitEntity.getEntity() instanceof LivingEntity) {
            hitEntity.getEntity().setOnFireFor(5);
        } else {
            BlockHitResult hitBlock = Spell.traceForBlock(player, 6);

            BlockPos pos = hitBlock.getBlockPos();
            BlockState state = player.world.getBlockState(pos);

            if (FlintAndSteelItem.isIgnitable(state)) {
                player.world.setBlockState(pos, state.with(Properties.LIT, true), 11);
            } else {
                pos = pos.offset(hitBlock.getSide());
                state = player.world.getBlockState(pos);
                if (FlintAndSteelItem.canIgnite(state, player.world, pos)) {
                    state = ((FireBlock) Blocks.FIRE).getStateForPosition(player.world, pos);
                    player.world.setBlockState(pos, state, 11);
                }
            }
        }
    }
}
