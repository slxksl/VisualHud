package net.brsk.hudmenu.client;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Draws the smooth vector icons stored in {@link IconData}. */
final class Icons {
    private static final List<int[]>[] RUNS = build();

    private Icons() {}

    @SuppressWarnings("unchecked")
    private static List<int[]>[] build() {
        List<int[]>[] all = new List[IconData.ROWS.length];
        for (int i = 0; i < all.length; i++) {
            List<int[]> runs = new ArrayList<>();
            String[] rows = IconData.ROWS[i];
            for (int y = 0; y < rows.length; y++) {
                String row = rows[y];
                int x = 0;
                while (x < row.length()) {
                    int level = Character.digit(row.charAt(x), 16);
                    if (level <= 0) {
                        x++;
                        continue;
                    }
                    int start = x;
                    while (x < row.length() && Character.digit(row.charAt(x), 16) == level) {
                        x++;
                    }
                    runs.add(new int[] {start, y, x - start, level});
                }
            }
            all[i] = runs;
        }
        return all;
    }

    static int indexOf(String name) {
        for (int i = 0; i < IconData.NAMES.length; i++) {
            if (IconData.NAMES[i].equals(name)) {
                return i;
            }
        }
        return -1;
    }

    static void draw(GuiGraphicsExtractor g, int index, float x, float y, float size, int color) {
        if (index < 0 || index >= RUNS.length) {
            return;
        }
        float baseAlpha = ((color >>> 24) & 0xFF) / 255.0f;
        int rgb = color & 0x00FFFFFF;
        g.pose().pushMatrix();
        g.pose().translate(x, y);
        float s = size / IconData.SIZE;
        g.pose().scale(s, s);
        for (int[] run : RUNS[index]) {
            int alpha = Math.round(run[3] * 17 * baseAlpha);
            if (alpha <= 0) {
                continue;
            }
            g.fill(run[0], run[1], run[0] + run[2], run[1] + 1, (alpha << 24) | rgb);
        }
        g.pose().popMatrix();
    }
}
