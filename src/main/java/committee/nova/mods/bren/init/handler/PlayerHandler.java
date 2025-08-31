package committee.nova.mods.bren.init.handler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import committee.nova.mods.bren.Bren;
import committee.nova.mods.bren.common.entity.IGunUser;
import committee.nova.mods.bren.common.item.GunItem;
import committee.nova.mods.bren.init.events.GunFireEvent;
import committee.nova.mods.bren.utils.GunHelper;
import committee.nova.mods.bren.utils.GunUtils;

/**
 * @author: cnlimiter
 */
@Mod.EventBusSubscriber
public class PlayerHandler {
    public static final EntityDataAccessor<CompoundTag> LAST_GUN_NBT = SynchedEntityData.defineId(Player.class, EntityDataSerializers.COMPOUND_TAG);
    public static final EntityDataAccessor<Integer> GUN_TICKS = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);
    @SubscribeEvent
    public static void playerTickPost(TickEvent.PlayerTickEvent event){
        var player = event.player;
        if (event.phase == TickEvent.Phase.END && player instanceof IGunUser gunUser) {
            if (gunUser.getLastEquipGun().isEmpty() && !player.getEntityData().get(LAST_GUN_NBT).isEmpty() && !gunUser.lastGunLoaded()) {
                buildLastGun(player.getEntityData().get(LAST_GUN_NBT), gunUser);
                gunUser.setLastGunLoaded(true);
            }

            ItemStack handItem = player.getMainHandItem();

            if (handItem.getItem() instanceof GunItem gunItem) {
                if (gunItem.renderOnBack()) {
                    gunUser.setLastEquipGun(handItem);
                }
            }


            if (!handItem.equals(gunUser.getLastEquipGun())) {
                gunUser.setLastGun(gunUser.getLastEquipGun());
            } else {
                gunUser.setLastGun(ItemStack.EMPTY);
            }
            handleShooting(player, gunUser);

            if (gunUser.isShooting()) {
                var dur = gunUser.shootingDuration();
                gunUser.setShootingDuration(++dur);
                if (!player.level.isClientSide()) {
                    Bren.LONG_SHOOTING.trigger((ServerPlayer) player, player.getUseItem());
                }
            } else {
                gunUser.setShootingDuration(0);
            }

            reloadTick(player, gunUser);
        }
    }

    private static void buildLastGun(CompoundTag itemNbt, IGunUser gunUser) {
        String s = itemNbt.getString("id");
        ResourceLocation id = new ResourceLocation(s);
        Item item = ForgeRegistries.ITEMS.getValue(id);

        ItemStack stack = new ItemStack(item);
        stack.setTag(itemNbt.getCompound("tag"));
        gunUser.setLastEquipGun(stack);
    }

    public static void reloadTick(Player player, IGunUser gunUser) {
        if (player.level.isClientSide()) return;
        var cooldownManager = player.getCooldowns();
            if (player.getMainHandItem().getItem() instanceof GunItem gunItem && gunUser.getGunState().equals(GunHelper.GunStates.RELOADING)) {
                gunItem.reloadTick(gunUser.getReloadingGun(), player.level, player, (IGunUser) player);
            }

            if (gunUser.getGunState().equals(GunHelper.GunStates.RELOADING) && player.getMainHandItem() != gunUser.getReloadingGun()) {
                cooldownManager.removeCooldown(gunUser.getReloadingGun().getItem());
                gunUser.setGunState(GunHelper.GunStates.NORMAL);
                gunUser.setCanReload(true);
            }

            if (gunUser.getGunState().equals(GunHelper.GunStates.NORMAL) && !gunUser.getReloadingGun().isEmpty()) {
                cooldownManager.removeCooldown(gunUser.getReloadingGun().getItem());
                gunUser.setReloadingGun(ItemStack.EMPTY);
            }


    }

    public static void handleShooting(Player player, IGunUser gunUser) {

            if (
                    gunUser.canShoot() && gunUser.isShooting()
            ) {

            }

    }
}
