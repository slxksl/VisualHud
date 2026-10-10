package net.brsk.hudmenu.client;

/** Colors used by the menu. All values are ARGB and can be changed at runtime. */
public final class Theme {
    public static int panelColor = 0xB0A9ABAE;   // translucent gray
    public static int textColor = 0xFF1B1C1E;    // dark text
    public static int iconColor = 0xFFFFFFFF;    // white icons
    public static int hoverColor = 0x30FFFFFF;

    private Theme() {}

    /** Multiplies the alpha of an ARGB color by a factor from 0 to 1. */
    public static int withAlpha(int argb, float factor) {
        int a = Math.round(((argb >>> 24) & 0xFF) * Math.max(0f, Math.min(1f, factor)));
        return (a << 24) | (argb & 0x00FFFFFF);
    }
}
