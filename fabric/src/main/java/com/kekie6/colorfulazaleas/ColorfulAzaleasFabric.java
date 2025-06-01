package com.kekie6.colorfulazaleas;

import com.kekie6.colorfulazaleas.registry.*;
import net.fabricmc.api.*;

public class ColorfulAzaleasFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        AzaleaBlocks.init();
        ColorfulAzaleasItemGroups.register();
    }
}