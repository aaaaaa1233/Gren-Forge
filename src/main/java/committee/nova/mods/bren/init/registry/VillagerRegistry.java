package committee.nova.mods.bren.init.registry;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import committee.nova.mods.bren.common.trade.TradeClothedMagFactory;
import committee.nova.mods.bren.common.trade.TradeRandomGunEnchantmentFactory;
import committee.nova.mods.bren.common.trade.TradeRandomGunFactory;

@Mod.EventBusSubscriber
public class VillagerRegistry {
    @SubscribeEvent
    public static void registerTrades(VillagerTradesEvent event) {
        if (event.getType().equals(VillagersReg.GUNSMITH.get())) {
            var trades = event.getTrades();
            trades.get(1).add((trader, rand) -> {
                var a = new MerchantOffer(
                        new ItemStack(Items.GUNPOWDER, 16), new ItemStack(Items.EMERALD, 1), 6, 3, 0.02F);
                return rand.nextFloat() > .4f ? a : new TradeRandomGunEnchantmentFactory(3, 4).getOffer(trader, rand);
            });
            trades.get(1).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, 3),
                    new ItemStack(rand.nextFloat() < .5f ? ItemReg.MAGAZINE.get() : ItemReg.SHORT_MAGAZINE.get(), 1),
                    6, 2, 0.03f));

            trades.get(2).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(ItemReg.MAGAZINE.get()), new ItemStack(Items.EMERALD, 1), 12, 6, 0.03f));
            trades.get(2).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(ItemReg.SHORT_MAGAZINE.get()), new ItemStack(Items.EMERALD, 1), 12, 6, 0.03f));
            trades.get(2).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.COPPER_INGOT, 4), new ItemStack(Items.EMERALD, 1), 12, 5, 0.03f));

            trades.get(3).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 20), new ItemStack(ItemReg.AUTO_LOADER_CONTRAPTION.get(), 1),  6, 10, 0.03f));
            trades.get(3).add(new TradeRandomGunEnchantmentFactory(6, 12));


            trades.get(4).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2), new ItemStack(rand.nextFloat() < .5 ? ItemReg.BULLET.get() : ItemReg.SHELL.get(), 16),
                    12, 14, 0.03f));
            trades.get(4).add((trader, rand) -> {
                var a = new MerchantOffer(
                        new ItemStack(Items.TNT, 1), new ItemStack(Items.EMERALD, 6),  12, 17, 0.03f);
                return rand.nextFloat() > .5f ? a : new TradeClothedMagFactory(4, 15).getOffer(trader, rand);
            });

            trades.get(5).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2), new ItemStack(rand.nextFloat() < .5 ? ItemReg.BULLET.get() : ItemReg.SHELL.get(), 16),
                    15, 14, 0.03f));
            trades.get(5).add(new TradeRandomGunFactory(20, 17));
        }

    }
}
