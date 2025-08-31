package committee.nova.mods.bren.init.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import committee.nova.mods.bren.Bren;
import committee.nova.mods.bren.common.entity.BulletEntity;

/**
 * @author: cnlimiter
 */
public class EntityReg {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Bren.MODID);

    public static final RegistryObject<EntityType<BulletEntity>> BULLET = ENTITIES.register("bullet", () -> EntityType.Builder.of(BulletEntity::new, MobCategory.MISC).clientTrackingRange(10)
            .sized(0.35f, 0.35f).noSave().build(new ResourceLocation(Bren.MODID, "bullet").toString()));
}
