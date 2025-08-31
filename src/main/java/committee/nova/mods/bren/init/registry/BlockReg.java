package committee.nova.mods.bren.init.registry;


import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import committee.nova.mods.bren.Bren;
import committee.nova.mods.bren.common.block.WorkbenchBlock;

import java.util.function.Supplier;

public class BlockReg {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Bren.MODID);

    public static final RegistryObject<Block> WORKBENCH = baseBlock("workbench", () -> new WorkbenchBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).sound(SoundType.LANTERN).instrument(NoteBlockInstrument.BASEDRUM).strength(3.5F)));

    private static RegistryObject<Block> baseBlock(String name, Supplier<Block> block) {
        return BLOCKS.register(name, block);
    }



    public static void reg() {
    }
}
