package committee.nova.mods.bren.init.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import committee.nova.mods.bren.Bren;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class VillagersReg {
    public static final DeferredRegister<PoiType> POINTS_OF_INTEREST = DeferredRegister
            .create(ForgeRegistries.POI_TYPES, Bren.MODID);

    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister
            .create(ForgeRegistries.VILLAGER_PROFESSIONS, Bren.MODID);

    public static final RegistryObject<PoiType> GUNSMITH_POI = POINTS_OF_INTEREST.register("gunsmith_poi",
            () -> createPoiType(BlockReg.WORKBENCH.get()));

    public static final RegistryObject<VillagerProfession> GUNSMITH = PROFESSIONS.register("gunsmith",
            () -> {
                Predicate<Holder<PoiType>> isPoi = (poi) -> poi.is(Objects.requireNonNull(GUNSMITH_POI.getKey()));
                return new VillagerProfession("gunsmith", isPoi,
                        isPoi, ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_FLETCHER);
            });

    private static PoiType createPoiType(Block... blocks) {
        Collection<BlockState> blockStates = ImmutableSet.copyOf(Stream.of(blocks).map(x -> x.getStateDefinition().getPossibleStates()).flatMap(ImmutableList::stream).toArray(BlockState[]::new));
        return new PoiType(ImmutableSet.copyOf(blockStates), 1, 1);
    }

    public static void registerPointOfInterests() {
        //PoiTypes.registerBlockStates(SKI_MERCHANT_POI.getHolder().orElseThrow(), createPoiType(Blocks.JUKEBOX));
    }
}
