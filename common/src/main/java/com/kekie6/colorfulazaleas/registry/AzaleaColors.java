package com.kekie6.colorfulazaleas.registry;

import net.minecraft.world.item.DyeColor;

import java.util.Locale;

public enum AzaleaColors {
    orange("tecal"),
    yellow("fiss"),
    red("roze"),
    blue("azule"),
    pink("bright"),
    purple("walnut"),
    //  black("black"),
    //  brown("brown"),
    // cyan("cyan"),
    white("titanium");

    public final String title;
    final DyeColor color;

    AzaleaColors(String title) {
        this.title = title;
        color = DyeColor.valueOf(name().toUpperCase(Locale.ROOT));
    }
}
