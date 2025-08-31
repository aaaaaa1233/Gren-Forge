package committee.nova.mods.bren.common.enchantment;


import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import committee.nova.mods.bren.Bren;
import committee.nova.mods.bren.common.item.MagazineItem;
import committee.nova.mods.bren.init.registry.EnchantmentReg;
import committee.nova.mods.bren.init.registry.ItemReg;

public class AutofillEnchantment extends MagazineEnchantment {
    public AutofillEnchantment(Rarity weight) {
        super(weight);
    }

    public static void insert(ItemStack mag, Player player) {

        if (player.getRandom().nextFloat() > 0.1) {
            return;
        }

        float calculatedChance = ((float) EnchantmentHelper.getItemEnchantmentLevel(EnchantmentReg.AUTOFILL.get(), mag) / 40) * 2.5F;

        if (player.getRandom().nextFloat() <= calculatedChance)

            if (mag.getItem() instanceof MagazineItem && !MagazineItem.isFull(mag)) {
                ItemStack bullet = Bren.getItemFromPlayer(player, ItemReg.BULLET.get());
                if (!bullet.isEmpty()) {
                    if (!player.level().isClientSide()) {
                        MagazineItem.fillMagazine(mag, 1);
                        bullet.shrink(1);
                    }
                }
            }
    }

    public int getMinPower(int level) {
        return 0;
    }

    public int getMaxPower(int level) {
        return 4;
    }

    public boolean isTreasure() {
        return true;
    }

    public int getMaxLevel() {
        return 4;
    }
}
