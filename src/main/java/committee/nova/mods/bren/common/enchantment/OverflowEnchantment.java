package committee.nova.mods.bren.common.enchantment;


import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import committee.nova.mods.bren.common.item.ShotgunItem;

public class OverflowEnchantment extends MagazineEnchantment {
    public OverflowEnchantment(Enchantment.Rarity weight) {
        super(weight);
    }

    public int getMinPower(int level) {
        return level * 2;
    }

    public int getMaxPower(int level) {
        return 8;
    }

    public boolean isTreasure() {
        return false;
    }

    public int getMaxLevel() {
        return 4;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return super.canEnchant(stack) || stack.getItem() instanceof ShotgunItem;
    }
}
