package com.kekie6.colorfulazaleas;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class ColorfulAzaleasClientNeoforge{

    static void init(IEventBus bus) {
        bus.addListener(ColorfulAzaleasClientNeoforge::setup);
    }

    static void setup(FMLClientSetupEvent event) {
        ColorfulAzaleasClient.renderTypes(ItemBlockRenderTypes::setRenderLayer);
    }
}