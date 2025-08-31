package committee.nova.mods.bren.common.trade;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.MerchantOffer;
import committee.nova.mods.bren.init.registry.ItemReg;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public record TradeRandomGunFactory(int price, int exp) implements VillagerTrades.ItemListing {

    private static final Set<Item> GUNS = ImmutableList.of(
            ItemReg.MACHINE_GUN.get(),
            ItemReg.AUTO_GUN.get(),
            ItemReg.RIFLE.get(),
            ItemReg.SHOTGUN.get(),
            ItemReg.REVOLVER.get()
    ).stream().collect(ImmutableSet.toImmutableSet());


    @Override
    public @NotNull MerchantOffer getOffer(@NotNull Entity pTrader, @NotNull RandomSource random) {
        Item item = (Item) GUNS.toArray()[random.nextIntBetweenInclusive(0, GUNS.size() - 1)];

        int i = 5 + random.nextInt(15);
        ItemStack itemStack = EnchantmentHelper.enchantItem(random, new ItemStack(item), i, false);
        int j = Math.min(this.price + i, 64);
        ItemStack itemStack2 = new ItemStack(Items.EMERALD, j);


        return new MerchantOffer(itemStack2, itemStack, 1, this.exp, .23f);
    }
}
