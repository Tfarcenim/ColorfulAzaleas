package com.kekie6.colorfulazaleas;

import com.google.common.collect.ImmutableMap;
import com.kekie6.colorfulazaleas.decorators.ColorfulTreeDecorator;
import com.kekie6.colorfulazaleas.platform.Services;
import com.kekie6.colorfulazaleas.registry.AzaleaBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.HashMap;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class ColorfulAzaleas {

    public static final String MOD_ID = "colorfulazaleas";
    public static final TreeDecoratorType<ColorfulTreeDecorator> COLORFUL_TREE_DECORATOR = Registry.register(BuiltInRegistries.TREE_DECORATOR_TYPE, id("colorful_tree_decorator"), new TreeDecoratorType<>(ColorfulTreeDecorator.CODEC));

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {


        // It is common for all supported loaders to provide a similar feature that can not be used directly in the
        // common code. A popular way to get around this is using Java's built-in service loader feature to create
        // your own abstraction layer. You can learn more about this in our provided services class. In this example
        // we have an interface in the common code and use a loader specific implementation to delegate our call to
        // the platform specific approach.
    }

    public static void setup() {
    }

    public static void addStrippable(Block block,Block stripped) {
        if (AxeItem.STRIPPABLES instanceof ImmutableMap<Block, Block>) {
            AxeItem.STRIPPABLES = new HashMap<>(AxeItem.STRIPPABLES);
        }
        AxeItem.STRIPPABLES.put(block,stripped);
    }

    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }
}