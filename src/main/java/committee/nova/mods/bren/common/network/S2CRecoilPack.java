package committee.nova.mods.bren.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import committee.nova.mods.bren.client.renderer.RecoilSys;
import committee.nova.mods.bren.client.renderer.WeaponTickHolder;

import java.util.function.Supplier;

/**
 * S2CRecoilPack
 *
 * @author cnlimiter
 */
public class S2CRecoilPack {
    private final float recoil;


    public S2CRecoilPack(FriendlyByteBuf buf) {
        this.recoil = buf.readFloat();
    }

    public S2CRecoilPack(float recoil) {
        this.recoil = recoil;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeFloat(this.recoil);
    }

    public void run(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (Minecraft.getInstance().player ==  null) {return;}
            RecoilSys.shotEvent(Minecraft.getInstance().player, recoil);
        });
        ctx.get().setPacketHandled(true);
    }


}
