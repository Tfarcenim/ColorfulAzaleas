package com.kekie6.colorfulazaleas;


import com.kekie6.colorfulazaleas.registry.AzaleaBlocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(ColorfulAzaleas.MOD_ID)
public class ColorfulAzaleasNeoforge {

    public ColorfulAzaleasNeoforge(IEventBus eventBus, Dist dist) {

        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        if (dist.isClient()) {
            ColorfulAzaleasClientNeoforge.init(eventBus);
        }

        // Use NeoForge to bootstrap the Common mod.
        ColorfulAzaleas.init();
        eventBus.addListener(ModDatagen::gather);
        eventBus.addListener(this::register);
    }

    void register(RegisterEvent event) {
        AzaleaBlocks.init();
    }

}