package com.kekie6.colorfulazaleas;

import com.kekie6.colorfulazaleas.registry.AzaleaBlocks;
import com.kekie6.colorfulazaleas.registry.AzaleaColors;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Items;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.util.List;
import java.util.function.Supplier;

public class BoatTypesForge {

    //'(Ljava/lang/String;ILjava/util/function/Supplier;Ljava/lang/String;Ljava/util/function/Supplier;Ljava/util/function/Supplier;Ljava/util/function/Supplier;Z)V'

    public static EnumProxy<Boat.Type> create(Supplier<AzaleaColors> color) {
        return new EnumProxy<>(Boat.Type.class, List.of((Supplier<Object>) () -> {
            int o = color.get().ordinal();
            AzaleaBlocks.ColorfulTree tree = AzaleaBlocks.trees[o];
                    return tree.woodSet.planks;
                },ColorfulAzaleas.MOD_ID+":"+color.get().title+"_azalea",
                (Supplier<Object>)   () -> {
                    int o = color.get().ordinal();
                    AzaleaBlocks.ColorfulTree tree = AzaleaBlocks.trees[o];
                    return tree.woodSet.boat;
                },(Supplier<Object>) () -> {
                    int o = color.get().ordinal();
                    AzaleaBlocks.ColorfulTree tree = AzaleaBlocks.trees[o];
                    return tree.woodSet.chest_boat;
                },(Supplier<Object>) () -> Items.STICK,false));

    }

    //        private Type(Supplier planks, String name, Supplier boatItem, Supplier chestBoatItem, Supplier stickItem, boolean raft) {
    public static final EnumProxy<Boat.Type> TECAL_AZALEA = create(() -> AzaleaColors.orange);
    public static final EnumProxy<Boat.Type> FISS_AZALEA = create(() -> AzaleaColors.yellow);
    public static final EnumProxy<Boat.Type> ROZE_AZALEA = create(() -> AzaleaColors.red);
    public static final EnumProxy<Boat.Type> AZULE_AZALEA = create(() -> AzaleaColors.blue);
    public static final EnumProxy<Boat.Type> BRIGHT_AZALEA = create(() -> AzaleaColors.pink);
    public static final EnumProxy<Boat.Type> WALNUT_AZALEA = create(() -> AzaleaColors.purple);
    public static final EnumProxy<Boat.Type> TITANIUM_AZALEA = create(() -> AzaleaColors.white);

    public static void init() {

    }

    public static Boat.Type getEnum(AzaleaColors color) {
        return switch (color) {
            case orange -> TECAL_AZALEA.getValue();
            case yellow -> FISS_AZALEA.getValue();
            case red -> ROZE_AZALEA.getValue();
            case blue -> AZULE_AZALEA.getValue();
            case pink -> BRIGHT_AZALEA.getValue();
            case purple -> WALNUT_AZALEA.getValue();
            case white -> TITANIUM_AZALEA.getValue();
        };
    }
}
