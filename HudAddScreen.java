package net.brsk.hudmenu.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

/** Floating, rounded, translucent "What to add?" panel. */
public final class HudAddScreen extends Screen {
    private static final String[] OPTIONS = {
        "Watermark", "Player", "Coordinates", "Island", "Hotbar", "Scoreboard",
        "Server", "Target Hud", "Effects", "Music", "Notifications", "Armor",
        "Keybinds", "Cooldowns", "Staff List", "Inventory", "Arraylist", "Locked Target"
    };

    private static final int PANEL_WIDTH = 116;
    private static final int HEADER_HEIGHT = 26;
    private static final int PADDING = 6;
    private static final int RADIUS = 9;
    private static final float ICON_SIZE = 9.0f;
    private static final long ANIM_MS = 180L;

    private final Screen parent;
    private final long openedAt = System.currentTimeMillis();
    private int panelX;
    private int panelY;
    private boolean dragging;
    private double dragOffsetX;
    private double dragOffsetY;

    public HudAddScreen(Screen parent, int clickX, int clickY) {
        super(Component.empty());
        this.parent = parent;
        this.panelX = clickX;
        this.panelY = clickY;
    }

    @Override
    protected void init() {
        clampPanel();
    }

    private int rowHeight() {
        int available = this.height - 14 - HEADER_HEIGHT - PADDING;
        return Math.max(11, Math.min(17, available / OPTIONS.length));
    }

    private int panelHeight() {
        return HEADER_HEIGHT + rowHeight() * OPTIONS.length + PADDING;
    }

    private void clampPanel() {
        int maxX = Math.max(0, this.width - PANEL_WIDTH);
        int maxY = Math.max(0, this.height - panelHeight());
        panelX = Math.max(0, Math.min(panelX, maxX));
        panelY = Math.max(0, Math.min(panelY, maxY));
    }

    private float progress() {
        float t = Math.min(1.0f, (System.currentTimeMillis() - openedAt) / (float) ANIM_MS);
        float inv = 1.0f - t;
        return 1.0f - inv * inv * inv;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        if (parent != null) {
            parent.extractRenderState(graphics, mouseX, mouseY, delta);
        } else {
            super.extractRenderState(graphics, mouseX, mouseY, delta);
        }

        float p = progress();
        float scale = 0.90f + 0.10f * p;
        int h = panelHeight();
        int rowH = rowHeight();

        graphics.pose().pushMatrix();
        graphics.pose().translate(panelX, panelY);
        graphics.pose().scale(scale, scale);
        graphics.pose().translate(-panelX, -panelY);

        // soft shadow + translucent body
        Shapes.roundRect(graphics, panelX - 1, panelY + 1, PANEL_WIDTH + 2, h + 2, RADIUS + 1, Theme.withAlpha(0x38000000, p));
        Shapes.roundRect(graphics, panelX, panelY, PANEL_WIDTH, h, RADIUS, Theme.withAlpha(Theme.panelColor, p));

        int textColor = Theme.withAlpha(Theme.textColor, p);
        int iconColor = Theme.withAlpha(Theme.iconColor, p);

        graphics.text(this.font, UiFont.of("What to add?"), panelX + 12, panelY + 9, textColor, false);

        for (int i = 0; i < OPTIONS.length; i++) {
            int rowY = panelY + HEADER_HEIGHT + i * rowH;
            boolean hovered = p >= 1.0f && mouseX >= panelX && mouseX < panelX + PANEL_WIDTH
                    && mouseY >= rowY && mouseY < rowY + rowH;
            if (hovered) {
                Shapes.roundRect(graphics, panelX + 4, rowY, PANEL_WIDTH - 8, rowH, 5, Theme.hoverColor);
            }
            int textY = rowY + (rowH - this.font.lineHeight) / 2 + 1;
            graphics.text(this.font, UiFont.of(OPTIONS[i]), panelX + 12, textY, textColor, false);
            Icons.draw(graphics, Icons.indexOf(OPTIONS[i]), panelX + PANEL_WIDTH - 12 - ICON_SIZE,
                    rowY + (rowH - ICON_SIZE) / 2.0f, ICON_SIZE, iconColor);
        }

        graphics.pose().popMatrix();
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
            boolean insideX = mouseX >= panelX && mouseX < panelX + PANEL_WIDTH;
            if (insideX && mouseY >= panelY && mouseY < panelY + HEADER_HEIGHT) {
                dragging = true;
                dragOffsetX = mouseX - panelX;
                dragOffsetY = mouseY - panelY;
                return true;
            }
            // Options do nothing yet (next steps): no selection is kept.
        }
        return true;
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
        if (dragging) {
            panelX = (int) (event.x() - dragOffsetX);
            panelY = (int) (event.y() - dragOffsetY);
            clampPanel();
        }
        return true;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        dragging = false;
        return true;
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
