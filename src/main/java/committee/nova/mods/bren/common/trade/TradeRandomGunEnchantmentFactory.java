package committee.nova.mods.bren.common.trade;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.trading.MerchantOffer;
import committee.nova.mods.bren.init.registry.EnchantmentReg;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public record TradeRandomGunEnchantmentFactory(int price, int exp) implements VillagerTrades.ItemListing {

    private static final Set<Enchantment> ENCHANTS = ImmutableList.of(
            EnchantmentReg.AUTOFILL.get(),
            EnchantmentReg.OVERFLOW.get(),
            EnchantmentReg.SILENCED.get(),
            EnchantmentReg.STEADY_HANDS.get(),
            EnchantmentReg.FIRE_LANCE.get(),
            EnchantmentReg.MOUNTED.get()
    ).stream().collect(ImmutableSet.toImmutableSet());

    @Override
    public @NotNull MerchantOffer getOffer(@NotNull Entity pTrader, @NotNull RandomSource random) {
        int i = random.nextInt(4) + 1;

        Enchantment enchantment = (Enchantment) ENCHANTS.toArray()[random.nextIntBetweenInclusive(0, random.nextInt(ENCHANTS.size() - 1))];

        ItemStack itemStack = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, i));

        return new MerchantOffer(new ItemStack(Items.EMERALD, this.price + i + 1), new ItemStack(Items.BOOK), itemStack, 12, this.exp, .23f);
    }
}
