package committee.nova.mods.bren.init.registry;


import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import committee.nova.mods.bren.Bren;
import committee.nova.mods.bren.common.enchantment.*;

public class EnchantmentReg {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Bren.MODID);

    public static RegistryObject<Enchantment> OVERFLOW = ENCHANTMENTS.register("overflow", () -> new OverflowEnchantment(Enchantment.Rarity.RARE));
    public static RegistryObject<Enchantment> AUTOFILL = ENCHANTMENTS.register("autofill", () -> new AutofillEnchantment(Enchantment.Rarity.VERY_RARE));
    public static RegistryObject<Enchantment> SILENCED = ENCHANTMENTS.register("silenced", () -> new SilencedEnchantment(Enchantment.Rarity.UNCOMMON));
    public static RegistryObject<Enchantment> STEADY_HANDS = ENCHANTMENTS.register("steady_hands", () -> new SteadyHandsEnchantment(Enchantment.Rarity.RARE));
    public static RegistryObject<Enchantment> FIRE_LANCE = ENCHANTMENTS.register("fire_lance", () -> new FireLanceEnchantment(Enchantment.Rarity.RARE));
    public static RegistryObject<Enchantment> MOUNTED = ENCHANTMENTS.register("mounted", () -> new MountedEnchantment(Enchantment.Rarity.RARE));

}
