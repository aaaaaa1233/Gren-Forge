package committee.nova.mods.bren.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import committee.nova.mods.bren.init.registry.SoundReg;
import org.jetbrains.annotations.Nullable;

public class CasingParticle extends BaseAshSmokeParticle {

    protected float bounce = 0.9f;
    protected boolean made_sound = false;

    protected CasingParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, float scaleMultiplier, SpriteSet spriteProvider) {
        super(world, x, y, z, 0.1F, 0.1F, 0.1F, velocityX, velocityY, velocityZ, scaleMultiplier, spriteProvider, 1.0F, 32, 2.0F, true);
        this.roll = (float) (Math.PI * world.getRandom().nextFloat());
        this.setColor(2.0F,2.0F,2.0F);
    }

    @Override
    public void tick() {
        super.tick();
        this.oRoll = this.roll;
        if (!this.onGround) {
            this.roll += 0.3f;
        } else {
            this.yd += this.bounce;
            this.bounce = 0;
            if (!this.made_sound) {
                this.made_sound = true;

                this.level.playLocalSound(this.x, this.y, this.z, SoundReg.PARTICLE_CASING_BOUNCE, SoundSource.BLOCKS, 1.0F,1.0F - (this.level.getRandom().nextFloat() - 0.5F)/8, false);
            }
        }
    }

    public ParticleRenderType getType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new CasingParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, 1.8F, this.spriteProvider);
        }
    }
}
