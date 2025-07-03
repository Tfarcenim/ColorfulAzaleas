package com.kekie6.colorfulazaleas.platform;

import com.kekie6.colorfulazaleas.platform.services.IPlatformHelper;
import com.kekie6.colorfulazaleas.registry.AzaleaColors;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.state.properties.WoodType;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public Boat.Type createBoatType(AzaleaColors name) {
        return null;
    }

    @Override
    public void registerWoodType(WoodType type) {

    }
}
