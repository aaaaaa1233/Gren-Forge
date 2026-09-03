package committee.nova.mods.bren.common.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.sounds.SoundSource;
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

public class ShotgunItem extends BulletOnlyGun {

    public ShotgunItem(Properties settings, Tier material, GunProperties gunProperties) {
        super(settings, material, gunProperties);
    }

    @Override
    protected void onInsert(ItemStack stack, Player player) {
        player.level().playSound(null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundReg.ITEM_SHOTGUN_SHELL_INSERT,
                SoundSource.PLAYERS, 1.0F, 1.0F - (player.getRandom().nextFloat() - 0.5F) / 4);
    }

    @Override
    protected void onFullyLoaded(ItemStack stack, Player player) {
        player.level().playSound(null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundReg.ITEM_SHOTGUN_RACK,
                SoundSource.PLAYERS, 1.0F, 1.0F - (player.getRandom().nextFloat() - 0.5F) / 4);
    }

    @Override
    public int getMaxCapacity(ItemStack stack) {
        return 8 * Math.round(Math.max(1, EnchantmentHelper.getItemEnchantmentLevel(EnchantmentReg.OVERFLOW.get(), stack)/2));
    }

    @Override
    public boolean applyCustomMatrix(LivingEntity entity, GunHelper.GunStates state, PoseStack matrixStack, ItemStack stack, float cooldownProgress, ItemDisplayContext renderMode, boolean leftHanded) {

        if (state == GunHelper.GunStates.NORMAL && renderMode.firstPerson()) {

            float f = -Math.min(cooldownProgress - .5F, 0) * 2;
            float f1 = -Math.min(cooldownProgress - .3333333333333333F, 0) * 3;

            float sin1 = (float) Math.abs(Math.sin(f * Math.PI));
            float sin2 = (float) Math.abs(Math.sin(f1 * Math.PI));

            matrixStack.translate(0, sin1 * .3 - sin2 * .7, -0.2F * sin1);
            matrixStack.mulPose(Axis.XP.rotation(sin1 * 1.047198F));
        }

        return false;
    }


    @Override
    public float spread() {
        return 6.0F;
    }

    @Override
    public int bulletAmount() {
        return 10;
    }

    @Override
    public Item compatibleBullet() {return ItemReg.SHELL.get();}

    @Override
    public int reloadSpeed() {
        return 13;
    }
}
