package committee.nova.mods.bren.common.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import committee.nova.mods.bren.Bren;
import committee.nova.mods.bren.common.entity.IGunUser;
import committee.nova.mods.bren.init.registry.ItemReg;
import committee.nova.mods.bren.utils.GunHelper;

public class BulletOnlyGun extends GunItem{
    public BulletOnlyGun(Properties settings, Tier material, GunProperties gunProperties) {
        super(settings, material, gunProperties);
    }

    @Override
    public int getContents(ItemStack stack) {
        if (stack.getTag() != null) {
            return stack.getTag().getInt("Contents");
        }
        return 0;
    }

    public void addContent(ItemStack stack) {
        if (stack.getItem() instanceof BulletOnlyGun) {
            stack.getOrCreateTag().putInt("Contents", getContents(stack) + 1);
        }
    }

    @Override
    public void useBullet(ItemStack stack) {
        if (stack.getItem() instanceof BulletOnlyGun) {
            stack.getOrCreateTag().putInt("Contents", getContents(stack) - 1);
        }
    }

    @Override
    public void onReload(Player player) {
        ItemStack stack = player.getMainHandItem();
        ItemCooldowns cooldownManager = player.getCooldowns();

        if (player instanceof IGunUser gunUser && !cooldownManager.isOnCooldown(stack.getItem())) {

            ItemStack bullets = Bren.getItemFromPlayer(player, compatibleBullet());

            gunUser.setGunState(GunHelper.GunStates.NORMAL);
            gunUser.setCanReload(true);

            if (bullets.isEmpty() || getContents(stack) >= getMaxCapacity(stack)) {
                return;
            }

            if (!gunUser.canReload()) {
                return;
            }
            gunUser.setCanReload(false);
            gunUser.setGunState(GunHelper.GunStates.RELOADING);
            cooldownManager.addCooldown(stack.getItem(), this.reloadSpeed());
            onInsert(stack, player);
        }
    }

    protected void onInsert(ItemStack stack, Player player) {}

    protected void afterInserted(ItemStack stack, Player player) {}

    protected void onFullyLoaded(ItemStack stack, Player player) {}

    public Item compatibleBullet() {
        return ItemReg.BULLET.get();
    }


    @Override
    public void reloadTick(ItemStack stack, Level world, Player player, IGunUser gunUser) {

        ItemCooldowns cooldownManager = player.getCooldowns();

        if (!cooldownManager.isOnCooldown(stack.getItem()) && getContents(stack) < getMaxCapacity(stack)) {

            ItemStack bullets = Bren.getItemFromPlayer(player, compatibleBullet());

            bullets.shrink(1);
            addContent(stack);

            afterInserted(stack, player);

            gunUser.setGunState(GunHelper.GunStates.NORMAL);
            gunUser.setCanReload(true);
        } else if (cooldownManager.getCooldownPercent(stack.getItem(), 1) == 0 && getContents(stack) == getMaxCapacity(stack) - 1) {
            onFullyLoaded(stack, player);
        }
    }
}
