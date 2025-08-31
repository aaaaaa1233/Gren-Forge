package committee.nova.mods.bren;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import committee.nova.mods.bren.init.config.MConfig;
import committee.nova.mods.bren.init.registry.*;
import committee.nova.mods.bren.common.item.MagazineItem;
import committee.nova.mods.bren.common.criterion.LongShootingCriterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Bren.MODID)
public class Bren {
	public static final String MODID = "bren";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	public static final int UNIVERSAL_AMMO_COLOR = 0xFFAE00;



	public static LongShootingCriterion LONG_SHOOTING = CriteriaTriggers.register(new LongShootingCriterion());

    public Bren() {
        MConfig.init();
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        AttributeReg.ATTRIBUTES.register(bus);
        BlockReg.BLOCKS.register(bus);
        ItemReg.ITEMS.register(bus);
        TabReg.TABS.register(bus);
        ParticleReg.PARTICLE_TYPE.register(bus);
        EnchantmentReg.ENCHANTMENTS.register(bus);
        EntityReg.ENTITIES.register(bus);
        VillagersReg.POINTS_OF_INTEREST.register(bus);
        VillagersReg.PROFESSIONS.register(bus);
        LOGGER.info("BAM! {} is done loading!", MODID);
    }


    @SubscribeEvent
    public static void serverStarting(ServerStartingEvent event) {
        StructureRegistry.registerJigsaws(event.getServer());
    }

	private static ItemStack mag(MagazineItem m) {
		ItemStack mag = m.getDefaultInstance();
		MagazineItem.fillMagazine(mag, MagazineItem.getMaxCapacity(mag));
		return mag;
	}

	public static ItemStack getMagazineFromPlayer(Player player, TagKey<Item> magTag) {
		var inventory = player.getInventory();

		ItemStack fullestMag = ItemStack.EMPTY;

		if (player.getOffhandItem().is(magTag)) {
			if (!MagazineItem.isEmpty(player.getOffhandItem())) return player.getOffhandItem();
		}
		for(int i = 0; i < inventory.getContainerSize(); ++i) {
			ItemStack itemStack = inventory.getItem(i);
			if (itemStack.is(magTag)) {
				if (!MagazineItem.isEmpty(itemStack) && MagazineItem.getContents(itemStack) > MagazineItem.getContents(fullestMag)) {
					fullestMag = itemStack;
				};
			}
		}
		return fullestMag;
	}

	public static ItemStack getItemFromPlayer(Player player, Item item) {
		var inventory = player.getInventory();

		if (player.getOffhandItem().is(item)) {
			return player.getOffhandItem();
		}
		for(int i = 0; i < inventory.getContainerSize(); ++i) {
			ItemStack itemStack = inventory.getItem(i);
			if (itemStack.is(item)) {
				return itemStack;
			}
		}
		return ItemStack.EMPTY;
	}
}