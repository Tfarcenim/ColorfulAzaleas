package com.kekie6.colorfulazaleas;

import com.kekie6.colorfulazaleas.registry.AzaleaBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
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

        @Override
        protected void registerStatesAndModels() {
            for (AzaleaBlocks.ColorfulTree tree : AzaleaBlocks.trees) {
                String name = BuiltInRegistries.BLOCK.getKey(tree.sapling).getPath();
                simpleBlockWithItem(tree.sapling,models().withExistingParent(name,modLoc("block/template_colorful_azalea"))
                        .texture("side",modLoc("block/"+tree.sapling.dyeColor.getName()+"_azalea_sapling_side"))
                        .texture("top",modLoc("block/"+tree.sapling.dyeColor.getName()+"_azalea_sapling_top"))
                        .texture("bish",modLoc("block/"+tree.sapling.dyeColor.getName()+"_azalea_sapling_bush"))

                );
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
                addDefaultBlock(tree.droopingLeaves);
                addDefaultBlock(tree.sapling);
                addDefaultBlock(tree.pottedSapling);

                AzaleaBlocks.WoodSet woodSet = tree.woodSet;
                addDefaultBlock(woodSet.log);
                addDefaultBlock(woodSet.stripped_log);
                addDefaultBlock(woodSet.wood);
                addDefaultBlock(woodSet.stripped_wood);



                addDefaultBlock(woodSet.fence);
                addDefaultBlock(woodSet.fence_gate);
                addDefaultBlock(woodSet.planks);
                addDefaultBlock(woodSet.stairs);

            }

           /* "block.colorfulazaleas.orange_blooming_azalea_leaves": "Orange Blooming Azalea Leaves",
                    "block.colorfulazaleas.orange_flowering_azalea_leaves": "Orange Flowering Azalea Leaves",
                    "block.colorfulazaleas.orange_azalea_leaves": "Orange Azalea Leaves",
                    "block.colorfulazaleas.orange_azalea_sapling": "Orange Azalea Sapling",
                    "block.colorfulazaleas.orange_drooping_azalea_leaves": "Orange Drooping Azalea Leaves",
                    "block.colorfulazaleas.red_blooming_azalea_leaves": "Red Blooming Azalea Leaves",
                    "block.colorfulazaleas.red_flowering_azalea_leaves": "Red Flowering Azalea Leaves",
                    "block.colorfulazaleas.red_azalea_leaves": "Red Azalea Leaves",
                    "block.colorfulazaleas.red_azalea_sapling": "Red Azalea Sapling",
                    "block.colorfulazaleas.red_drooping_azalea_leaves": "Red Drooping Azalea Leaves",
                    "block.colorfulazaleas.white_blooming_azalea_leaves": "White Blooming Azalea Leaves",
                    "block.colorfulazaleas.white_flowering_azalea_leaves": "White Flowering Azalea Leaves",
                    "block.colorfulazaleas.white_azalea_leaves": "White Azalea Leaves",
                    "block.colorfulazaleas.white_azalea_sapling": "White Azalea Sapling",
                    "block.colorfulazaleas.white_drooping_azalea_leaves": "White Drooping Azalea Leaves",
                    "block.colorfulazaleas.blue_blooming_azalea_leaves": "Blue Blooming Azalea Leaves",
                    "block.colorfulazaleas.blue_flowering_azalea_leaves": "Blue Flowering Azalea Leaves",
                    "block.colorfulazaleas.blue_azalea_leaves": "Blue Azalea Leaves",
                    "block.colorfulazaleas.blue_azalea_sapling": "Blue Azalea Sapling",
                    "block.colorfulazaleas.blue_drooping_azalea_leaves": "Blue Drooping Azalea Leaves",
                    "block.colorfulazaleas.yellow_blooming_azalea_leaves": "Yellow Blooming Azalea Leaves",
                    "block.colorfulazaleas.yellow_flowering_azalea_leaves": "Yellow Flowering Azalea Leaves",
                    "block.colorfulazaleas.yellow_azalea_leaves": "Yellow Azalea Leaves",
                    "block.colorfulazaleas.yellow_azalea_sapling": "Yellow Azalea Sapling",
                    "block.colorfulazaleas.yellow_drooping_azalea_leaves": "Yellow Drooping Azalea Leaves",
                    "block.colorfulazaleas.drooping_azalea_leaves": "Drooping Azalea Leaves",
                    "block.colorfulazaleas.pink_blooming_azalea_leaves": "Pink Blooming Azalea Leaves",
                    "block.colorfulazaleas.pink_flowering_azalea_leaves": "Pink Flowering Azalea Leaves",
                    "block.colorfulazaleas.pink_azalea_leaves": "Pink Azalea Leaves",
                    "block.colorfulazaleas.pink_azalea_sapling": "Pink Azalea Sapling",
                    "block.colorfulazaleas.pink_drooping_azalea_leaves": "Pink Drooping Azalea Leaves",
                    "block.colorfulazaleas.purple_blooming_azalea_leaves": "Purple Blooming Azalea Leaves",
                    "block.colorfulazaleas.purple_flowering_azalea_leaves": "Purple Flowering Azalea Leaves",
                    "block.colorfulazaleas.purple_azalea_leaves": "Purple Azalea Leaves",
                    "block.colorfulazaleas.purple_azalea_sapling": "Purple Azalea Sapling",
                    "block.colorfulazaleas.purple_drooping_azalea_leaves": "Purple Drooping Azalea Leaves",

                    "itemGroup.colorfulazaleas.colorful_azaleas": "Colorful Azaleas",

                    "block.colorfulazaleas.azule_azalea_planks": "Azule Azalea Planks",
                    "block.colorfulazaleas.azule_azalea_log": "Azule Azalea Log",
                    "block.colorfulazaleas.azule_azalea_wood": "Azule Azalea Wood",
                    "block.colorfulazaleas.stripped_azule_azalea_log": "Stripped Azule Azalea Log",
                    "block.colorfulazaleas.stripped_azule_azalea_wood": "Stripped Azule Azalea Wood",
                    "block.colorfulazaleas.roze_azalea_planks": "Roze Azalea Planks",
                    "block.colorfulazaleas.roze_azalea_log": "Roze Azalea Log",
                    "block.colorfulazaleas.roze_azalea_wood": "Roze Azalea Wood",
                    "block.colorfulazaleas.stripped_roze_azalea_log": "Stripped Roze Azalea Log",
                    "block.colorfulazaleas.stripped_roze_azalea_wood": "Stripped Roze Azalea Wood",
                    "block.colorfulazaleas.tecal_azalea_planks": "Tecal Azalea Planks",
                    "block.colorfulazaleas.tecal_azalea_log": "Tecal Azalea Log",
                    "block.colorfulazaleas.tecal_azalea_wood": "Tecal Azalea Wood",
                    "block.colorfulazaleas.stripped_tecal_azalea_log": "Stripped Tecal Azalea Log",
                    "block.colorfulazaleas.stripped_tecal_azalea_wood": "Stripped Tecal Azalea Wood",
                    "block.colorfulazaleas.bright_azalea_planks": "Bright Azalea Planks",
                    "block.colorfulazaleas.bright_azalea_log": "Bright Azalea Log",
                    "block.colorfulazaleas.bright_azalea_wood": "Bright Azalea Wood",
                    "block.colorfulazaleas.stripped_bright_azalea_log": "Stripped Bright Azalea Log",
                    "block.colorfulazaleas.stripped_bright_azalea_wood": "Stripped Bright Azalea Wood",
                    "block.colorfulazaleas.walnut_azalea_planks": "Walnut Azalea Planks",
                    "block.colorfulazaleas.walnut_azalea_log": "Walnut Azalea Log",
                    "block.colorfulazaleas.walnut_azalea_wood": "Walnut Azalea Wood",
                    "block.colorfulazaleas.stripped_walnut_azalea_log": "Stripped Walnut Azalea Log",
                    "block.colorfulazaleas.stripped_walnut_azalea_wood": "Stripped Walnut Azalea Wood",
                    "block.colorfulazaleas.titanium_azalea_planks": "Titanium Azalea Planks",
                    "block.colorfulazaleas.titanium_azalea_log": "Titanium Azalea Log",
                    "block.colorfulazaleas.titanium_azalea_wood": "Titanium Azalea Wood",
                    "block.colorfulazaleas.stripped_titanium_azalea_log": "Stripped Titanium Azalea Log",
                    "block.colorfulazaleas.stripped_titanium_azalea_wood": "Stripped Titanium Azalea Wood",
                    "block.colorfulazaleas.fiss_azalea_planks": "Fiss Azalea Planks",
                    "block.colorfulazaleas.fiss_azalea_log": "Fiss Azalea Log",
                    "block.colorfulazaleas.fiss_azalea_wood": "Fiss Azalea Wood",
                    "block.colorfulazaleas.stripped_fiss_azalea_log": "Stripped Fiss Azalea Log",
                    "block.colorfulazaleas.stripped_fiss_azalea_wood": "Stripped Fiss Azalea Wood",
                    "block.colorfulazaleas.fiss_azalea_fence": "Fiss Azalea Fence",
                    "block.colorfulazaleas.fiss_azalea_fence_gate": "Fiss Azalea Fence Gate",
                    "block.colorfulazaleas.fiss_azalea_pressure_plate": "Fiss Azalea Pressure Plate",
                    "block.colorfulazaleas.fiss_azalea_button": "Fiss Azalea Button",
                    "block.colorfulazaleas.fiss_azalea_stairs": "Fiss Azalea Stairs",
                    "block.colorfulazaleas.fiss_azalea_slab": "Fiss Azalea Slab",
                    "block.colorfulazaleas.fiss_azalea_door": "Fiss Azalea Door",
                    "block.colorfulazaleas.fiss_azalea_trapdoor": "Fiss Azalea Trapdoor",
                    "block.colorfulazaleas.roze_azalea_fence": "Roze Azalea Fence",
                    "block.colorfulazaleas.roze_azalea_fence_gate": "Roze Azalea Fence Gate",
                    "block.colorfulazaleas.roze_azalea_pressure_plate": "Roze Azalea Pressure Plate",
                    "block.colorfulazaleas.roze_azalea_button": "Roze Azalea Button",
                    "block.colorfulazaleas.roze_azalea_stairs": "Roze Azalea Stairs",
                    "block.colorfulazaleas.roze_azalea_slab": "Roze Azalea Slab",
                    "block.colorfulazaleas.roze_azalea_door": "Roze Azalea Door",
                    "block.colorfulazaleas.roze_azalea_trapdoor": "Roze Azalea Trapdoor",
                    "block.colorfulazaleas.bright_azalea_fence": "Bright Azalea Fence",
                    "block.colorfulazaleas.bright_azalea_fence_gate": "Bright Azalea Fence Gate",
                    "block.colorfulazaleas.bright_azalea_pressure_plate": "Bright Azalea Pressure Plate",
                    "block.colorfulazaleas.bright_azalea_button": "Bright Azalea Button",
                    "block.colorfulazaleas.bright_azalea_stairs": "Bright Azalea Stairs",
                    "block.colorfulazaleas.bright_azalea_slab": "Bright Azalea Slab",
                    "block.colorfulazaleas.bright_azalea_door": "Bright Azalea Door",
                    "block.colorfulazaleas.bright_azalea_trapdoor": "Bright Azalea Trapdoor",
                    "block.colorfulazaleas.titanium_azalea_fence": "Titanium Azalea Fence",
                    "block.colorfulazaleas.titanium_azalea_fence_gate": "Titanium Azalea Fence Gate",
                    "block.colorfulazaleas.titanium_azalea_pressure_plate": "Titanium Azalea Pressure Plate",
                    "block.colorfulazaleas.titanium_azalea_button": "Titanium Azalea Button",
                    "block.colorfulazaleas.titanium_azalea_stairs": "Titanium Azalea Stairs",
                    "block.colorfulazaleas.titanium_azalea_slab": "Titanium Azalea Slab",
                    "block.colorfulazaleas.titanium_azalea_door": "Titanium Azalea Door",
                    "block.colorfulazaleas.titanium_azalea_trapdoor": "Titanium Azalea Trapdoor",
                    "block.colorfulazaleas.tecal_azalea_fence": "Tecal Azalea Fence",
                    "block.colorfulazaleas.tecal_azalea_fence_gate": "Tecal Azalea Fence Gate",
                    "block.colorfulazaleas.tecal_azalea_pressure_plate": "Tecal Azalea Pressure Plate",
                    "block.colorfulazaleas.tecal_azalea_button": "Tecal Azalea Button",
                    "block.colorfulazaleas.tecal_azalea_stairs": "Tecal Azalea Stairs",
                    "block.colorfulazaleas.tecal_azalea_slab": "Tecal Azalea Slab",
                    "block.colorfulazaleas.tecal_azalea_door": "Tecal Azalea Door",
                    "block.colorfulazaleas.tecal_azalea_trapdoor": "Tecal Azalea Trapdoor",
                    "block.colorfulazaleas.walnut_azalea_fence": "Walnut Azalea Fence",
                    "block.colorfulazaleas.walnut_azalea_fence_gate": "Walnut Azalea Fence Gate",
                    "block.colorfulazaleas.walnut_azalea_pressure_plate": "Walnut Azalea Pressure Plate",
                    "block.colorfulazaleas.walnut_azalea_button": "Walnut Azalea Button",
                    "block.colorfulazaleas.walnut_azalea_stairs": "Walnut Azalea Stairs",
                    "block.colorfulazaleas.walnut_azalea_slab": "Walnut Azalea Slab",
                    "block.colorfulazaleas.walnut_azalea_door": "Walnut Azalea Door",
                    "block.colorfulazaleas.walnut_azalea_trapdoor": "Walnut Azalea Trapdoor",
                    "block.colorfulazaleas.azule_azalea_fence": "Azule Azalea Fence",
                    "block.colorfulazaleas.azule_azalea_fence_gate": "Azule Azalea Fence Gate",
                    "block.colorfulazaleas.azule_azalea_pressure_plate": "Azule Azalea Pressure Plate",
                    "block.colorfulazaleas.azule_azalea_button": "Azule Azalea Button",
                    "block.colorfulazaleas.azule_azalea_stairs": "Azule Azalea Stairs",
                    "block.colorfulazaleas.azule_azalea_slab": "Azule Azalea Slab",
                    "block.colorfulazaleas.azule_azalea_door": "Azule Azalea Door",
                    "block.colorfulazaleas.azule_azalea_trapdoor": "Azule Azalea Trapdoor",

                    "block.colorfulazaleas.potted_orange_azalea_sapling": "Potted Orange Azalea Sapling",
                    "block.colorfulazaleas.potted_yellow_azalea_sapling": "Potted Yellow Azalea Sapling",
                    "block.colorfulazaleas.potted_red_azalea_sapling": "Potted Red Azalea Sapling",
                    "block.colorfulazaleas.potted_blue_azalea_sapling": "Potted Blue Azalea Sapling",
                    "block.colorfulazaleas.potted_pink_azalea_sapling": "Potted Pink Azalea Sapling",
                    "block.colorfulazaleas.potted_purple_azalea_sapling": "Potted Purple Azalea Sapling",
                    "block.colorfulazaleas.potted_white_azalea_sapling": "Potted White Azalea Sapling",

                    "tag.colorfulazaleas.azaleas": "Azaleas",
                    "tag.colorfulazaleas.azule_azalea_logs": "Azule Azalea Logs",
                    "tag.colorfulazaleas.bright_azalea_logs": "Bright Azalea Logs",
                    "tag.colorfulazaleas.fiss_azalea_logs": "Fiss Azalea Logs",
                    "tag.colorfulazaleas.roze_azalea_logs": "Roze Azalea Logs",
                    "tag.colorfulazaleas.tecal_azalea_logs": "Tecal Azalea Logs",
                    "tag.colorfulazaleas.titanium_azalea_logs": "Titanium Azalea Logs",
                    "tag.colorfulazaleas.walnut_azalea_logs": "Walnut Azalea Logs"*/
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
