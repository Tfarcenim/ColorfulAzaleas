package com.kekie6.colorfulazaleas.registry;

import com.kekie6.colorfulazaleas.ColorfulAzaleas;
import com.kekie6.colorfulazaleas.blocks.ColorfulAzaleaBlock;
import com.kekie6.colorfulazaleas.blocks.DroopingLeavesBlock;
import com.kekie6.colorfulazaleas.decorators.ColorfulTreeDecorator;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.Optional;

public class AzaleaBlocks {

    public static final TreeDecoratorType<ColorfulTreeDecorator> COLORFUL_TREE_DECORATOR = Registry.register(BuiltInRegistries.TREE_DECORATOR_TYPE, ColorfulAzaleas.id("colorful_tree_decorator"), new TreeDecoratorType<>(ColorfulTreeDecorator.CODEC));
    public static final ColorfulTree[] trees = new ColorfulTree[AzaleaColors.values().length];
    public static final Block DROOPING_AZALEA_LEAVES = registerBlockWithItem("drooping_azalea_leaves", new DroopingLeavesBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.AZALEA_LEAVES).noCollission().sound(SoundType.CAVE_VINES)));

    //public static final TagKey<Block> AZELEA


    static {
        for(AzaleaColors color : AzaleaColors.values()) {
            trees[color.ordinal()] = new ColorfulTree(color);
        }
    }
    public static void init() {}

    public enum AzaleaColors {
        orange("tecal"),
        yellow("fiss"),
        red("roze"),
        blue("azule"),
        pink("bright"),
        purple("walnut"),
        white("titanium");

        final String title;

        AzaleaColors(String title) {
            this.title = title;
        }
    }

    public static class ColorfulTree {
        public final String name;
        public final WoodSet woodSet;
        public final Block sapling;
        public final Block pottedSapling;
        public final LeavesBlock azaleaLeaves;
        public final LeavesBlock floweringLeaves;
        public final LeavesBlock bloomingLeaves;
        public final Block droopingLeaves;

        public ColorfulTree(AzaleaColors color) {
            this.name = color.name();
            this.woodSet = new WoodSet(color);
            this.azaleaLeaves = registerBlockWithItem(name + "_azalea_leaves", new LeavesBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.AZALEA_LEAVES)));
            this.floweringLeaves = registerBlockWithItem(name + "_flowering_azalea_leaves", new LeavesBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.AZALEA_LEAVES)));
            this.bloomingLeaves = registerBlockWithItem(name + "_blooming_azalea_leaves", new LeavesBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.AZALEA_LEAVES).requiresCorrectToolForDrops()));
            this.droopingLeaves = registerBlockWithItem(name + "_drooping_azalea_leaves", new DroopingLeavesBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.AZALEA_LEAVES).noCollission().sound(SoundType.CAVE_VINES)));

            ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureKey = ResourceKey.create(Registries.CONFIGURED_FEATURE, ColorfulAzaleas.id(name));
            this.sapling = registerBlockWithItem(name + "_flowering_azalea", new ColorfulAzaleaBlock(new TreeGrower(name,
                    Optional.empty(),
                    Optional.of(configuredFeatureKey),
                    Optional.empty()), BlockBehaviour.Properties.ofLegacyCopy(Blocks.AZALEA).noOcclusion()));
            this.pottedSapling = registerBlock("potted_" + name + "_flowering_azalea_bush", new FlowerPotBlock(this.sapling, BlockBehaviour.Properties.ofLegacyCopy(Blocks.POTTED_AZALEA)));
            
           // addBlockToAzaleaLootTable(sapling);
            //CompostingChanceRegistry.INSTANCE.add(sapling, 0.65F);
        }

    }

    public static class WoodSet {
        public final String name;

        public static final BlockSetType BLOCK_SET_TYPE = new BlockSetType(ColorfulAzaleas.id("colorful_azaleas").toString(),
                true,
                true,
                true,
                BlockSetType.PressurePlateSensitivity.EVERYTHING,
                SoundType.CHERRY_WOOD,
                SoundEvents.CHERRY_WOOD_DOOR_CLOSE,
                SoundEvents.CHERRY_WOOD_DOOR_OPEN,
                SoundEvents.CHERRY_WOOD_TRAPDOOR_CLOSE,
                SoundEvents.CHERRY_WOOD_TRAPDOOR_OPEN,
                SoundEvents.CHERRY_WOOD_PRESSURE_PLATE_CLICK_OFF,
                SoundEvents.CHERRY_WOOD_PRESSURE_PLATE_CLICK_ON,
                SoundEvents.CHERRY_WOOD_BUTTON_CLICK_OFF,
                SoundEvents.CHERRY_WOOD_BUTTON_CLICK_ON);
        public static final WoodType WOOD_TYPE = new WoodType(BLOCK_SET_TYPE.name(), BLOCK_SET_TYPE);

        public final Block log;
        public final Block wood;
        public final Block stripped_log;
        public final Block stripped_wood;
        public final Block planks;
        public final Block stairs;
        public final Block slab;
        public final Block fence;
        public final Block fence_gate;
        public final Block door;
        public final Block trapdoor;
        public final Block pressure_plate;
        public final Block button;

        public final TagKey<Block> logBlocksTag;
        public final TagKey<Item> logItemsTag;

        public WoodSet(AzaleaColors color) {
            this.name = color.title;

            this.log = registerBlockWithItem(name + "_azalea_log", new RotatedPillarBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_LOG)));
            this.wood = registerBlockWithItem(name + "_azalea_wood", new RotatedPillarBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_WOOD)));
            this.stripped_log = registerBlockWithItem("stripped_" + name + "_azalea_log", new RotatedPillarBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.STRIPPED_OAK_LOG)));
            this.stripped_wood = registerBlockWithItem("stripped_" + name + "_azalea_wood", new RotatedPillarBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.STRIPPED_OAK_WOOD)));
            this.planks = registerBlockWithItem(name + "_azalea_planks", new Block(BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_PLANKS)));
            this.stairs = registerBlockWithItem(name + "_azalea_stairs", new StairBlock(planks.defaultBlockState(), BlockBehaviour.Properties.ofLegacyCopy(planks)));
            this.slab = registerBlockWithItem(name + "_azalea_slab", new SlabBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_SLAB)));
            this.fence = registerBlockWithItem(name + "_azalea_fence", new FenceBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_FENCE)));
            this.fence_gate = registerBlockWithItem(name + "_azalea_fence_gate", new FenceGateBlock(WOOD_TYPE, BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_FENCE_GATE)));
            this.door = registerBlockWithItem(name + "_azalea_door", new DoorBlock(BLOCK_SET_TYPE, BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_DOOR)));
            this.trapdoor = registerBlockWithItem(name + "_azalea_trapdoor", new TrapDoorBlock(BLOCK_SET_TYPE, BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_TRAPDOOR)));
            this.pressure_plate = registerBlockWithItem(name + "_azalea_pressure_plate", new PressurePlateBlock(BLOCK_SET_TYPE, BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_PRESSURE_PLATE)));
            this.button = registerBlockWithItem(name + "_azalea_button", new ButtonBlock(BLOCK_SET_TYPE, 30, BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_BUTTON)));

            logBlocksTag = TagKey.create(Registries.BLOCK,ColorfulAzaleas.id(name + "_azalea_logs"));
            logItemsTag = TagKey.create(Registries.ITEM,ColorfulAzaleas.id(name + "_azalea_logs"));

            ColorfulAzaleas.addStrippable(log, stripped_log);
            ColorfulAzaleas.addStrippable(wood, stripped_wood);
        }
    }

    public static <B extends Block> B registerBlockWithItem(String name, B block) {
        ResourceLocation resourceLocation = ColorfulAzaleas.id(name);
        Registry.register(BuiltInRegistries.BLOCK, resourceLocation, block);
        Registry.register(BuiltInRegistries.ITEM, resourceLocation, new BlockItem(block, new Item.Properties()));
        return block;
    }

    public static Block registerBlock(String name, Block block) {
        ResourceLocation resourceLocation = ColorfulAzaleas.id(name);
        Registry.register(BuiltInRegistries.BLOCK, resourceLocation, block);
        return block;
    }
}