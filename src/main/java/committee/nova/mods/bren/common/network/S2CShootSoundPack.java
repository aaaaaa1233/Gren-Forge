package committee.nova.mods.bren.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import committee.nova.mods.bren.init.registry.SoundReg;

import java.util.function.Supplier;

/**
 * S2CTotemPacket
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/28 14:02
 */
public class S2CShootSoundPack {
    private final float volume;


    public S2CShootSoundPack(FriendlyByteBuf buf) {
        this.volume = buf.readFloat();
    }

    public S2CShootSoundPack(float volume) {
        this.volume = volume;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeFloat(this.volume);
    }

    public void run(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var client = Minecraft.getInstance();
            var world = client.level;
            if (world != null) {

                var soundInstance = SimpleSoundInstance.forUI(SoundReg.ITEM_DISTANT_GUNFIRE, 1.0F - (world.getRandom().nextFloat() - 0.5F)/8, volume);
                client.getSoundManager().play(soundInstance);
            }
        });
        ctx.get().setPacketHandled(true);
    }


}
