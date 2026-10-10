package net.brsk.hudmenu.client;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;

/** The custom font (Inter) used by the menu. */
final class UiFont {
    private static final Style STYLE = Style.EMPTY.withFont(
            new FontDescription.Resource(Identifier.fromNamespaceAndPath("hudmenu", "ui")));

    private UiFont() {}

    static Component of(String text) {
        return Component.literal(text).withStyle(STYLE);
    }
}
