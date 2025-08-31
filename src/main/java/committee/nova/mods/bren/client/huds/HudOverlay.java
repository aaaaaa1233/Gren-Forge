package committee.nova.mods.bren.client.huds;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import committee.nova.mods.bren.Bren;
import committee.nova.mods.bren.common.item.GunItem;
import committee.nova.mods.bren.common.item.GunWithMagItem;

public class HudOverlay implements IGuiOverlay {
    private static final ResourceLocation BULLET_ICONS = new ResourceLocation(Bren.MODID,
            "textures/gui/bullet_icons.png");


    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Player player = null;
        Minecraft client = Minecraft.getInstance();

        int width =  client.getWindow().getGuiScaledWidth();
        int height = client.getWindow().getGuiScaledHeight();

        int x = width / 2;
        int y = height/ 2;

        if (client != null) {
            player = client.player;
        }

        if (player == null) {
            return;
        }

        ItemStack gun = player.getMainHandItem();
        int i;
        int u;
        int max;
        if (gun.getItem() instanceof GunItem gunItem) {
            i = gunItem.getContents(gun);
            u = gunItem.bulletAmount() > 1 ? 12 : 0;
            max = gunItem.getMaxCapacity(gun);

            if (gunItem instanceof GunWithMagItem) {
                if (!GunWithMagItem.hasMagazine(gun)) {
                    return;
                }
            }

        } else {
            return;
        }




        client.getProfiler().push("machine_gun_bullets");

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();

        int rows = 2;

        for (int n = 0; n < max; ++n) {

            int ri = rows * 10;

            int row = (int) Math.floor(n/ri);

            int y1 = n * 6 - row*ri*6;
            int x1 = 15*row + 15;

            int u1 = n < i ? 0 : 24;

            addBulletIcon(guiGraphics,x1, y1, u + u1, 0);
        }

        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();

        client.getProfiler().pop();
    }

    public void addBulletIcon(GuiGraphics context, int x, int y,int u, int v) {
        context.blit(BULLET_ICONS, x, y, u, v, 12, 12, 48, 12);
    }


}
