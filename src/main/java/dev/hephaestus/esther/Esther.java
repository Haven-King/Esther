package dev.hephaestus.esther;

import dev.hephaestus.esther.block.SpaceBlock;
import dev.hephaestus.esther.features.ShrineFeature;
import dev.hephaestus.esther.items.AscendantItem;
import dev.hephaestus.esther.spells.*;
import dev.hephaestus.esther.util.ManaComponent;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.EntityComponentCallback;
import nerdhub.cardinal.components.api.util.EntityComponents;
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biomes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Esther implements ModInitializer {
	public static final String MOD_ID = "esther";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final boolean DEBUG = true;

	public static final Identifier UPDATE_MANA_PACKET_ID = newID("mana");

	public static final SpellRegistry SPELLS = new SpellRegistry();

	public static final Block SPACE = new SpaceBlock(FabricBlockSettings.of(Material.GLASS).nonOpaque().strength(-1.0F, 3600000.0F).build());

	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(
			newID("general"))
			.icon(() -> new ItemStack(Esther.ASCENDANT))
			.build();

	public static final Item ASCENDANT = new AscendantItem(new Item.Settings().group(ITEM_GROUP).maxCount(1));

	public static final ComponentType<ManaComponent> MANA =
			ComponentRegistry.INSTANCE.registerIfAbsent(newID("mana"), ManaComponent.class);

	@Override
	public void onInitialize() {
		Registry.register(Registry.BLOCK, newID("space"), SPACE);
		Registry.register(Registry.ITEM, newID("ascendant"), ASCENDANT);

		ShrineFeature.create(Biomes.FOREST);
		ShrineFeature.create(Biomes.DESERT);
		EstherDimensions.init();


		SPELLS.register(new BindAscendant(newID("bind_ascendant"), Spell.Difficulty.HARD, 30), "sanguinem filio, sanguinem effurgarex perpetuum").withSound(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL);
		SPELLS.register(new UseAscendant(newID("use_ascendant"), Spell.Difficulty.MODERATE, 15), "Sangima Maerma, Bernos Asescenda");
		SPELLS.register(newID("use_ascendant"), "Sangina Mearma, Ascendarum Cavea");
		SPELLS.register(newID("use_ascendant"), "Sangiema Meam Et Nos Mundo Carcerema");

		SPELLS.register(new Fireball(newID("fireball"), Spell.Difficulty.EASY, 5), "crepitus");

		SPELLS.register(new StartFire(newID("star_fire"), Spell.Difficulty.TRIVIAL, 1), "ignus");

		SPELLS.register(new Venom(newID("venom"), Spell.Difficulty.MODERATE, 10), "ostium");

		EntityComponentCallback.event(PlayerEntity.class).register((player, components) ->
				components.put(MANA, new ManaComponent(player)));

		EntityComponents.setRespawnCopyStrategy(MANA, RespawnCopyStrategy.NEVER_COPY);
	}

	public static void log(String msg) {
		LOGGER.info(String.format("[%s] %s", MOD_ID.substring(0, 1).toUpperCase() + MOD_ID.substring(1), msg));
	}

	public static void debug(String msg) {
		if (DEBUG) LOGGER.info(String.format("[%s] %s", MOD_ID.substring(0, 1).toUpperCase() + MOD_ID.substring(1), msg));
	}

	public static Identifier newID(String id) {
		return new Identifier(MOD_ID, id);
	}
}
