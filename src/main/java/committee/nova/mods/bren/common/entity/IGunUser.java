package committee.nova.mods.bren.common.entity;

import net.minecraft.world.item.ItemStack;
import committee.nova.mods.bren.utils.GunHelper;

public interface IGunUser {

    void setReloadingGun(ItemStack reloadingGun);

    ItemStack getReloadingGun();

    boolean isShooting();

    boolean canShoot();

    void setCanShoot(boolean canShoot);

    int getGunTicks();

    void setGunTicks(int t);

    int shootingDuration();

    void setShootingDuration(int t);

    void setCanReload(boolean b);

    boolean canReload();

    ItemStack getLastGun();

    void setLastGun(ItemStack lastGun);

    void setLastEquipGun(ItemStack lastEquipGun);

    ItemStack getLastEquipGun();

    boolean lastGunLoaded();

    void setLastGunLoaded(boolean lastGunLoaded);

    GunHelper.GunStates getGunState();

    void setGunState(GunHelper.GunStates state);
}
