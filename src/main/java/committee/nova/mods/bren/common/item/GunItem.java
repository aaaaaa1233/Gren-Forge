package committee.nova.mods.bren.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import committee.nova.mods.bren.Bren;
import committee.nova.mods.bren.common.entity.IGunUser;
import committee.nova.mods.bren.init.events.GunFireEvent;
import committee.nova.mods.bren.init.registry.AttributeReg;
import committee.nova.mods.bren.init.registry.ParticleReg;
import committee.nova.mods.bren.common.PoseType;
import committee.nova.mods.bren.utils.GunHelper;
import committee.nova.mods.bren.utils.GunUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class GunItem extends TieredItem implements Vanishable {
    private final Multimap<Attribute, AttributeModifier> attributeModifiers;
    private final SoundEvent shootSound;
    private final SoundEvent silentShootSound;
    private final float bulletSpeed;
    private final float recoil;

    public GunItem(Properties settings, Tier material, GunProperties gunProperties) {
        super(material, settings.defaultDurability((int) (material.getUses() * 1.5)));
        this.shootSound = gunProperties.sound;
        this.silentShootSound = gunProperties.silentSound;
        this.bulletSpeed = gunProperties.speed;

        this.recoil = gunProperties.recoil;

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(AttributeReg.RANGED_DAMAGE.get(), new AttributeModifier(AttributeReg.RANGED_DAMAGE_MODIFIER_ID, "Weapon modifier",
                gunProperties.rangedDamage, AttributeModifier.Operation.ADDITION));

        builder.put(AttributeReg.FIRE_RATE.get(), new AttributeModifier(AttributeReg.FIRE_RATE_MODIFIER_ID, "Weapon modifier",
                gunProperties.fireRate, AttributeModifier.Operation.ADDITION));

        builder.put(AttributeReg.RECOIL.get(), new AttributeModifier(AttributeReg.RECOIL_MODIFIER_ID, "Weapon modifier",
                gunProperties.recoil, AttributeModifier.Operation.ADDITION));

        this.attributeModifiers = builder.build();
    }

    public float getRecoil() {
        return this.recoil;
    }

    public boolean applyCustomMatrix(LivingEntity entity, GunHelper.GunStates state, PoseStack matrixStack, ItemStack stack, float cooldownProgress, ItemDisplayContext renderMode, boolean leftHanded) {return false;}

    public boolean hasGUIModel() {return true;}

    public boolean ejectCasing() {return true;}

    public boolean renderOnBack() {return true;}

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return this.attributeModifiers;
        }
        return super.getDefaultAttributeModifiers(slot);
    }

    public PoseType holdingPose() {
        return PoseType.TWO_ARMS;
    }

    public int getMaxCapacity(ItemStack stack) {
        return 0;
    }

    public SoundEvent getShootSound() {
        return this.shootSound;
    }

    public SoundEvent getSilentShootSound() {
        return this.silentShootSound;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.literal(getContents(stack) + " ").append(Component.translatable(String.format("desc.%s.item.gun_with_mag.content", Bren.MODID)))
                .withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, world, tooltip, context);
    }

    public void onReload(Player player) {}

    public void reloadTick(ItemStack stack, Level world, Player player, IGunUser gunUser) {}

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player user, @NotNull InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);

        if (hand == InteractionHand.OFF_HAND) {
            return InteractionResultHolder.pass(stack);
        }

        if (user instanceof IGunUser gunUser && stack.getItem() instanceof GunItem gunItem && !gunItem.isEmpty(stack)) {
            int fireRate = GunUtils.fire(user);
            if (fireRate == 0) {
                return InteractionResultHolder.pass(stack);
            }

            user.getCooldowns().addCooldown(user.getMainHandItem().getItem(), fireRate);
            gunUser.setCanShoot(true);
            user.startUsingItem(hand);
            gunUser.setGunTicks(16);
            MinecraftForge.EVENT_BUS.post(new GunFireEvent(user, stack));
            GunUtils.sendAnimationPacket(user);
        }

        return super.use(world, user, hand);
    }

    public boolean isEmpty(ItemStack stack) {
        return getContents(stack) <= 0;
    }

    public int getContents(ItemStack stack) {
        return 0;
    }

    public void useBullet(ItemStack stack) {}

    public static void shotParticles(Level world, Vec3 origin, Vec3 direction, RandomSource random) {

        for (int i = 0; i != 8; ++i) {
            double t = Math.pow(random.nextFloat(), 1.5);

            var p = origin.add(direction.scale(0.8 + t));
            var v = direction.scale(0.2 * (1 - t));
            world.addParticle(ParticleReg.MUZZLE_SMOKE_PARTICLE.get(), p.x, p.y, p.z, v.x, v.y, v.z);
        }
    }

    public static void ejectCasingParticle(Level world, Vec3 origin, Vec3 direction, RandomSource random) {
        var rotated = direction.yRot((float) (-Math.PI/2));

        var p = origin.add(direction.scale(.3f)).add(rotated.scale(.26));
        var v = rotated.scale(.15f).add(0,.5f + world.getRandom().nextFloat() * .1f,0);
        world.addParticle(ParticleReg.CASING_PARTICLE.get(), p.x, p.y, p.z, v.x, v.y, v.z);
    }


//    @Override
//    public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
//        return false;
//    }

    public int bulletLifespan() {
        return 35;
    }

    public float spread() {
        return 0.0f;
    }

    public int bulletAmount() {
        return 1;
    }

    public float bulletSpeed() {
        return this.bulletSpeed;
    }

    public int reloadSpeed() {return 20;}
}
