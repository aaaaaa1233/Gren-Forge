package committee.nova.mods.bren.common.enchantment;

import net.minecraft.world.item.ItemStack;
import committee.nova.mods.bren.common.item.RevolverItem;
import committee.nova.mods.bren.common.item.ShotgunItem;

public class SilencedEnchantment extends GunEnchantment {
    public SilencedEnchantment(Rarity weight) {
        super(weight);
    }

    public int getMinPower(int level) {
        return 0;
    }

    public int getMaxPower(int level) {
        return 1;
    }

    public boolean isTreasure() {
        return false;
    }

    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return super.canEnchant(stack) && !(stack.getItem() instanceof ShotgunItem || stack.getItem() instanceof RevolverItem);
    }
}
