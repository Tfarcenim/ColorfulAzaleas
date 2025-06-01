package com.kekie6.colorfulazaleas;

import com.kekie6.colorfulazaleas.registry.AzaleaBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModDatagen {
    static void gather(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        var lookup = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();
        generator.addProvider(event.includeServer(),new Recipes(output,lookup));
        BlockTagsProvider blockTagsProvider = new BlockTags(output,lookup,helper);
        generator.addProvider(event.includeServer(),blockTagsProvider);
        generator.addProvider(event.includeServer(),new ItemTags(output,lookup,blockTagsProvider.contentsGetter(),helper));
    }

    public static class Recipes extends RecipeProvider {

        public Recipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected void buildRecipes(RecipeOutput recipeOutput) {
            for (AzaleaBlocks.ColorfulTree tree : AzaleaBlocks.trees) {
                AzaleaBlocks.WoodSet wood = tree.woodSet;
                generateRecipes(wood,recipeOutput);
            }
        }

        static void generateRecipes(AzaleaBlocks.WoodSet woodSet,RecipeOutput output) {
            buttonBuilder(woodSet.button,Ingredient.of(woodSet.planks)).unlockedBy(getHasName(woodSet.planks), has(woodSet.planks)).save(output);
            doorBuilder(woodSet.door,Ingredient.of(woodSet.planks)).unlockedBy(getHasName(woodSet.planks), has(woodSet.planks)).save(output);
            fenceBuilder(woodSet.fence,Ingredient.of(woodSet.planks)).unlockedBy(getHasName(woodSet.planks), has(woodSet.planks)).save(output);
            fenceGateBuilder(woodSet.fence_gate,Ingredient.of(woodSet.planks)).unlockedBy(getHasName(woodSet.planks), has(woodSet.planks)).save(output);
            //signBuilder(woodSet.,Ingredient.of(woodSet.planks));
            slabBuilder(RecipeCategory.BUILDING_BLOCKS, woodSet.slab,Ingredient.of(woodSet.planks))
                    .unlockedBy(getHasName(woodSet.planks), has(woodSet.planks)).save(output);
            stairBuilder(woodSet.stairs,Ingredient.of(woodSet.planks)).unlockedBy(getHasName(woodSet.planks), has(woodSet.planks)).save(output);
            pressurePlateBuilder(RecipeCategory.REDSTONE, woodSet.pressure_plate,Ingredient.of(woodSet.planks))
                    .unlockedBy(getHasName(woodSet.planks), has(woodSet.planks)).save(output);
            trapdoorBuilder(woodSet.trapdoor,Ingredient.of(woodSet.planks)).unlockedBy(getHasName(woodSet.planks), has(woodSet.planks)).save(output);

            planksFromLog(output,woodSet.planks, woodSet.logItemsTag, 4);
            woodFromLogs(output, woodSet.wood, Blocks.CHERRY_LOG);
            woodFromLogs(output,woodSet.stripped_wood, woodSet.stripped_log);
           // woodenBoat(output, Items.CHERRY_BOAT, Blocks.CHERRY_PLANKS);
           // chestBoat(output, Items.CHERRY_CHEST_BOAT, Items.CHERRY_BOAT);
        }
    }

    public static class BlockTags extends BlockTagsProvider {

        public BlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, ColorfulAzaleas.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            for (AzaleaBlocks.ColorfulTree tree : AzaleaBlocks.trees) {
                AzaleaBlocks.WoodSet wood = tree.woodSet;
                tag(wood.logBlocksTag).add(wood.log,wood.stripped_log,wood.wood,wood.stripped_wood);
            }
        }
    }

    public static class ItemTags extends ItemTagsProvider {

        public ItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags,
                        @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, blockTags, ColorfulAzaleas.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            for (AzaleaBlocks.ColorfulTree tree : AzaleaBlocks.trees) {
                AzaleaBlocks.WoodSet wood = tree.woodSet;
                copy(wood.logBlocksTag,wood.logItemsTag);
            }
        }
    }
}
