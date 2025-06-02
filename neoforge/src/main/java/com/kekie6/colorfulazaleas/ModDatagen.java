package com.kekie6.colorfulazaleas;

import com.kekie6.colorfulazaleas.registry.AzaleaBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.codehaus.plexus.util.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModDatagen {
    static void gather(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        var lookup = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();
        generator.addProvider(event.includeServer(),new Recipes(output,lookup));
        BlockTagsProvider blockTagsProvider = new BlockTag(output,lookup,helper);
        generator.addProvider(event.includeServer(),blockTagsProvider);
        generator.addProvider(event.includeServer(),new ItemTags(output,lookup,blockTagsProvider.contentsGetter(),helper));
        generator.addProvider(event.includeClient(),new Lang(output));

        generator.addProvider(event.includeClient(),new BlockStates(output,helper));

        generator.addProvider(event.includeServer(),ModLootTableProvider.create(output,lookup));
    }

    public static class Recipes extends RecipeProvider {

        public Recipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected void buildRecipes(RecipeOutput recipeOutput) {
            for (AzaleaBlocks.ColorfulTree tree : AzaleaBlocks.trees) {
                generateRecipes(tree,recipeOutput);
            }
        }

        static void generateRecipes(AzaleaBlocks.ColorfulTree tree,RecipeOutput output) {
            AzaleaBlocks.WoodSet woodSet = tree.woodSet;
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

            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,tree.sapling).requires(AzaleaBlocks.FLOWERING_AZELEAS_ITEM)
                    .requires(DyeItem.byColor(tree.sapling.dyeColor))
                    .unlockedBy("has_azalea",has(AzaleaBlocks.FLOWERING_AZELEAS_ITEM))
                    .save(output);

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC,tree.azaleaLeaves)
                    .define('#',tree.floweringLeaves).define('b', Items.BONE_MEAL)
                    .pattern("###")
                    .pattern("#b#")
                    .pattern("###")
                    .unlockedBy("has_azalea_leaves",has(tree.floweringLeaves))
                    .save(output);

           // woodenBoat(output, Items.CHERRY_BOAT, Blocks.CHERRY_PLANKS);
           // chestBoat(output, Items.CHERRY_CHEST_BOAT, Items.CHERRY_BOAT);
        }
    }

    public static class BlockTag extends BlockTagsProvider {

        public BlockTag(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, ColorfulAzaleas.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            for (AzaleaBlocks.ColorfulTree tree : AzaleaBlocks.trees) {
                tag(AzaleaBlocks.FLOWERING_AZELEAS_BLOCK).add(tree.sapling);
                AzaleaBlocks.WoodSet wood = tree.woodSet;
                tag(wood.logBlocksTag).add(wood.log,wood.stripped_log,wood.wood,wood.stripped_wood);
                tag(BlockTags.FENCE_GATES).add(wood.fence_gate);
                tag(BlockTags.FLOWER_POTS).add(tree.pottedSapling);
                tag(BlockTags.FLOWERS).add(tree.sapling,tree.floweringLeaves);
                tag(BlockTags.LOGS_THAT_BURN).addTag(wood.logBlocksTag);
                tag(BlockTags.LEAVES).add(tree.azaleaLeaves,tree.floweringLeaves,tree.azaleaLeaves);
                tag(BlockTags.OVERWORLD_NATURAL_LOGS).add(wood.log);
                tag(BlockTags.PLANKS).add(wood.planks);
                tag(BlockTags.SAPLINGS).add(tree.sapling);
                tag(BlockTags.WOODEN_BUTTONS).add(wood.button);
                tag(BlockTags.WOODEN_DOORS).add(wood.door);
                tag(BlockTags.WOODEN_FENCES).add(wood.fence);
                tag(BlockTags.WOODEN_PRESSURE_PLATES).add(wood.pressure_plate);
                tag(BlockTags.WOODEN_SLABS).add(wood.slab);
                tag(BlockTags.WOODEN_STAIRS).add(wood.stairs);
                tag(BlockTags.WOODEN_TRAPDOORS).add(wood.trapdoor);
            }
        }
    }

    public static class BlockStates extends BlockStateProvider {

        public BlockStates(PackOutput output,ExistingFileHelper exFileHelper) {
            super(output, ColorfulAzaleas.MOD_ID, exFileHelper);
        }

        //{
        //  "parent": "colorfulazaleas:block/template_colorful_azalea",
        //  "textures": {
        //    "side": "colorfulazaleas:block/blue_azalea_sapling_side",
        //    "top": "colorfulazaleas:block/blue_azalea_sapling_top",
        //    "plant": "colorfulazaleas:block/blue_azalea_sapling_bush"
        //  },
        //  "render_type": "cutout"
        //}

        //{
        //  "parent": "minecraft:block/template_potted_azalea_bush",
        //  "textures": {
        //    "plant": "minecraft:block/potted_azalea_bush_plant",
        //    "side": "minecraft:block/potted_azalea_bush_side",
        //    "top": "minecraft:block/potted_azalea_bush_top"
        //  }
        //}

        //{
        //  "parent": "minecraft:block/template_potted_azalea_bush",
        //  "textures": {
        //    "side": "colorfulazaleas:block/red_azalea_sapling_side",
        //    "top": "colorfulazaleas:block/red_azalea_sapling_top",
        //    "plant": "colorfulazaleas:block/red_azalea_sapling_bush"
        //  }
        //}

        @Override
        protected void registerStatesAndModels() {
            for (AzaleaBlocks.ColorfulTree tree : AzaleaBlocks.trees) {
                String name = BuiltInRegistries.BLOCK.getKey(tree.sapling).getPath();
                simpleBlockWithItem(tree.sapling,models().withExistingParent(name,modLoc("block/template_colorful_azalea"))
                        .texture("side",modLoc("block/"+tree.sapling.dyeColor.getName()+"_azalea_sapling_side"))
                        .texture("top",modLoc("block/"+tree.sapling.dyeColor.getName()+"_azalea_sapling_top"))
                        .texture("plant",modLoc("block/"+tree.sapling.dyeColor.getName()+"_azalea_sapling_bush"))

                );

                simpleBlock(tree.pottedSapling,models().withExistingParent("potted_"+name,mcLoc("block/template_potted_azalea_bush"))
                                .texture("side",modLoc("block/"+tree.sapling.dyeColor.getName()+"_azalea_sapling_side"))
                                .texture("top",modLoc("block/"+tree.sapling.dyeColor.getName()+"_azalea_sapling_top"))
                                .texture("plant",modLoc("block/"+tree.sapling.dyeColor.getName()+"_azalea_sapling_bush")));

                AzaleaBlocks.WoodSet wood = tree.woodSet;
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
            copy(AzaleaBlocks.FLOWERING_AZELEAS_BLOCK,AzaleaBlocks.FLOWERING_AZELEAS_ITEM);
            for (AzaleaBlocks.ColorfulTree tree : AzaleaBlocks.trees) {
                AzaleaBlocks.WoodSet wood = tree.woodSet;
                copy(wood.logBlocksTag,wood.logItemsTag);
            }
        }
    }

    public static class Lang extends LanguageProvider {

        public Lang(PackOutput output) {
            super(output, ColorfulAzaleas.MOD_ID, "en_us");
        }

        @Override
        protected void addTranslations() {

            for (AzaleaBlocks.ColorfulTree tree : AzaleaBlocks.trees) {
                addDefaultBlock(tree.azaleaLeaves);
                addDefaultBlock(tree.floweringLeaves);
                addDefaultBlock(tree.bloomingLeaves);
                addDefaultBlock(tree.droopingLeaves);
                addDefaultBlock(tree.sapling);
                addDefaultBlock(tree.pottedSapling);


                AzaleaBlocks.WoodSet woodSet = tree.woodSet;
                addDefaultBlock(woodSet.log);
                addDefaultBlock(woodSet.stripped_log);
                addDefaultBlock(woodSet.wood);
                addDefaultBlock(woodSet.stripped_wood);

                addDefaultBlock(woodSet.door);
                addDefaultBlock(woodSet.fence);
                addDefaultBlock(woodSet.fence_gate);
                addDefaultBlock(woodSet.planks);
                addDefaultBlock(woodSet.slab);
                addDefaultBlock(woodSet.stairs);
                addDefaultBlock(woodSet.trapdoor);
            }
        }

        protected void addDefaultItem(Item item) {
            add(item,getNameFromItem(item));
        }

        protected void addDefaultBlock(Block block) {
            add(block,getNameFromBlock(block));
        }

        protected void addDefaultEntityType(EntityType<?> type) {
            add(type,getNameFromEntity(type));
        }

        public static String getNameFromItem(Item item) {
            return StringUtils.capitaliseAllWords(item.getDescriptionId().split("\\.")[2].replace("_", " "));
        }

        public static String getNameFromBlock(Block block) {
            return StringUtils.capitaliseAllWords(block.getDescriptionId().split("\\.")[2].replace("_", " "));
        }

        public static String getNameFromEntity(EntityType<?> entity) {
            return StringUtils.capitaliseAllWords(entity.getDescriptionId().split("\\.")[2].replace("_", " "));
        }

        protected void addTextComponent(MutableComponent component, String text) {
            ComponentContents contents = component.getContents();
            if (contents instanceof TranslatableContents translatableContents) {
                add(translatableContents.getKey(),text);
            } else {
                throw new UnsupportedOperationException(component +" is not translatable");
            }
        }
    }
}
