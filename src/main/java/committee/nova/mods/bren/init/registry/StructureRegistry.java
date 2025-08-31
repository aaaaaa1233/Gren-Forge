package committee.nova.mods.bren.init.registry;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import committee.nova.mods.bren.Bren;
import committee.nova.mods.bren.init.mixin.StructurePoolAccessor;

import java.util.ArrayList;
import java.util.List;

public class StructureRegistry {
    private static final ResourceKey<StructureProcessorList> EMPTY_PROCESSOR_LIST_KEY = ResourceKey.create(Registries.PROCESSOR_LIST, new ResourceLocation("minecraft", "empty"));
    private static final ResourceLocation plainsPoolLocation = new ResourceLocation("minecraft:village/plains/houses");
    private static final ResourceLocation desertPoolLocation = new ResourceLocation("minecraft:village/desert/houses");
    private static final ResourceLocation savannaPoolLocation = new ResourceLocation("minecraft:village/savanna/houses");
    private static final ResourceLocation snowyPoolLocation = new ResourceLocation("minecraft:village/snowy/houses");
    private static final ResourceLocation taigaPoolLocation = new ResourceLocation("minecraft:village/taiga/houses");

    public static void registerJigsaws(MinecraftServer server) {
        Registry<StructureTemplatePool> templatePoolRegistry = server.registryAccess().registryOrThrow(Registries.TEMPLATE_POOL);
        Registry<StructureProcessorList> processorListRegistry = server.registryAccess().registryOrThrow(Registries.PROCESSOR_LIST);

        addBuildingToPool(templatePoolRegistry, processorListRegistry, plainsPoolLocation, Bren.MODID + ":village/plains/houses/plains_gun_store_0", 5);
        addBuildingToPool(templatePoolRegistry, processorListRegistry, desertPoolLocation, Bren.MODID + ":village/desert/houses/desert_gun_store_0", 5);
        addBuildingToPool(templatePoolRegistry, processorListRegistry, savannaPoolLocation, Bren.MODID + ":village/savanna/houses/savanna_gun_store_0", 5);
        addBuildingToPool(templatePoolRegistry, processorListRegistry, snowyPoolLocation, Bren.MODID + ":village/snowy/houses/snowy_gun_store_0", 5);
        addBuildingToPool(templatePoolRegistry, processorListRegistry, taigaPoolLocation, Bren.MODID + ":village/taiga/houses/taiga_gun_store_0", 5);
    }

    public static void addBuildingToPool(Registry<StructureTemplatePool> templatePoolRegistry, Registry<StructureProcessorList> processorListRegistry, ResourceLocation poolRL, String nbtPieceRL, int weight) {
        var processorList = processorListRegistry.getHolderOrThrow(EMPTY_PROCESSOR_LIST_KEY);

        StructureTemplatePool pool = templatePoolRegistry.get(poolRL);
        if (pool == null) return;

        SinglePoolElement piece = SinglePoolElement.single(nbtPieceRL, processorList).apply(StructureTemplatePool.Projection.RIGID);

        for (int i = 0; i < weight; i++) {
            ((StructurePoolAccessor) pool).getTemplates().add(piece);
        }

        List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = new ArrayList<>(((StructurePoolAccessor) pool).getRawTemplates());
        listOfPieceEntries.add(new Pair<>(piece, weight));
        ((StructurePoolAccessor) pool).setRawTemplates(listOfPieceEntries);
    }
}
