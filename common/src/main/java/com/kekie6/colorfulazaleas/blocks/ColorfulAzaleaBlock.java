package com.kekie6.colorfulazaleas.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AzaleaBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;

public class ColorfulAzaleaBlock extends AzaleaBlock {

    private final TreeGrower grower;
    private final MapCodec<? extends AzaleaBlock> codec;
    public final DyeColor dyeColor;

    public ColorfulAzaleaBlock(TreeGrower grower, Properties properties, DyeColor dyeColor) {
        super(properties);
        this.grower = grower;
        codec = simpleCodec(properties1 -> new ColorfulAzaleaBlock(grower,properties1,dyeColor));
        this.dyeColor = dyeColor;
    }

    @Override
    public MapCodec<AzaleaBlock> codec() {
        return (MapCodec<AzaleaBlock>) codec;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return level.getFluidState(pos.above()).isEmpty();
    }

    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        grower.growTree(level, level.getChunkSource().getGenerator(), pos, state, random);
    }
}