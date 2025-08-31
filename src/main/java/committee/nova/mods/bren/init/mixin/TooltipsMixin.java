package committee.nova.mods.bren.init.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import committee.nova.mods.bren.init.config.MConfig;
import committee.nova.mods.bren.init.registry.AttributeReg;
import committee.nova.mods.bren.init.registry.EnchantmentReg;
import committee.nova.mods.bren.common.item.GunItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.text.DecimalFormat;
import java.util.UUID;

/*
 * Credits : WHY-HAVE-YOU-SUMMONED-ME
 * From : https://github.com/WHY-HAVE-YOU-SUMMONED-ME/Bren/blob/main/src/main/java/nl/sniffiandros/bren/common/mixin/CustomFirearmTooltips.java
 *
 * He/She knows much more about mixins than me. That's for sure!
 */


@Mixin(ItemStack.class)
public abstract class TooltipsMixin {
    @Final
    @Shadow
    private Item item;

    @ModifyVariable(method = "getTooltipLines", at = @At("STORE"), ordinal = 0)
    private boolean overrideFormatting(boolean original) {
        return original || item instanceof GunItem;
    }

    @Redirect(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Ljava/text/DecimalFormat;format(D)Ljava/lang/String;", ordinal = 0))
    private String modifyTooltipContents(DecimalFormat formatter, double value, @Local AttributeModifier attribute, @Local @Nullable Player player) {
        String insertion = "";
        UUID attributeID = attribute.getId();

        if (attributeID == AttributeReg.RANGED_DAMAGE_MODIFIER_ID) {

            if (item instanceof GunItem gunItem) {
                if (gunItem.bulletAmount() > 1) {
                    insertion = "x" + formatter.format(gunItem.bulletAmount());
                }
            }

            if (player != null) {
                value += player.getAttributeBaseValue(AttributeReg.RANGED_DAMAGE.get());
            }

        } else if (attributeID == AttributeReg.FIRE_RATE_MODIFIER_ID) {
            insertion = "t";

            if (player != null) {
                value += player.getAttributeBaseValue(AttributeReg.FIRE_RATE.get());
            }

        } else if (attributeID == AttributeReg.RECOIL_MODIFIER_ID) {
            insertion = "°";

            value *= MConfig.recoilMultiplier.get();
            value /= ((EnchantmentHelper.getItemEnchantmentLevel(EnchantmentReg.STEADY_HANDS.get(), (ItemStack)(Object)this) * 2.6d * 0.1d) + 1);

            if (player != null) {
                value += player.getAttributeBaseValue(AttributeReg.RECOIL.get());
            }
            value = Math.round(value * 2) / 2.0;
        }

        return formatter.format(value) + insertion;
    }
}
