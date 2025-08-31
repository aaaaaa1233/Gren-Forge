package committee.nova.mods.bren.common.enchantment;

import net.minecraft.world.item.ItemStack;
import committee.nova.mods.bren.common.item.ShotgunItem;

public class FireLanceEnchantment extends GunEnchantment {
    public FireLanceEnchantment(Rarity weight) {
        super(weight);
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
        return 1;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.getItem() instanceof ShotgunItem;
    }
}
