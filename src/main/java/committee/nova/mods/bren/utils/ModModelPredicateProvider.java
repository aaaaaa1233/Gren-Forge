package committee.nova.mods.bren.utils;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import committee.nova.mods.bren.init.registry.ItemReg;
import committee.nova.mods.bren.common.item.GunWithMagItem;
import committee.nova.mods.bren.common.item.MagazineItem;

public class ModModelPredicateProvider {
    public static void regModels() {
        regGun(ItemReg.MACHINE_GUN.get(), true);
        regGun(ItemReg.AUTO_GUN.get(), true);
        regGun(ItemReg.RIFLE.get(), false);
        regGun(ItemReg.NETHERITE_MACHINE_GUN.get(), true);
        regGun(ItemReg.NETHERITE_AUTO_GUN.get(), true);
        regGun(ItemReg.NETHERITE_RIFLE.get(), false);
        regMag(ItemReg.MAGAZINE.get());
        regMag(ItemReg.CLOTHED_MAGAZINE.get());
        regMag(ItemReg.SHORT_MAGAZINE.get());
    }


    private static void regGun(Item machineGun, boolean colorable) {
        ItemProperties.register(machineGun, new ResourceLocation("has_magazine"),
                (stack, world, entity, seed) -> GunWithMagItem.hasMagazine(stack) ? 1.0f : 0.0f);
        if (colorable) {
            ItemProperties.register(machineGun, new ResourceLocation("has_colorable_magazine"),
                    (stack, world, entity, seed) -> GunWithMagItem.hasColorableMagazine(stack) ? 1.0f : 0.0f);
        }

    }
    private static void regMag(Item magazine) {
        ItemProperties.register(magazine, new ResourceLocation("is_full"),
                (stack, world, entity, seed) -> !MagazineItem.isEmpty(stack) ? 1.0f : 0.0f);
    }
}
