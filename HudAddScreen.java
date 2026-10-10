package net.brsk.hudmenu.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

/** Visual menu inspired by the supplied reference image. */
public final class HudAddScreen extends Screen {
    private static final String[] OPTIONS = {
        "Watermark", "Player", "Coordinates", "Island", "Hotbar", "Scoreboard",
        "Server", "Target Hud", "Effects", "Music", "Notifications", "Armor",
        "Keybinds", "Cooldowns", "Staff List", "Inventory", "Arraylist", "Locked Target"
    };

    private static final String[][] ICONS = {
        {"...#...", "..#.#..", ".#...#.", "#.....#", ".#...#.", "..#.#..", "...#..."},
        {"...##..", "..#..#.", "..#..#.", "...##..", "..####.", ".#....#", "#......"},
        {"...#...", "..#.#..", ".#...#.", ".#...#.", "..#.#..", "...#...", "...#..."},
        {".......", ".......", "#.....#", ".#...#.", "..#.#..", "...#...", "......."},
        {".......", "#######", "#.#.#.#", "#.#.#.#", "#######", ".......", "......."},
        {"#......", "#..####", "#......", "#..####", "#......", "#..####", "......."},
        {"...###.", "..#...#", ".#.....", ".#..#..", ".#.....", "..#...#", "...###."},
        {"...#...", "..###..", ".#.#.#.", "###.###", ".#.#.#.", "..###..", "...#..."},
        {"...##..", "..#..#.", "..#..#.", "..####.", ".#....#", ".#....#", "..####."},
        {"....#..", "...##..", "..#.#..", "....#..", "....#..", "....##.", ".....#."},
        {"..###..", ".#...#.", ".#...#.", "..###..", "...#...", "..###..", "......."},
        {"..###..", ".#...#.", ".#...#.", ".#...#.", "..#.#..", "...#...", "......."},
        {".......", ".#.#.#.", "..###..", "...#...", "..###..", ".#...#.", "......."},
        {"..###..", ".#...#.", "#.....#", "#..#..#", "#.....#", ".#...#.", "..###.."},
        {"...#...", "..###..", "..###..", "...#...", "..###..", ".#...#.", "......."},
        {"...#...", "..###..", ".#####.", "#..#..#", ".#####.", "..###..", "...#..."},
        {"#......", "##.....", "#.#....", "#..#...", "#...#..", "#....##", "......."},
        {"..#####", "..#...#", "..#...#", "..#####", "..#.....", "..#.....", "..#####"}
    };

    private static final int PANEL_WIDTH = 102;
    private static final int HEADER_HEIGHT = 25;
    private static final int PANEL_X = 7;
    private static final int PANEL_Y = 7;
    private static final int PANEL_BOTTOM_PADDING = 7;
    private static final int RED = 0xFFFF3028;
    private final Screen parent;
    private int selectedIndex = -1;

    public HudAddScreen(Screen parent) {
        super(Component.empty());
        this.parent = parent;
    }

    private int rowHeight() {
        int available = Math.max(18, this.height - PANEL_Y * 2 - HEADER_HEIGHT - PANEL_BOTTOM_PADDING);
        return Math.max(12, Math.min(19, available / OPTIONS.length));
    }

    private int panelHeight() {
        return HEADER_HEIGHT + rowHeight() * OPTIONS.length + PANEL_BOTTOM_PADDING;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        // Keep the chat screen visible behind this overlay.
        if (parent != null) {
            parent.extractRenderState(graphics, mouseX, mouseY, delta);
        } else {
            super.extractRenderState(graphics, mouseX, mouseY, delta);
        }

        graphics.fill(0, 0, this.width, this.height, 0x18000000);

        int x = PANEL_X;
        int y = PANEL_Y;
        int h = panelHeight();
        drawRoundedRect(graphics, x + 2, y + 3, PANEL_WIDTH, h, 8, 0x65000000);
        drawRoundedRect(graphics, x, y, PANEL_WIDTH, h, 8, 0xEB0A070A);
        graphics.text(this.font, "What to add?", x + 10, y + 8, 0xFFF1EDEF, false);

        int rowH = rowHeight();
        for (int i = 0; i < OPTIONS.length; i++) {
            int rowY = y + HEADER_HEIGHT + i * rowH;
            boolean hovered = mouseX >= x && mouseX < x + PANEL_WIDTH && mouseY >= rowY && mouseY < rowY + rowH;
            if (i == selectedIndex) {
                graphics.fill(x + 4, rowY, x + PANEL_WIDTH - 4, rowY + rowH, 0x35F02C26);
                graphics.fill(x + 3, rowY + 3, x + 4, rowY + rowH - 3, RED);
            } else if (hovered) {
                graphics.fill(x + 4, rowY, x + PANEL_WIDTH - 4, rowY + rowH, 0x20FFFFFF);
            }

            int textY = rowY + Math.max(2, (rowH - 8) / 2);
            graphics.text(this.font, OPTIONS[i], x + 10, textY,
                    i == selectedIndex ? 0xFFFFFFFF : 0xFFD0CACE, false);
            drawIcon(graphics, ICONS[i], x + PANEL_WIDTH - 16, rowY + Math.max(2, (rowH - 7) / 2));
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double mouseX = event.x();
        double mouseY = event.y();
        int button = event.button();
        if (button == 1) {
            onClose();
            return true;
        }
        if (button == 0) {
            int rowH = rowHeight();
            int rowsStart = PANEL_Y + HEADER_HEIGHT;
            if (mouseX >= PANEL_X && mouseX < PANEL_X + PANEL_WIDTH && mouseY >= rowsStart) {
                int index = (int) ((mouseY - rowsStart) / rowH);
                if (index >= 0 && index < OPTIONS.length && mouseY < rowsStart + OPTIONS.length * rowH) {
                    selectedIndex = index;
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    private static void drawIcon(GuiGraphicsExtractor graphics, String[] pattern, int x, int y) {
        for (int row = 0; row < pattern.length; row++) {
            String line = pattern[row];
            for (int col = 0; col < line.length(); col++) {
                if (line.charAt(col) == '#') {
                    graphics.fill(x + col, y + row, x + col + 1, y + row + 1, RED);
                }
            }
        }
    }

    private static void drawRoundedRect(GuiGraphicsExtractor graphics, int x, int y, int width, int height,
                                        int radius, int color) {
        int r = Math.min(radius, Math.min(width / 2, height / 2));
        for (int row = 0; row < r; row++) {
            int dy = r - row;
            int inset = r - (int) Math.sqrt(Math.max(0, r * r - dy * dy));
            graphics.fill(x + inset, y + row, x + width - inset, y + row + 1, color);
            graphics.fill(x + inset, y + height - row - 1, x + width - inset, y + height - row, color);
        }
        graphics.fill(x, y + r, x + width, y + height - r, color);
    }
}
