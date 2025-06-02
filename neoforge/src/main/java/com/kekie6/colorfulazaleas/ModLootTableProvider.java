package com.kekie6.colorfulazaleas;

import com.kekie6.colorfulazaleas.registry.AzaleaBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(PackOutput output, Set<ResourceKey<LootTable>> requiredTables, List<SubProviderEntry> subProviders, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, requiredTables, subProviders, registries);
    }

    public static LootTableProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        return new LootTableProvider(
                output,
                Set.of(),
                List.of(
                        new LootTableProvider.SubProviderEntry(ModBlockLoot::new, LootContextParamSets.BLOCK)
                ),
                registries
        );
    }

    @Override
    protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContext validationcontext, ProblemReporter.Collector problemreporter$collector) {
    }

    public static class ModBlockLoot extends BlockLootSubProvider {

        protected ModBlockLoot(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
        }

        @Override
        protected void generate() {

            this.add(AzaleaBlocks.DROOPING_AZALEA_LEAVES, b -> this.createLeavesDrops(b, Blocks.AZALEA, NORMAL_LEAVES_SAPLING_CHANCES));

            for (AzaleaBlocks.ColorfulTree tree : AzaleaBlocks.trees) {
                dropSelf(tree.sapling);

                this.add(tree.bloomingLeaves, b -> this.createLeavesDrops(b, tree.sapling, NORMAL_LEAVES_SAPLING_CHANCES));
                this.add(tree.floweringLeaves, b -> this.createLeavesDrops(b, tree.sapling, NORMAL_LEAVES_SAPLING_CHANCES));
                this.add(tree.droopingLeaves, b -> this.createLeavesDrops(b, tree.sapling, NORMAL_LEAVES_SAPLING_CHANCES));
                this.add(tree.azaleaLeaves, b -> this.createLeavesDrops(b, tree.sapling, NORMAL_LEAVES_SAPLING_CHANCES));
                this.dropPottedContents(tree.pottedSapling);

                AzaleaBlocks.WoodSet wood = tree.woodSet;

                dropSelf(wood.log);
                dropSelf(wood.stripped_log);
                dropSelf(wood.wood);
                dropSelf(wood.stripped_wood);

                dropSelf(wood.button);
                this.add(wood.door, this::createDoorTable);;
                dropSelf(wood.fence);
                dropSelf(wood.fence_gate);
                dropSelf(wood.planks);
                dropSelf(wood.pressure_plate);
                add(wood.slab, this::createSlabItemTable);
                dropSelf(wood.stairs);
                dropSelf(wood.trapdoor);
            }
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return BuiltInRegistries.BLOCK.stream().filter(block -> BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(ColorfulAzaleas.MOD_ID)).toList();
        }
    }
}
