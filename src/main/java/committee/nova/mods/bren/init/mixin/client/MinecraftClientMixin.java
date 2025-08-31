package committee.nova.mods.bren.init.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import committee.nova.mods.bren.client.renderer.RecoilSys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(Minecraft.class)
public abstract class MinecraftClientMixin {

    @Inject(at = @At("TAIL"), method = "runTick")
    private void render(boolean tick, CallbackInfo ci) {
        RecoilSys.render((Minecraft) (Object) this);
    }
}
