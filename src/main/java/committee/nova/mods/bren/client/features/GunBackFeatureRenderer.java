package committee.nova.mods.bren.client.features;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import committee.nova.mods.bren.common.entity.IGunUser;

public class GunBackFeatureRenderer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    private final ItemRenderer itemRenderer;

    public GunBackFeatureRenderer(RenderLayerParent<T, M> context, ItemRenderer itemRenderer) {
        super(context);
        this.itemRenderer = itemRenderer;
    }


    protected void renderItem(LivingEntity entity, ItemStack stack, ItemDisplayContext transformationMode, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
        if (stack == null) return;

        if (!stack.isEmpty()) {

            matrices.pushPose();

            ModelPart modelPart = this.getParentModel().body;
            modelPart.translateAndRotate(matrices);

            matrices.translate(0.1F,0.1F,0.25F);
            matrices.scale(1.65F,1.65F,1.0F);
            matrices.mulPose(Axis.ZP.rotationDegrees(60 + 180));
            matrices.mulPose(Axis.YP.rotationDegrees(-180));
            BakedModel bakedModel = this.itemRenderer.getModel(stack,entity.level(),entity,entity.getId() + transformationMode.ordinal());
            this.itemRenderer.render(stack,transformationMode,false,matrices,vertexConsumers,light, OverlayTexture.NO_OVERLAY,bakedModel);
            matrices.popPose();
        }
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (pLivingEntity instanceof IGunUser machineGunUser) {
            var machineGun = machineGunUser.getLastGun();
            if (!machineGun.isEmpty()) {
                this.renderItem(pLivingEntity, machineGun, ItemDisplayContext.NONE, pPoseStack, pBuffer, pPackedLight);
            }
        }
    }
}
