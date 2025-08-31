package committee.nova.mods.bren.init.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import committee.nova.mods.bren.Bren;
import org.jetbrains.annotations.Nullable;

public class DamageTypeReg {
    public static final ResourceKey<DamageType> BULLET_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Bren.MODID, "bullet_type"));

    public static DamageSource of(Level world, ResourceKey<DamageType> key) {
        return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key));
    }

    public static DamageSource shot(Level world, @Nullable Entity source, @Nullable Entity attacker) {
        return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(BULLET_TYPE), source, attacker);
    }
}
