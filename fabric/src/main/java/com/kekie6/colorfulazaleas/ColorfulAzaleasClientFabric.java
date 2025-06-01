package com.kekie6.colorfulazaleas;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;

public class ColorfulAzaleasClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ColorfulAzaleasClient.renderTypes(BlockRenderLayerMap.INSTANCE::putBlock);

    }
}