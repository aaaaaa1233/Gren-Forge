package committee.nova.mods.bren.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import committee.nova.mods.bren.common.item.GunItem;
import committee.nova.mods.bren.init.config.MConfig;

import java.util.function.Supplier;

/**
 * S2CTotemPacket
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/28 14:02
 */
public class S2CShotPack {
    private final Vec3 origin;
    private final Vec3 direction;
    private final boolean ejectCasing;

    public S2CShotPack(FriendlyByteBuf buf) {
        this.origin = new  Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.direction = new  Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.ejectCasing = buf.readBoolean();
    }

    public S2CShotPack(Vec3 origin, Vec3 direction, boolean ejectCasing) {
        this.origin = origin;
        this.direction = direction;
        this.ejectCasing = ejectCasing;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeDouble(this.origin.x);
        buf.writeDouble(this.origin.y);
        buf.writeDouble(this.origin.z);
        buf.writeDouble(this.direction.x);
        buf.writeDouble(this.direction.y);
        buf.writeDouble(this.direction.z);
        buf.writeBoolean(this.ejectCasing);
    }

    public void run(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var client = Minecraft.getInstance();
            var world = client.level;
            Vec3 origin = new Vec3(this.origin.x, this.origin.y, this.origin.z);
            Vec3 direction = new Vec3(this.direction.x, this.direction.y, this.direction.z);

            if (world != null) {
                GunItem.shotParticles(world, origin, direction, world.getRandom());
                if (MConfig.spawnCasingParticles.get() && this.ejectCasing) {
                    GunItem.ejectCasingParticle(world, origin, direction, world.getRandom());
                }
            }        });
        ctx.get().setPacketHandled(true);
    }


}
