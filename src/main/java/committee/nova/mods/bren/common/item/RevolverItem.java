package committee.nova.mods.bren.common.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import committee.nova.mods.bren.common.entity.IGunUser;
import committee.nova.mods.bren.init.registry.EnchantmentReg;
import committee.nova.mods.bren.init.registry.SoundReg;
import committee.nova.mods.bren.common.PoseType;
import committee.nova.mods.bren.utils.GunHelper;

public class RevolverItem extends BulletOnlyGun {

    public RevolverItem(Properties settings, Tier material, GunProperties gunProperties) {
        super(settings, material, gunProperties);
    }

    @Override
    public int getMaxCapacity(ItemStack stack) {
        return 6 * Math.round(Math.max(1, EnchantmentHelper.getItemEnchantmentLevel(EnchantmentReg.OVERFLOW.get(), stack)/2));
    }

    @Override
    public PoseType holdingPose() {
        return PoseType.REVOLVER;
    }

    @Override
    public int reloadSpeed() {
        return 16;
    }

    @Override
    public boolean renderOnBack() {
        return false;
    }

    @Override
    public boolean applyCustomMatrix(LivingEntity entity, GunHelper.GunStates state, PoseStack matrices, ItemStack stack, float cooldownProgress, ItemDisplayContext renderMode, boolean leftHanded) {
        if (entity instanceof IGunUser gunUser && cooldownProgress > 0) {

            boolean reloading = gunUser.getGunState().equals(GunHelper.GunStates.RELOADING);

            float sin = (float) Math.sin((cooldownProgress * 2 - 0.5) * Math.PI) * 0.5F + 0.5F;

            if (renderMode.firstPerson()) {
                matrices.translate(0, 0.2, 0);
            }

            matrices.translate(0, (reloading ? sin / 2 : 0), 0);

            matrices.mulPose(Axis.ZN.rotationDegrees(leftHanded ? 90 + 25 : 65 +
                    (reloading && !renderMode.firstPerson() ? sin * 180 : 0)));
            matrices.mulPose(Axis.XN.rotation(cooldownProgress * 15));
        }

        return true;
    }

    @Override
    public boolean hasGUIModel() {
        return false;
    }

    @Override
    public boolean ejectCasing() {
        return false;
    }

    @Override
    protected void afterInserted(ItemStack stack, Player player) {
        player.level().playSound(null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundReg.ITEM_REVOLVER_RELOAD,
                SoundSource.PLAYERS, 1.0F, 1.0F - (player.getRandom().nextFloat() - 0.5F) / 4);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (entity instanceof IGunUser gunUser && entity instanceof Player player) {
            var cooldownManager = player.getCooldowns();
            if (selected) {
                float f = cooldownManager.getCooldownPercent(stack.getItem(), 1);

                if (f == .5F && gunUser.getGunState() == GunHelper.GunStates.RELOADING) {
                    player.level().playSound(null,
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            SoundReg.ITEM_REVOLVER_BULLET_INSERT,
                            SoundSource.PLAYERS, 1.0F, 1.0F - (player.getRandom().nextFloat() - 0.5F) / 4);
                } else if (player.tickCount % 5 == 0 && cooldownManager.isOnCooldown(stack.getItem())) {
                    player.level().playSound(null,
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            SoundReg.ITEM_REVOLVER_SPINNING,
                            SoundSource.PLAYERS, 1.0F, 1.0F - (player.getRandom().nextFloat() - 0.5F) / 4);
                }
            }
        }
    }
}
