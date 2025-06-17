package com.kekie6.colorfulazaleas.registry;

import com.kekie6.colorfulazaleas.ColorfulAzaleas;
import com.kekie6.colorfulazaleas.blocks.ColorfulAzaleaBlock;
import com.kekie6.colorfulazaleas.blocks.DroopingLeavesBlock;
import com.kekie6.colorfulazaleas.decorators.ColorfulTreeDecorator;
import com.kekie6.colorfulazaleas.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
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

    public static final TagKey<Block> FLOWERING_AZELEAS_BLOCK = TagKey.create(Registries.BLOCK,ResourceLocation.fromNamespaceAndPath("c","flowering_azaleas"));
    public static final TagKey<Item> FLOWERING_AZELEAS_ITEM = TagKey.create(Registries.ITEM,ResourceLocation.fromNamespaceAndPath("c","flowering_azaleas"));


    static {
        for(AzaleaColors color : AzaleaColors.values()) {
            trees[color.ordinal()] = new ColorfulTree(color);
        }
    }
    public static void init() {}

    public static class ColorfulTree {
        public final String name;
        public final WoodSet woodSet;
        public final ColorfulAzaleaBlock sapling;
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
                    Optional.empty()), BlockBehaviour.Properties.ofLegacyCopy(Blocks.AZALEA).noOcclusion(),color.color));
            this.pottedSapling = registerBlock("potted_" + name + "_flowering_azalea_bush", new FlowerPotBlock(this.sapling, BlockBehaviour.Properties.ofLegacyCopy(Blocks.POTTED_AZALEA)));
            
           // addBlockToAzaleaLootTable(sapling);
            //CompostingChanceRegistry.INSTANCE.add(sapling, 0.65F);
        }

    }

    public static class WoodSet {
        public final String name;

        public final BlockSetType blockSetType;
        public final WoodType woodType;
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
        public final StandingSignBlock sign;
        public final WallSignBlock wall_sign;
        public final Block hanging_sign;
        public final Block wall_hanging_sign;

        public final SignItem sign_item;
        public final HangingSignItem hanging_sign_item;

        public final Item boat;
        public final Item chest_boat;

        public final TagKey<Block> logBlocksTag;
        public final TagKey<Item> logItemsTag;

        public WoodSet(AzaleaColors color) {
            this.name = color.title;
            blockSetType = new BlockSetType(ColorfulAzaleas.id(name+"_azelea").toString());
            woodType = new WoodType(blockSetType.name(),blockSetType);
            Services.PLATFORM.registerWoodType(woodType);
            this.log = registerBlockWithItem(name + "_azalea_log", new RotatedPillarBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_LOG)));
            this.wood = registerBlockWithItem(name + "_azalea_wood", new RotatedPillarBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_WOOD)));
            this.stripped_log = registerBlockWithItem("stripped_" + name + "_azalea_log", new RotatedPillarBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.STRIPPED_OAK_LOG)));
            this.stripped_wood = registerBlockWithItem("stripped_" + name + "_azalea_wood", new RotatedPillarBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.STRIPPED_OAK_WOOD)));
            this.planks = registerBlockWithItem(name + "_azalea_planks", new Block(BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_PLANKS)));
            this.stairs = registerBlockWithItem(name + "_azalea_stairs", new StairBlock(planks.defaultBlockState(), BlockBehaviour.Properties.ofLegacyCopy(planks)));
            this.slab = registerBlockWithItem(name + "_azalea_slab", new SlabBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_SLAB)));
            this.fence = registerBlockWithItem(name + "_azalea_fence", new FenceBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_FENCE)));
            this.fence_gate = registerBlockWithItem(name + "_azalea_fence_gate", new FenceGateBlock(woodType, BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_FENCE_GATE)));
            this.door = registerBlockWithItem(name + "_azalea_door", new DoorBlock(blockSetType, BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_DOOR)));
            this.trapdoor = registerBlockWithItem(name + "_azalea_trapdoor", new TrapDoorBlock(blockSetType, BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_TRAPDOOR)));
            this.pressure_plate = registerBlockWithItem(name + "_azalea_pressure_plate", new PressurePlateBlock(blockSetType, BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_PRESSURE_PLATE)));
            this.button = registerBlockWithItem(name + "_azalea_button", new ButtonBlock(blockSetType, 30, BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_BUTTON)));
            this.sign = registerBlock(name+"_sign",new StandingSignBlock(woodType,BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_SIGN)));
            this.wall_sign = registerBlock(name+"_wall_sign",new WallSignBlock(woodType,BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_WALL_SIGN).dropsLike(sign)));

            this.hanging_sign = registerBlock(name+"_hanging_sign",new CeilingHangingSignBlock(woodType,BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_HANGING_SIGN)));
            this.wall_hanging_sign = registerBlock(name+"_wall_hanging_sign",new CeilingHangingSignBlock(woodType,BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_WALL_HANGING_SIGN).dropsLike(hanging_sign)));

            this.sign_item = registerItem(name+"_sign",new SignItem(new Item.Properties(),sign,wall_sign));
            this.hanging_sign_item = registerItem(name+"_hanging_sign",new HangingSignItem(hanging_sign,wall_hanging_sign,new Item.Properties()));

            this.boat = registerItem(name+"_boat",new BoatItem(false, Services.PLATFORM.createBoatType(color),new Item.Properties()));
            this.chest_boat = registerItem(name+"_chest_boat",new BoatItem(true, Services.PLATFORM.createBoatType(color),new Item.Properties()));

            logBlocksTag = TagKey.create(Registries.BLOCK,ColorfulAzaleas.id(name + "_azalea_logs"));
            logItemsTag = TagKey.create(Registries.ITEM,ColorfulAzaleas.id(name + "_azalea_logs"));

            ColorfulAzaleas.addStrippable(log, stripped_log);
            ColorfulAzaleas.addStrippable(wood, stripped_wood);
        }
    }

    public static <I extends Item> I registerItem(String name, I item) {
        ResourceLocation resourceLocation = ColorfulAzaleas.id(name);
        Registry.register(BuiltInRegistries.ITEM, resourceLocation, item);
        return item;
    }


    public static <B extends Block> B registerBlockWithItem(String name, B block) {
        ResourceLocation resourceLocation = ColorfulAzaleas.id(name);
        Registry.register(BuiltInRegistries.BLOCK, resourceLocation, block);
        Registry.register(BuiltInRegistries.ITEM, resourceLocation, new BlockItem(block, new Item.Properties()));
        return block;
    }

    public static <B extends Block> B registerBlock(String name, B block) {
        ResourceLocation resourceLocation = ColorfulAzaleas.id(name);
        Registry.register(BuiltInRegistries.BLOCK, resourceLocation, block);
        return block;
    }
}