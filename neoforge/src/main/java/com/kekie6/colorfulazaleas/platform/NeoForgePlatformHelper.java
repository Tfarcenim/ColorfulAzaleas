package com.kekie6.colorfulazaleas.platform;

import com.kekie6.colorfulazaleas.BoatTypesForge;
import com.kekie6.colorfulazaleas.platform.services.IPlatformHelper;
import com.kekie6.colorfulazaleas.registry.AzaleaColors;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public Boat.Type createBoatType(AzaleaColors name) {
        return BoatTypesForge.getEnum(name);
    }

    @Override
    public void registerWoodType(WoodType type) {
        WoodType.register(type);
    }
}