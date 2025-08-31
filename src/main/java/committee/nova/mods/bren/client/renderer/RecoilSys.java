package committee.nova.mods.bren.client.renderer;


import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class RecoilSys {

    private static float cameraRecoil = 0;
    private static float sideRecoil = 0;
    private static float recoil = 0;
    private static int cameraRecoilProgress = 0;
    private static int lastCameraRecoilProgress = 0;

    public static void shotEvent(Player player, float cam_recoil) {
        cameraRecoil = cam_recoil;
        sideRecoil = (player.getRandom().nextFloat() - .5F) / 2;
        cameraRecoilProgress = 2;
        recoil = 0;
    }

    public static void render(Minecraft client) {
        var player = client.player;

        if (player == null) { return;}

        float progress = Mth.lerp(client.getFrameTime(), lastCameraRecoilProgress, cameraRecoilProgress);

        float pitch = player.getXRot();
        float yaw = player.getYRot();

        recoil = progress * cameraRecoil * client.getDeltaFrameTime();

        player.setXRot(pitch - (Float.isNaN(recoil) ? .0F : recoil));
        player.setYRot(yaw - (Float.isNaN(recoil * sideRecoil) ? .0F : recoil * sideRecoil));
        player.xRotO = pitch;
    }

    public static void tick(Minecraft client) {
        lastCameraRecoilProgress = cameraRecoilProgress;
        cameraRecoilProgress = Math.max(0, --cameraRecoilProgress);
    }
}
