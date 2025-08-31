package committee.nova.mods.bren.common.item;


import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;

public class MachineGunItem extends GunWithMagItem {
    public MachineGunItem(Properties settings, Tier material, TagKey<Item> compatibleMagazines, GunProperties gunProperties) {
        super(settings, material, compatibleMagazines, gunProperties);
    }

}
