package dev.hephaestus.esther;

import dev.hephaestus.esther.block.SpaceBlock;
import dev.hephaestus.esther.features.ShrineFeature;
import dev.hephaestus.esther.items.AscendantItem;
import dev.hephaestus.esther.spells.*;
import dev.hephaestus.esther.spells.aura.Flight;
import dev.hephaestus.esther.spells.aura.EffectAura;
import dev.hephaestus.esther.spells.faf.BindAscendant;
import dev.hephaestus.esther.spells.faf.Fireball;
import dev.hephaestus.esther.spells.faf.StartFire;
import dev.hephaestus.esther.spells.faf.UseAscendant;
import dev.hephaestus.esther.util.EstherComponent;
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
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biomes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Esther implements ModInitializer {
	public static final String MOD_ID = "esther";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final boolean DEBUG = false;

	public static final Identifier UPDATE_MANA_PACKET_ID = newID("mana");

	public static final Registry SPELLS = new Registry();
	public static Spell BIND_ASCENDANT = SPELLS.register(new BindAscendant(Esther.newID("bind_ascendant"), Spell.Difficulty.HARD, 30), "sanguinem filio, sanguinem effurgarex perpetuum").withSound(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL);
	public static Spell USE_ASCENDANT = SPELLS.register(new UseAscendant(Esther.newID("use_ascendant"), Spell.Difficulty.MODERATE, 15), "Sangima Maerma, Bernos Asescenda");
	public static Spell FIREBALL = SPELLS.register(new Fireball(Esther.newID("fireball"), Spell.Difficulty.EASY, 5), "crepitus");
	public static Spell START_FIRE = SPELLS.register(new StartFire(Esther.newID("star_fire"), Spell.Difficulty.TRIVIAL, 1), "ignus");

	public static Spell FLIGHT = SPELLS.register(new Flight(Esther.newID("flight"), Spell.Difficulty.HARD, 25, new ItemStack(Items.BLAZE_ROD, 12)), "igni mihi caelum", "descendit");

	public static Spell HASTE = SPELLS.register(new EffectAura(Esther.newID("haste"), Spell.Difficulty.MODERATE, 10, StatusEffects.HASTE, new ItemStack(Items.SUGAR, 3)), "celeritas", "se tardum");
	public static Spell HASTE_II = SPELLS.register(new EffectAura(Esther.newID("haste"), Spell.Difficulty.MODERATE, 20, StatusEffects.HASTE, new ItemStack(Items.HONEY_BOTTLE, 1)), "citius", "se tardum");
	public static Spell HASTE_III = SPELLS.register(new EffectAura(Esther.newID("haste"), Spell.Difficulty.HARD, 30, StatusEffects.HASTE, new ItemStack(Items.FEATHER, 5)), "summa celeritate", "se tardum");

	public static Spell WATER_BREATHING = SPELLS.register(new EffectAura(Esther.newID("water_breathing"), Spell.Difficulty.EASY, 10, StatusEffects.WATER_BREATHING, new ItemStack(Items.TROPICAL_FISH_BUCKET, 1)), "respirare me aquae", "respirare me aere");


	public static final Block SPACE = new SpaceBlock(FabricBlockSettings.of(Material.GLASS).nonOpaque().strength(-1.0F, 3600000.0F).sounds(
			new BlockSoundGroup(-1000.0F, 1.0F, SoundEvents.BLOCK_STONE_BREAK, SoundEvents.BLOCK_STONE_STEP, SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL)
	).build());

	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(
			newID("general"))
			.icon(() -> new ItemStack(Esther.ASCENDANT))
			.build();

	public static final Item ASCENDANT = new AscendantItem(new Item.Settings().group(ITEM_GROUP).maxCount(1));

	public static final ComponentType<EstherComponent> COMPONENT =
			ComponentRegistry.INSTANCE.registerIfAbsent(newID("component"), EstherComponent.class);

	static {
		SPELLS.register(Esther.newID("use_ascendant"), "Sangina Mearma, Ascendarum Cavea");
		SPELLS.register(Esther.newID("use_ascendant"), "Sangiema Meam Et Nos Mundo Carcerema");
	}

	@Override
	public void onInitialize() {
		net.minecraft.util.registry.Registry.register(net.minecraft.util.registry.Registry.BLOCK, newID("space"), SPACE);
		net.minecraft.util.registry.Registry.register(net.minecraft.util.registry.Registry.ITEM, newID("ascendant"), ASCENDANT);

		ShrineFeature.create(Biomes.FOREST);
		ShrineFeature.create(Biomes.DESERT);
		EstherDimensions.init();

		EntityComponentCallback.event(PlayerEntity.class).register((player, components) ->
				components.put(COMPONENT, new EstherComponent(player)));

		EntityComponents.setRespawnCopyStrategy(COMPONENT, RespawnCopyStrategy.NEVER_COPY);
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
