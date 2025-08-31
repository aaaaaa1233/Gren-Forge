package committee.nova.mods.bren.common.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import committee.nova.mods.bren.Bren;
import committee.nova.mods.bren.common.entity.IGunUser;
import committee.nova.mods.bren.init.registry.SoundReg;
import committee.nova.mods.bren.utils.GunHelper;

public class GunWithMagItem extends GunItem{

    private final TagKey<Item> compatibleMagazines;

    public GunWithMagItem(Properties settings, Tier material, TagKey<Item> compatibleMagazines, GunProperties gunProperties) {
        super(settings, material, gunProperties);
        this.compatibleMagazines = compatibleMagazines;
    }

    @Override
    public void onReload(Player player) {
        var stack = player.getMainHandItem();
        var cooldownManager = player.getCooldowns();

        if (stack.getItem() instanceof GunWithMagItem gunItem) {

            if (player instanceof IGunUser gunUser && !cooldownManager.isOnCooldown(stack.getItem())) {

                var mag = Bren.getMagazineFromPlayer(player, gunItem.compatibleMagazines());

                if (!GunWithMagItem.hasMagazine(stack) && mag.isEmpty()) {
                    return;
                }

                if (!gunUser.canReload()) {
                    return;
                }
                gunUser.setCanReload(false);
                gunUser.setGunState(GunHelper.GunStates.RELOADING);
                cooldownManager.addCooldown(stack.getItem(), this.reloadSpeed());
            }
        }
    }

    public static void putMagazine(ItemStack stack, ItemStack mag) {
        if (!(mag.getItem() instanceof MagazineItem)) {
            return;
        }

        CompoundTag magNBT = new CompoundTag();

        mag.save(magNBT);
        magNBT.remove("Count");

        stack.getOrCreateTag().putInt("MaxMagazineCapacity", MagazineItem.getMaxCapacity(mag));
        stack.getOrCreateTag().put("Magazine", magNBT);
    }

    public static CompoundTag getMagazineNBT(ItemStack stack) {
        if (stack.getTag() != null) {
            return stack.getTag().getCompound("Magazine");
        }
        return new CompoundTag();
    }

    @Override
    public int getMaxCapacity(ItemStack stack) {
        if (stack.getTag() != null) {
            return stack.getTag().getInt("MaxMagazineCapacity");
        }
        return 0;
    }

    @Override
    public int getContents(ItemStack stack) {
        if (stack.getTag() != null && hasMagazine(stack)) {
            CompoundTag nbtCompound = getMagazineNBT(stack);
            return MagazineItem.getContentsNBT(nbtCompound);
        }
        return 0;
    }

    public static boolean hasMagazine(ItemStack stack){
        if (stack.getTag() != null) {
            CompoundTag nbtCompound = getMagazineNBT(stack);
            if (nbtCompound == null) return false;

            return !nbtCompound.isEmpty();
        }
        return false;
    }

    public static boolean hasColorableMagazine(ItemStack stack) {
        CompoundTag getMagNBT = getMagazineNBT(stack);
        if (!getMagNBT.isEmpty()) {
            CompoundTag getSubNBT = getMagNBT.getCompound("display");
            if (!getSubNBT.isEmpty()) {
                return getSubNBT.contains("color", 99);
            }

            String id = getMagNBT.getString("id");
            ResourceLocation identifier = new ResourceLocation(id);

            ItemStack mag = new ItemStack(ForgeRegistries.ITEMS.getValue(identifier));
            return mag.getItem() instanceof ColorableMagazineItem;
        }


        return false;
    }

    public static int getMagazineColor(ItemStack stack) {
        CompoundTag getMagNBT = getMagazineNBT(stack);
        if (!getMagNBT.isEmpty()) {
            CompoundTag getSubNBT = getMagNBT.getCompound("tag").getCompound("display");
            if (!getSubNBT.isEmpty()) {
                return getSubNBT.contains("color", 99) ? getSubNBT.getInt("color") : 10511680;
            }
        }
        return hasColorableMagazine(stack) ? 10511680 : -1;
    }

    public int getColor(ItemStack stack) {
        return getMagazineColor(stack);
    }

    public static ItemStack getMagazine(ItemStack stack) {
        boolean hasMag = hasMagazine(stack);
        if (hasMag) {

            CompoundTag nbtCompound = getMagazineNBT(stack);
            if (!nbtCompound.isEmpty()) {

                String id = nbtCompound.getString("id");
                ResourceLocation identifier = new ResourceLocation(id);

                ItemStack mag = new ItemStack(ForgeRegistries.ITEMS.getValue(identifier));
                mag.setTag(nbtCompound.getCompound("tag"));
                return mag;
            }
        }
        return ItemStack.EMPTY;
    }


    public static ItemStack unloadMagazine(ItemStack stack, Player player) {
        if (!(stack.getItem() instanceof GunItem)) {
            return ItemStack.EMPTY;
        }

        ItemStack mag = getMagazine(stack);
        if (!mag.isEmpty()) {

            int empty_slot = player.getInventory().getFreeSlot();
            if (empty_slot != -1) {
                player.getInventory().setItem(empty_slot, mag);
            } else {
                ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getEyePosition().y(), player.getZ(), mag);
                itemEntity.setThrower(player.getUUID());
                player.level.addFreshEntity(itemEntity);
            }

            stack.getTag().put("Magazine", new CompoundTag());

            return mag;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isEmpty(ItemStack stack) {
        boolean b = false;

        if (stack.getItem() instanceof GunItem gunItem) {
            b = gunItem.getContents(stack) <= 0;
        }

        return b || !hasMagazine(stack);
    }
    @Override
    public void useBullet(ItemStack stack) {
        if (stack.getTag() != null) {
            CompoundTag nbtCompound = getMagazineNBT(stack);
            MagazineItem.removeOneContent(nbtCompound);
        }
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
        } else if (cooldownManager.getCooldownPercent(stack.getItem(),1) == 0.75F && !GunWithMagItem.hasMagazine(stack)) {
            world.playSound(null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundReg.ITEM_MAGAZINE_INSERT,
                    SoundSource.PLAYERS, 1.0F, 1.0F - (player.getRandom().nextFloat() - 0.5F) / 4);
        }

    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {

        if (entity instanceof IGunUser gunUser && entity instanceof Player player) {

            if (selected) {


            }
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    public TagKey<Item> compatibleMagazines() {
        return this.compatibleMagazines;
    }
}
