package committee.nova.mods.bren.common.enchantment;

import net.minecraft.world.item.ItemStack;
import committee.nova.mods.bren.common.item.MachineGunItem;

public class MountedEnchantment extends GunEnchantment {
    public MountedEnchantment(Rarity weight) {
        super(weight);
    }

    public int getMinPower(int level) {
        return 0;
    }

    public int getMaxPower(int level) {
        return 1;
    }

    public boolean isTreasure() {
        return true;
    }

    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.getItem() instanceof MachineGunItem;
    }
}
