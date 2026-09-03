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

public class DBShotgunItem extends BulletOnlyGun {

    public DBShotgunItem(Properties settings, Tier material, GunProperties gunProperties) {
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
                SoundReg.ITEM_DB_SHOTGUN_RELOAD,
                SoundSource.PLAYERS, 0.65F, 1.0F - (player.getRandom().nextFloat() - 0.5F) / 4);
    }

    @Override
    public int getMaxCapacity(ItemStack stack) {
        return 2 * Math.round(Math.max(1, EnchantmentHelper.getItemEnchantmentLevel(EnchantmentReg.OVERFLOW.get(), stack)/2));
    }



    @Override
    public boolean ejectCasing() {
        return false;
    }

    @Override
    public float spread() {
        return 7.5F;
    }

    @Override
    public int bulletAmount() {
        return 18;
    }

    @Override
    public Item compatibleBullet() {return ItemReg.SHELL.get();}

    @Override
    public int reloadSpeed() {
        return 15;
    }
}
