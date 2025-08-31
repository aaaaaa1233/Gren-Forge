package committee.nova.mods.bren.common.trade;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.*;
import net.minecraft.world.item.trading.MerchantOffer;
import committee.nova.mods.bren.init.registry.ItemReg;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record TradeClothedMagFactory(int price, int exp) implements VillagerTrades.ItemListing {

    @Override
    public @NotNull MerchantOffer getOffer(@NotNull Entity pTrader, @NotNull RandomSource random) {

        ItemStack stack = new ItemStack(ItemReg.CLOTHED_MAGAZINE.get());

        int i = random.nextInt(3) + 1;

        List<DyeItem> list = new ArrayList<>();

        for (int i1 = 0; i1 < i; ++i1) {
            DyeColor color = DyeColor.byId(random.nextInt(15));
            DyeItem dyeItem = DyeItem.byColor(color);
            list.add(dyeItem);
        }

        ItemStack clothed_mag = DyeableLeatherItem.dyeArmor(stack, list);

        return new MerchantOffer(new ItemStack(Items.EMERALD, this.price), clothed_mag, 12, this.exp, .26f);
    }
}
