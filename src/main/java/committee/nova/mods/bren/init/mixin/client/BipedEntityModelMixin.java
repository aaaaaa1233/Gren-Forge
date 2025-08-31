package committee.nova.mods.bren.init.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import committee.nova.mods.bren.client.animations.GunEntityModelAnimator;
import committee.nova.mods.bren.common.item.GunItem;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(HumanoidModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity> extends AgeableListModel<T> implements ArmedModel, HeadedModel {

    @Shadow public abstract @NotNull ModelPart getHead();

    @Shadow @Final public ModelPart leftArm;

    @Shadow @Final public ModelPart rightArm;

    @Shadow @Final public ModelPart hat;

    @Shadow
    protected abstract void setupAttackAnimation(T pLivingEntity, float pAgeInTicks);

    @Inject(at = @At("TAIL"), method = "setupAnim*")
    private void angles(T livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo info) {
        var minecraftClient = Minecraft.getInstance();
        float delta = minecraftClient.getFrameTime();

        var s = livingEntity.getMainHandItem();
        if (s != null) {
            if (s.getItem() instanceof GunItem gunItem && livingEntity instanceof Player player) {

                var cooldownManager = player.getCooldowns();

                var mainHandItem = player.getMainHandItem();

                float c = cooldownManager.getCooldownPercent(mainHandItem.getItem(),delta);

                switch (gunItem.holdingPose()) {
                    case TWO_ARMS -> GunEntityModelAnimator.angles(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch,
                            this.leftArm, this.rightArm, this.getHead(), c);
                    case ONE_ARM ->  GunEntityModelAnimator.oneArm(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch,
                            this.leftArm, this.rightArm, this.getHead(), c);
                    case REVOLVER -> GunEntityModelAnimator.revolver(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch,
                            this.leftArm, this.rightArm, this.getHead(), c);
                }

                this.hat.copyFrom(this.getHead());
                this.setupAttackAnimation(livingEntity, ageInTicks);
            }
        }
    }
}
