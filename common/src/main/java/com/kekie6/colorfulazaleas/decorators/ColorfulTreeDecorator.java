package com.kekie6.colorfulazaleas.decorators;

import com.kekie6.colorfulazaleas.registry.AzaleaBlocks;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.stateproviders.*;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.List;

public class ColorfulTreeDecorator extends TreeDecorator {

    public static final MapCodec<ColorfulTreeDecorator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockStateProvider.CODEC.fieldOf("leaf_block").forGetter(ColorfulTreeDecorator::getLeafBlock),
            BlockStateProvider.CODEC.fieldOf("blooming_leaves").forGetter(ColorfulTreeDecorator::getBloomingLeaves)
    ).apply(instance, ColorfulTreeDecorator::new));

    public final BlockStateProvider leafBlock;
    public final BlockStateProvider bloomingLeaves;

    public ColorfulTreeDecorator(BlockStateProvider leafBlock, BlockStateProvider bloomingLeavesBlock) {
        this.leafBlock = leafBlock;
        this.bloomingLeaves = bloomingLeavesBlock;
    }

    public BlockStateProvider getLeafBlock() {
        return leafBlock;
    }

    public BlockStateProvider getBloomingLeaves() {
        return bloomingLeaves;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return AzaleaBlocks.COLORFUL_TREE_DECORATOR;
    }

    @Override
    public void place(Context context) {
        List<BlockPos> leaves = context.leaves();

        List<BlockPos> filteredLeaves = leaves.stream().filter(blockPos -> context.isAir(blockPos.below())).toList();
        for (BlockPos leaf : filteredLeaves) {
            context.setBlock(leaf, this.getBloomingLeaves().getState(context.random(), leaf));
            if (context.random().nextFloat() >= 0.4f) continue;
            int limit = context.random().nextInt(2, 4);
            for (int i = 1; i <= limit; i++) {
                BlockPos decoration = leaf.below(i).immutable();
                if (context.isAir(decoration)) {
                    context.setBlock(decoration, this.getLeafBlock().getState(context.random(), decoration));
                }
            }
        }
    }
}