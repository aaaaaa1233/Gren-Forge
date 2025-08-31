package committee.nova.mods.bren.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import committee.nova.mods.bren.client.renderer.RecoilSys;
import committee.nova.mods.bren.client.renderer.WeaponTickHolder;

import java.util.function.Supplier;

/**
 * S2CTotemPacket
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/28 14:02
 */
public class S2CShootAnimationlPack {


    public S2CShootAnimationlPack(FriendlyByteBuf buf) {
    }

    public S2CShootAnimationlPack() {
    }

    public void write(FriendlyByteBuf buf) {
    }

    public void run(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            WeaponTickHolder.setTicks(16);
        });
        ctx.get().setPacketHandled(true);
    }


}
