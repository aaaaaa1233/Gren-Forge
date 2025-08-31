package committee.nova.mods.bren.common.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import committee.nova.mods.bren.common.item.GunItem;

public class GunEnchantment extends Enchantment {
    public GunEnchantment(Rarity weight) {
        super(weight, EnchantmentCategory.BOW, new EquipmentSlot[]{EquipmentSlot.MAINHAND,EquipmentSlot.OFFHAND});
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.getItem() instanceof GunItem;
    }
}
