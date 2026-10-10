package net.brsk.hudmenu.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Smooth drawing helpers. Everything is drawn at 4x resolution to hide the pixel steps. */
final class Shapes {
    private static final int SS = 4;

    private Shapes() {}

    static void roundRect(GuiGraphicsExtractor g, float x, float y, float w, float h, float radius, int color) {
        if ((color >>> 24) == 0) {
            return;
        }
        g.pose().pushMatrix();
        g.pose().translate(x, y);
        g.pose().scale(1.0f / SS, 1.0f / SS);
        int width = Math.round(w * SS);
        int height = Math.round(h * SS);
        int r = Math.min(Math.round(radius * SS), Math.min(width, height) / 2);
        for (int row = 0; row < r; row++) {
            double dy = r - row - 0.5;
            int inset = r - (int) Math.floor(Math.sqrt(Math.max(0.0, (double) r * r - dy * dy)));
            g.fill(inset, row, width - inset, row + 1, color);
            g.fill(inset, height - row - 1, width - inset, height - row, color);
        }
        g.fill(0, r, width, height - r, color);
        g.pose().popMatrix();
    }
}
