package committee.nova.mods.bren.init.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import committee.nova.mods.bren.Bren;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/3/31 10:36
 * Version: 1.0
 */
public class TabReg {

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Bren.MODID);
    public static final List<RegistryObject<Item>> ACCEPT_ITEM = new ArrayList<>();


    public static final RegistryObject<CreativeModeTab> CREATIVE_TAB = TABS.register("avaritia_group", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.bren"))
            .icon(()->ItemReg.AUTO_GUN.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                for (var item : ACCEPT_ITEM) {
                    output.accept(item.get());
                }

            })
            .build());

}
