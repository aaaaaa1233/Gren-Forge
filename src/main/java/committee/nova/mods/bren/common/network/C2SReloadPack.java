package committee.nova.mods.bren.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import committee.nova.mods.bren.common.entity.IGunUser;
import committee.nova.mods.bren.common.item.MagazineItem;
import committee.nova.mods.bren.common.item.GunItem;
import committee.nova.mods.bren.utils.GunUtils;

import java.util.function.Supplier;

/**
 * C2SReloadPack
 *
 * @author cnlimiter
 */
public class C2SReloadPack {

    public C2SReloadPack(FriendlyByteBuf buf) {
    }

    public C2SReloadPack() {
    }

    public void write(FriendlyByteBuf buf) {
    }

    public void run(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var player = ctx.get().getSender();
            ItemStack stack = player.getMainHandItem();

            if (stack.getItem() instanceof GunItem gunItem) {
                gunItem.onReload(player);
                ((IGunUser) player).setReloadingGun(stack);
            } else if (stack.getItem() instanceof MagazineItem) {
                GunUtils.fillMagazine(stack, player);
            }
        });
        ctx.get().setPacketHandled(true);
    }


}
