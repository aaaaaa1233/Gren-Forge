package committee.nova.mods.bren.common.item;


import committee.nova.mods.bren.Bren;
import committee.nova.mods.bren.common.entity.IGunUser;
import committee.nova.mods.bren.init.registry.SoundReg;
import committee.nova.mods.bren.utils.GunHelper;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class HeavyMgItem extends GunWithMagItem {
    public HeavyMgItem(Properties settings, Tier material, TagKey<Item> compatibleMagazines, GunProperties gunProperties) {
        super(settings, material, compatibleMagazines, gunProperties);
    }

    @Override
    public int reloadSpeed() {
        return 40;
    }

    @Override
    public void reloadTick(ItemStack stack, Level world, Player player, IGunUser gunUser) {

        var cooldownManager = player.getCooldowns();

        if (!cooldownManager.isOnCooldown(stack.getItem())) {
            if (GunWithMagItem.hasMagazine(stack)) {

                GunWithMagItem.unloadMagazine(stack, player);

                world.playSound(null,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        SoundReg.ITEM_MAGAZINE_REMOVE,
                        SoundSource.PLAYERS, 1.0F, 1.0F - (player.getRandom().nextFloat() - 0.5F) / 4);

            } else {
                ItemStack mag = Bren.getMagazineFromPlayer(player, ((GunWithMagItem) stack.getItem()).compatibleMagazines());
                GunWithMagItem.putMagazine(stack, mag);
                mag.shrink(1);
            }
            gunUser.setGunState(GunHelper.GunStates.NORMAL);
            gunUser.setCanReload(true);
        } else if (cooldownManager.getCooldownPercent(stack.getItem(),1) == 0.325F && !GunWithMagItem.hasMagazine(stack)) {
            world.playSound(null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundReg.ITEM_MAGAZINE_INSERT,
                    SoundSource.PLAYERS, 1.0F, 1.0F - (player.getRandom().nextFloat() - 0.5F) / 4);
        }

    }

}
