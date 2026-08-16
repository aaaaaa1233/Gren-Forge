package committee.nova.mods.bren.common.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import committee.nova.mods.bren.init.registry.EnchantmentReg;
import committee.nova.mods.bren.init.registry.ItemReg;
import committee.nova.mods.bren.init.registry.SoundReg;
import committee.nova.mods.bren.utils.GunHelper;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class HeavyRifleItem extends BulletOnlyGun {

    public HeavyRifleItem(Properties settings, Tier material, GunProperties gunProperties) {
        super(settings, material, gunProperties);
    }

    @Override
    protected void onInsert(ItemStack stack, Player player) {
        player.level().playSound(null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundReg.ITEM_HEAVY_RIFLE_INSERT,
                SoundSource.PLAYERS, 1.0F, 1.0F - (player.getRandom().nextFloat() - 0.5F) / 4);
    }

    @Override
    protected void onFullyLoaded(ItemStack stack, Player player) {
        player.level().playSound(null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundReg.ITEM_HEAVY_RIFLE_RACK,
                SoundSource.PLAYERS, 1.0F, 1.0F - (player.getRandom().nextFloat() - 0.5F) / 4);
    }

    @Override
    public int getMaxCapacity(ItemStack stack) {
        return Math.round(Math.max(1, EnchantmentHelper.getItemEnchantmentLevel(EnchantmentReg.OVERFLOW.get(), stack)/2));
    }



    @Override
    public int bulletLifespan() {
        return 9;
    }

    @Override
    public float spread() {
        return 1.1F;
    }

    @Override
    public int bulletAmount() {
        return 6;
    }

    @Override
    public Item compatibleBullet() {return ItemReg.SHELL.get();}

    @Override
    public int reloadSpeed() {
        return 35;
    }
}
