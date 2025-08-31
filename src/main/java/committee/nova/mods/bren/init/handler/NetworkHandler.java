package committee.nova.mods.bren.init.handler;


import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import committee.nova.mods.bren.Bren;
import committee.nova.mods.bren.common.network.*;

import java.util.Optional;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/4/2 13:07
 * Version: 1.0
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NetworkHandler {
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(Bren.MODID, "main"), () -> {
        return "1.0";
    }, (s) -> {
        return true;
    }, (s) -> {
        return true;
    });
    public static int id = 0;

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        CHANNEL.registerMessage(id++, S2CShotPack.class, S2CShotPack::write, S2CShotPack::new, S2CShotPack::run, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(id++, S2CRecoilPack.class, S2CRecoilPack::write, S2CRecoilPack::new, S2CRecoilPack::run, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(id++, S2CShootAnimationlPack.class, S2CShootAnimationlPack::write, S2CShootAnimationlPack::new, S2CShootAnimationlPack::run, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(id++, S2CShootSoundPack.class, S2CShootSoundPack::write, S2CShootSoundPack::new, S2CShootSoundPack::run, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(id++, C2SReloadPack.class, C2SReloadPack::write, C2SReloadPack::new, C2SReloadPack::run, Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }
}
