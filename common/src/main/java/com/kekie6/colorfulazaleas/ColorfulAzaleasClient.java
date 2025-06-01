package com.kekie6.colorfulazaleas;

import com.kekie6.colorfulazaleas.registry.AzaleaBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ColorfulAzaleasClient {

    public static void renderTypes(BiConsumer<Block, RenderType> consumer) {
        for (int i = 0; i < AzaleaBlocks.AzaleaColors.values().length; i++) {
            AzaleaBlocks.ColorfulTree tree = AzaleaBlocks.trees[i];

            consumer.accept(tree.sapling,RenderType.cutout());
            consumer.accept(tree.pottedSapling,RenderType.cutout());
            consumer.accept(tree.droopingLeaves,RenderType.cutout());

            AzaleaBlocks.WoodSet wood = tree.woodSet;

            consumer.accept(wood.door,RenderType.cutout());
            consumer.accept(wood.trapdoor,RenderType.cutout());
        }
        consumer.accept(AzaleaBlocks.DROOPING_AZALEA_LEAVES, RenderType.cutout());
    }

}
