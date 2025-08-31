package committee.nova.mods.bren.init.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import committee.nova.mods.bren.Bren;

public class TagReg {
    public static final TagKey<Item> MEDIUM_MAGAZINES = itemTag("magazines/medium_magazines");
    public static final TagKey<Item> SHORT_MAGAZINES = itemTag("magazines/short_magazines");
    public static final TagKey<DamageType> IS_BULLET = damageTypeTag("is_bullet");

    private static TagKey<Item> itemTag(String name) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(Bren.MODID, name));
    }
    private static TagKey<DamageType> damageTypeTag(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Bren.MODID, name));
    }
}
