package committee.nova.mods.bren.init.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import committee.nova.mods.bren.Bren;
import committee.nova.mods.bren.common.item.GunItem;
import committee.nova.mods.bren.common.item.GunWithMagItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(GuiGraphics.class)
public abstract class DrawContextMixin {

    @Shadow public abstract void fill(RenderType layer, int x1, int y1, int x2, int y2, int color);

    //private static final Identifier CONTENT_TEXTURE = new Identifier(Bren.MODID, "test");

    @Shadow
    @Final
    private PoseStack pose;

    @Inject(at = @At("HEAD"), method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V")
    private void editGuiItem(Font pFont, ItemStack stack, int x, int y, String pText, CallbackInfo ci) {
        if (!stack.isEmpty()) {
            this.pose.pushPose();
            if (stack.getItem() instanceof GunItem gunItem) {

                if (showBar(stack)) {
                    int m = gunItem.getMaxCapacity(stack);
                    int i = m > 0 ? Math.round(((float) gunItem.getContents(stack) / m) * 13) : 0;
                    int j = Bren.UNIVERSAL_AMMO_COLOR;
                    int k = x + 2;
                    int l = y + 11;
                    this.fill(RenderType.guiOverlay(), k, l, k + 13, l + 2, -16777216);
                    this.fill(RenderType.guiOverlay(), k, l, k + i, l + 1, j | -16777216);
                }
            }
            this.pose.popPose();
        }
    }

    private static boolean showBar(ItemStack stack) {
        if (stack.getItem() instanceof GunWithMagItem) {
            return GunWithMagItem.hasMagazine(stack);
        } else if (stack.getItem() instanceof GunItem gunItem) {
            return gunItem.getContents(stack) > 0;
        }
        return true;
    }
}
