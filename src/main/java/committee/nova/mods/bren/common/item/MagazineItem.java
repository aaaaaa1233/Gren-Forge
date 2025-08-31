package committee.nova.mods.bren.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import committee.nova.mods.bren.Bren;
import committee.nova.mods.bren.init.registry.EnchantmentReg;
import committee.nova.mods.bren.common.enchantment.AutofillEnchantment;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MagazineItem extends Item {
    private final int capacity;

    public MagazineItem(Properties settings, int capacity) {
        super(settings.stacksTo(1));
        this.capacity = capacity;

    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        ChatFormatting formatting = ChatFormatting.GRAY;

        tooltip.add(Component.translatable(String.format("desc.%s.item.magazine.content",Bren.MODID))
                .append(Component.literal(" " + getContents(stack) + "/" + getMaxCapacity(stack))).withStyle(formatting));

        super.appendHoverText(stack, world, tooltip, context);
    }


    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {

        if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentReg.AUTOFILL.get(), stack) > 0 && entity instanceof Player player) {
            AutofillEnchantment.insert(stack, player);
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    public static int getMaxCapacity(ItemStack stack) {
        if (stack.getItem() instanceof MagazineItem magazineItem) {
            return Math.round(magazineItem.capacity * Math.max(1, 1 + (float) EnchantmentHelper.getItemEnchantmentLevel(EnchantmentReg.OVERFLOW.get(), stack) / 4));
        }
        return 10;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        if (stack.getItem() instanceof MagazineItem) {
            return Math.round(getContents(stack) * 13.0F / (float) getMaxCapacity(stack));
        }
        return 0;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Bren.UNIVERSAL_AMMO_COLOR;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getContents(stack) > 0;
    }


    public static int getContentsNBT(CompoundTag nbtCompound) {
        if (!nbtCompound.isEmpty()) {
            return nbtCompound.getCompound("tag").getInt("Contents");
        }
        return 0;
    }

    public static int getContents(ItemStack itemStack) {
        if (itemStack.getTag() != null) {
            return itemStack.getTag().getInt("Contents");
        }
        return 0;
    }

    public static boolean isEmpty(ItemStack stack) {
        if (stack.getTag() != null) {
            return stack.getTag().getInt("Contents") <= 0;
        }
        return true;
    }

    public static void removeOneContent(CompoundTag nbtCompound) {
        if (!nbtCompound.isEmpty()) {
            nbtCompound.getCompound("tag").putInt("Contents", Math.max(nbtCompound.getCompound("tag").getInt("Contents") - 1, 0));
        }
    }

    public static boolean isFull(ItemStack mag) {
        if (mag.getItem() instanceof MagazineItem) {
            return MagazineItem.getContents(mag) >= MagazineItem.getMaxCapacity(mag);
        }
        return false;
    }

    public static int fillMagazine(ItemStack mag, int amount) {
        if (mag.getItem() instanceof MagazineItem) {
            int original = getContents(mag);

            int i = Math.min(getContents(mag) + amount, getMaxCapacity(mag));
            mag.getOrCreateTag().putInt("Contents", i);

            return getMaxCapacity(mag) - original;
        }
        return 0;
    }
}
