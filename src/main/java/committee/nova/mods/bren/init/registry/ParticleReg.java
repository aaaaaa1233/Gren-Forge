package committee.nova.mods.bren.init.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import committee.nova.mods.bren.Bren;

public class ParticleReg {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPE = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Bren.MODID);
    
    public static final RegistryObject<SimpleParticleType> MUZZLE_SMOKE_PARTICLE = PARTICLE_TYPE.register("muzzle_smoke", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> AIR_RING_PARTICLE = PARTICLE_TYPE.register("air_ring", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> CASING_PARTICLE = PARTICLE_TYPE.register("casing", () -> new SimpleParticleType(true));

}
