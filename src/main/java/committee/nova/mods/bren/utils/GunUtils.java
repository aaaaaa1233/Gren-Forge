package committee.nova.mods.bren.utils;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import committee.nova.mods.bren.Bren;
import committee.nova.mods.bren.common.network.*;
import committee.nova.mods.bren.init.config.MConfig;
import committee.nova.mods.bren.common.entity.BulletEntity;
import committee.nova.mods.bren.common.entity.IGunUser;
import committee.nova.mods.bren.init.handler.NetworkHandler;
import committee.nova.mods.bren.init.registry.AttributeReg;
import committee.nova.mods.bren.init.registry.EnchantmentReg;
import committee.nova.mods.bren.init.registry.ItemReg;
import committee.nova.mods.bren.common.item.GunItem;
import committee.nova.mods.bren.common.item.MagazineItem;

import java.util.ArrayList;
import java.util.List;


public class GunUtils {

    public static int fire(LivingEntity user) {

        var world = user.level();
        var stack = user.getMainHandItem();
        IGunUser gunUser = (IGunUser)user;

        if (!(stack.getItem() instanceof GunItem gunItem)) return 0;

        if (!gunUser.getGunState().equals(GunHelper.GunStates.NORMAL)) return 0;

        boolean silenced = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentReg.SILENCED.get(), stack) >= 1;

        world.playSound(null,
                user.getX(),
                user.getY(),
                user.getZ(),
                silenced ? gunItem.getSilentShootSound() : gunItem.getShootSound(),
                SoundSource.PLAYERS, silenced ? 0.5F : 5.0F,1.0F - (user.getRandom().nextFloat() - 0.5F)/8);

        if (!silenced) {
            GunUtils.playDistantGunFire(world, user.position());
        }

        int fireRate = (int)Math.round(user.getAttributeValue(AttributeReg.FIRE_RATE.get()));

        if (!world.isClientSide()) {

            if (user instanceof Player) {
                stack.hurtAndBreak(1, user, (p) -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
            }

            List<Vec3> position = GunUtils.calculatePositionBasedOnAngle(user);
            Vec3 origin = position.get(0);
            Vec3 front = position.get(1);
            Vec3 down = position.get(2);
            Vec3 side = position.get(3);

            for (Player p : user.level().players()) {
                if (p instanceof ServerPlayer serverPlayer)
                    NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new S2CShotPack(origin.add(side.add(down).scale(0.15)), front, gunItem.ejectCasing()));
            }

            int level = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentReg.FIRE_LANCE.get(), stack);

            for (int i = 0; i < gunItem.bulletAmount(); ++i) {
                float x = (user.getRandom().nextFloat() - .5f) * 2 * gunItem.spread();
                float y = (user.getRandom().nextFloat() - .5f) * 2 * gunItem.spread();

                GunUtils.spawnBullet(user, origin, front, stack, new Vec2(x,y), level * 6, gunItem.bulletSpeed(), gunItem.bulletLifespan());
            }
        }



        if (user instanceof ServerPlayer player) {

            double recoil = user.getAttributeValue(AttributeReg.RECOIL.get());
            recoil = CalculateRecoil(player, stack, recoil);

            NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new S2CRecoilPack((float)recoil));
        }

        gunItem.useBullet(stack);
        return fireRate;
    }

    public static double CalculateRecoil(Player player, ItemStack stack, double baseRecoil) {
        baseRecoil *= MConfig.recoilMultiplier.get();
        baseRecoil /= ((EnchantmentHelper.getItemEnchantmentLevel(EnchantmentReg.STEADY_HANDS.get(), stack) * 2.6d * 0.1d) + 1);
        baseRecoil = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentReg.MOUNTED.get(), stack) == 1 && player.isCrouching() ? baseRecoil / 2 : baseRecoil;
        return Math.round(baseRecoil * 2) / 2.0;
    }

    public static List<Vec3> calculatePositionBasedOnAngle(LivingEntity entity) {
        Vec3 front = Vec3.directionFromRotation(entity.getXRot(), entity.getYRot());
        var arm = entity.getMainArm();
        boolean isRightHand = arm == HumanoidArm.RIGHT;
        Vec3 side = Vec3.directionFromRotation(0, entity.getYRot() + (isRightHand ? 90 : -90));
        Vec3 down = Vec3.directionFromRotation(entity.getXRot() + 90, entity.getYRot());

        Vec3 origin = new Vec3(entity.getX(), entity.getEyeY(), entity.getZ());

        List<Vec3> positions = new ArrayList<>();
        positions.add(origin);
        positions.add(front);
        positions.add(down);
        positions.add(side);
        return positions;
    }

    public static void spawnBullet(LivingEntity entity, Vec3 origin, Vec3 front, ItemStack stack, Vec2 spread,
                                   int ticksOnFire, float speed, int lifespan) {

        var world = entity.level();

        float rangedDamage = (float)entity.getAttributeValue(AttributeReg.RANGED_DAMAGE.get());

        BulletEntity bullet = BulletEntity.create(world, rangedDamage, lifespan, entity);

        Vec3 bulletPos = origin.subtract(new Vec3(0,0.2,0)).subtract(front.scale(speed));

        bullet.setPos(bulletPos.x(), bulletPos.y() - 0.1, bulletPos.z());
        bullet.shootFromRotation(entity, entity.getXRot() + spread.y, entity.getYHeadRot() + spread.x, 0.0F, speed, 0.0F);

        bullet.hurtMarked = true;
        bullet.hasImpulse = true;
        bullet.setRemainingFireTicks(ticksOnFire);

        world.addFreshEntity(bullet);
    }

    public static void sendAnimationPacket(Player player) {
        if (player instanceof ServerPlayer serverPlayer)
            NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new S2CShootAnimationlPack());
    }

    public static void playDistantGunFire(Level world, Vec3 pos) {
        if (world.isClientSide()) {
            return;
        }

        world.players().forEach(player -> {
            double distance = player.distanceToSqr(pos);

            if (distance > 60) {

                float volume = (float) Math.max(1.0F - (distance / 400)/100, 0);

                if (volume > 0 && player instanceof ServerPlayer serverPlayer) {
                    NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new S2CShootSoundPack(volume));
                }
            }
        });
    }


    public static void fillMagazine(ItemStack mag, Player player) {
        while (mag.getItem() instanceof MagazineItem) {
            ItemStack bulletStack = Bren.getItemFromPlayer(player, ItemReg.BULLET.get());

            if (bulletStack.isEmpty()) break;
            if (MagazineItem.getContents(mag) >= MagazineItem.getMaxCapacity(mag)) {
                break;
            } else {
               int i = MagazineItem.fillMagazine(mag, bulletStack.getCount());
               bulletStack.shrink(i);
            }
        }
    }
}
