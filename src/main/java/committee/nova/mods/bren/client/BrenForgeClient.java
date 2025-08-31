package committee.nova.mods.bren.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import committee.nova.mods.bren.client.renderer.RecoilSys;
import committee.nova.mods.bren.client.renderer.WeaponTickHolder;
import committee.nova.mods.bren.Bren;
import committee.nova.mods.bren.common.network.C2SReloadPack;
import committee.nova.mods.bren.init.handler.NetworkHandler;
import committee.nova.mods.bren.init.registry.KeyBindingReg;

/**
 * @author: cnlimiter
 */
@Mod.EventBusSubscriber(modid = Bren.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BrenForgeClient {
    @SubscribeEvent
    public static void onClientTickStart(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        WeaponTickHolder.tick(Minecraft.getInstance());
    }
    @SubscribeEvent
    public static void onClientTickEnd(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        Level level = mc.level;
        if (player == null || level == null) return;

        while (KeyBindingReg.reloadKey.consumeClick()) {
            NetworkHandler.CHANNEL.sendToServer(new C2SReloadPack());
        }

        RecoilSys.tick(mc);
    }
}
