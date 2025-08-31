package committee.nova.mods.bren.init.registry;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import committee.nova.mods.bren.Bren;

import java.util.UUID;

public class AttributeReg {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Bren.MODID);
    public static final RegistryObject<Attribute> RANGED_DAMAGE = ATTRIBUTES.register("ranged_damage", () -> new RangedAttribute("attribute.name.ranged_damage", 0d, 0d, 2048d).setSyncable(true));
    public static final RegistryObject<Attribute> FIRE_RATE = ATTRIBUTES.register("fire_rate", () -> new RangedAttribute("attribute.name.fire_rate", 0d, 0d, 2048d).setSyncable(true));
    public static final RegistryObject<Attribute> RECOIL = ATTRIBUTES.register("recoil", () -> new RangedAttribute("attribute.name.recoil", 0d, -360d, 360d).setSyncable(true));

    public static final UUID RANGED_DAMAGE_MODIFIER_ID = UUID.fromString("EF1BE063-D502-1F12-9E55-7D827281DB27");
    public static final UUID FIRE_RATE_MODIFIER_ID = UUID.fromString("C8E578CC-5986-417E-B78D-CD4F6F3535CD");
    public static final UUID RECOIL_MODIFIER_ID = UUID.fromString("EA3C78D6-F93E-45CC-B683-4EBF2E5DE456");

}
