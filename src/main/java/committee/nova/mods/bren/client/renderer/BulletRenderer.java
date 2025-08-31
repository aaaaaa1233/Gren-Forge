package committee.nova.mods.bren.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import committee.nova.mods.bren.client.BrenModClient;
import committee.nova.mods.bren.Bren;
import committee.nova.mods.bren.common.entity.BulletEntity;

public class BulletRenderer<T extends BulletEntity> extends EntityRenderer<T> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Bren.MODID, "textures/entity/bullet.png");

    public BulletRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(T entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {

        if (entity.tickCount > 2) {
            BrenModClient.renderImage(
                    this.getTextureLocation(entity),
                    entity,
                    matrices,
                    this.entityRenderDispatcher);
        }

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public ResourceLocation getTextureLocation(BulletEntity entity) {
        return TEXTURE;
    }
}
