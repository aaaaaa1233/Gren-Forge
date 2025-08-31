package committee.nova.mods.bren.init.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import committee.nova.mods.bren.common.entity.IGunUser;
import committee.nova.mods.bren.init.handler.PlayerHandler;
import committee.nova.mods.bren.init.registry.AttributeReg;
import committee.nova.mods.bren.common.item.GunItem;
import committee.nova.mods.bren.utils.GunHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements IGunUser {
    private boolean canReload = true;
    private ItemStack reloadingGun = ItemStack.EMPTY;
    private ItemStack lastGun = ItemStack.EMPTY;
    private ItemStack lastEquippedGun = ItemStack.EMPTY;
    private boolean lastGunLoaded = false;
    private int shootingDur = 0;
    private boolean canShoot = false;

    protected PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public boolean isShooting() {
        return this.getUseItem().getItem() instanceof GunItem;
    }

    @Override
    public int shootingDuration() {
        return this.shootingDur;
    }

    @Override
    public void setShootingDuration(int t) {
        this.shootingDur = t;
    }

    @Override
    public boolean canShoot() {
        return this.canShoot;
    }

    public void setCanShoot(boolean canShoot) {
        this.canShoot = canShoot;
    }

    @Override
    public void setReloadingGun(ItemStack reloadingGun) {
        this.reloadingGun = reloadingGun;
    }

    @Override
    public ItemStack getReloadingGun() {
        return reloadingGun;
    }

    @Override
    public ItemStack getLastEquipGun() {
        return this.lastEquippedGun;
    }

    @Override
    public void setLastEquipGun(ItemStack lastEquipGun) {
        this.lastEquippedGun = lastEquipGun;
    }

    @Override
    public boolean canReload() {
        return this.canReload;
    }

    @Override
    public void setCanReload(boolean canReload) {
        this.canReload = canReload;
    }

    @Override
    public ItemStack getLastGun() {
        return this.lastGun;
    }

    @Override
    public void setLastGun(ItemStack lastGun) {
        this.lastGun = lastGun;
    }

    @Override
    public int getGunTicks() {
        return this.entityData.get(PlayerHandler.GUN_TICKS);
    }

    @Override
    public void setGunTicks(int ticks) {
        this.entityData.set(PlayerHandler.GUN_TICKS, Math.max(ticks, 0));
    }

    @Override
    public GunHelper.GunStates getGunState() {
        return GunHelper.getGunState(this.entityData);
    }

    @Override
    public void setGunState(GunHelper.GunStates state) {
        GunHelper.setGunState(state, this.entityData);
    }

    @Override
    public boolean lastGunLoaded() {
        return this.lastGunLoaded;
    }

    @Override
    public void setLastGunLoaded(boolean lastGunLoaded) {
        this.lastGunLoaded = lastGunLoaded;
    }

    @Inject(at = @At("RETURN"), method = "createAttributes")
    private static void createPlayerAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.getReturnValue()
                .add(AttributeReg.RANGED_DAMAGE.get(), 0d)
                .add(AttributeReg.FIRE_RATE.get(), 0d)
                .add(AttributeReg.RECOIL.get(), 0d);
    }

    @Inject(at = @At("TAIL"), method = "aiStep")
    public void tickMovement(CallbackInfo ci) {
        this.setGunTicks(this.getGunTicks() - 1);
    }

    @Inject(at = @At("TAIL"), method = "defineSynchedData")
    private void dataTracker(CallbackInfo ci) {
        this.entityData.define(PlayerHandler.GUN_TICKS, 0);
        this.entityData.define(PlayerHandler.LAST_GUN_NBT, new CompoundTag());
        GunHelper.dataTracker(this.entityData);
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    private void readCustomDataFromNbt(CompoundTag nbt, CallbackInfo ci) {
        CompoundTag itemNbt = nbt.getCompound("LastGun");

        if (!itemNbt.isEmpty()) {
            this.entityData.set(PlayerHandler.LAST_GUN_NBT, itemNbt);
        }
        GunHelper.readCustomDataFromNbt(nbt, this.entityData);
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    private void writeCustomDataToNbt(CompoundTag nbt, CallbackInfo ci) {
        if (this.getLastGun().hasTag()) {
            CompoundTag nbtCompound = new CompoundTag();
            this.getLastGun().save(nbtCompound);
            nbt.put("LastGun", nbtCompound);
        }
        GunHelper.writeCustomDataToNbt(nbt, this.entityData);
    }
}
