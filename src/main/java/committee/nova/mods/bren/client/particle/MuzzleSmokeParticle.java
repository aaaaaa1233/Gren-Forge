package committee.nova.mods.bren.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class MuzzleSmokeParticle extends BaseAshSmokeParticle {

    protected MuzzleSmokeParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, float scaleMultiplier, SpriteSet spriteProvider) {
        super(world, x, y, z, 0.1F, 0.1F, 0.1F, velocityX, velocityY, velocityZ, scaleMultiplier, spriteProvider, 1.0F, 2, 0.0F, true);
        this.setColor(1.0F,1.0F,1.0F);
    }

    @Override
    public void tick() {
        super.tick();
        float f = (float) this.age/(this.getLifetime()*2);
        this.setColor(1.0F - f, 1.0F - f, 1.0F - f);
    }

    public ParticleRenderType getType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Override
    protected int getLightColor(float pPartialTick) {
        return 15728880;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new MuzzleSmokeParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, 1.8F, this.spriteProvider);
        }
    }
}
