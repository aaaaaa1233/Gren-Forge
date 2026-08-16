package committee.nova.mods.bren.common.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import committee.nova.mods.bren.common.PoseType;
import committee.nova.mods.bren.common.entity.IGunUser;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import committee.nova.mods.bren.init.registry.EnchantmentReg;
import committee.nova.mods.bren.init.registry.ItemReg;
import committee.nova.mods.bren.init.registry.SoundReg;
import committee.nova.mods.bren.utils.GunHelper;

public class PistolItem extends GunWithMagItem {

    public PistolItem(Properties settings, Tier material, TagKey<Item> compatibleMagazines, GunProperties gunProperties) {
        super(settings, material, compatibleMagazines, gunProperties);
    }

    @Override
    public PoseType holdingPose() {
        return PoseType.ONE_ARM;
    }


    @Override
    public boolean applyCustomMatrix(LivingEntity entity, GunHelper.GunStates state, PoseStack matrices, ItemStack stack, float cooldownProgress, ItemDisplayContext renderMode, boolean leftHanded) {
        if (entity instanceof IGunUser gunUser && cooldownProgress > 0) {

            boolean reloading = gunUser.getGunState().equals(GunHelper.GunStates.RELOADING);

            float sin = (float) Math.sin((cooldownProgress * 2 - 0.5) * Math.PI) * 0.5F + 0.5F;

            if (renderMode.firstPerson()) {
                matrices.translate(0, 0.0, 0);
            }



            matrices.translate(0, (reloading ? sin / 5 : -0.15), 0);

            matrices.mulPose(Axis.ZN.rotation((reloading ? sin / -3 : 0)));

            matrices.mulPose(Axis.XN.rotation(cooldownProgress * 3));


            if (cooldownProgress >= cooldownProgress/2) {

                matrices.translate(0, (reloading ? sin / 7 : 0.0), 0);
                matrices.mulPose(Axis.ZP.rotation((reloading ? sin / -3 : 0)));
                matrices.mulPose(Axis.XP.rotation(cooldownProgress * 6));
            }
        }

        return true;
    }








    @Override
    public boolean renderOnBack() {
        return false;
    }

    @Override
    public boolean hasGUIModel() {
        return false;
    }


}
