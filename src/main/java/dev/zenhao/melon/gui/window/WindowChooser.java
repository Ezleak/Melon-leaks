package dev.zenhao.melon.gui.window;

import dev.zenhao.melon.gui.settingpanel.Window;
import dev.zenhao.melon.gui.settingpanel.component.components.Pane;
import dev.zenhao.melon.manager.GuiManager;
import dev.zenhao.melon.utils.Rainbow;
import dev.zenhao.melon.utils.font.RFontRenderer;
import dev.zenhao.melon.utils.render.RenderUtils;

import java.awt.*;

public class WindowChooser {
    public static Color BACKGROUND = new Color(20, 20, 20, 220);
    private final String title;
    private int y;
    private int x;
    private int width;
    private int height;
    private final RFontRenderer RFontRender = new RFontRenderer(new Font("Consoles", Font.PLAIN, 18), true, false);
    private int headerHeight;
    private boolean beingDragged;
    private int dragX;
    private int dragY;
    private Pane contentPane;

    public WindowChooser(String title, int x, int y, int width, int height) {
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render() {
        int fontHeight = Window.getFontRenderer().getHeight();
        int headerFontOffset = fontHeight / 2;
        this.headerHeight = headerFontOffset * 2 + fontHeight;
        RenderUtils.drawRoundedRectangle(this.x, this.y, this.width, this.height, 15.0, BACKGROUND);
        RenderUtils.drawHalfRoundedRectangle(this.x, this.y, this.width, this.headerHeight, 15.0, RenderUtils.HalfRoundedDirection.Top, Rainbow.getRainbowColor(10.0f, 0.6f, 1.0f));
        if (GuiManager.getINSTANCE().isRainbow()) {
            RenderUtils.drawHalfRoundedRectangle(this.x, this.y, this.width, this.headerHeight, 15.0, RenderUtils.HalfRoundedDirection.Top, Rainbow.getRainbowColor(GuiManager.getINSTANCE().getColorINSTANCE().rainbowSpeed.getValue().floatValue(), 0.6f, 0.8f));
            this.RFontRender.drawStringWithShadow(this.title, (float) this.x + (float) this.width / 2.0f - (float) this.RFontRender.getStringWidth(this.title) / 2.0f, this.y + headerFontOffset, GuiManager.getINSTANCE().getColorINSTANCE().rainbowSpeed.getValue().floatValue(), GuiManager.getINSTANCE().getColorINSTANCE().rainbowSaturation.getValue().floatValue(), GuiManager.getINSTANCE().getColorINSTANCE().rainbowBrightness.getValue().floatValue(), -20L, 255);
        } else {
            RenderUtils.drawHalfRoundedRectangle(this.x, this.y, this.width, this.headerHeight, 15.0, RenderUtils.HalfRoundedDirection.Top, new Color(80, 80, 80, 240));
            RenderUtils.getFontRender().drawStringWithShadow(this.title, (float) this.x + (float) this.width / 2.0f - (float) RenderUtils.getFontRender().getStringWidth(this.title) / 2.0f, this.y + headerFontOffset, -1);
        }
        if (this.contentPane != null) {
            if (this.contentPane.isSizeChanged()) {
                this.contentPane.setSizeChanged(false);
            }
            this.contentPane.setX(this.x);
            this.contentPane.setY(this.y + this.headerHeight + 30);
            this.contentPane.setWidth(this.width);
            this.contentPane.setHeight(this.height - this.headerHeight - 35);
            this.contentPane.render();
        }
    }

    public void mousePressed(int button, int x, int y) {
        if (this.contentPane != null) {
            this.contentPane.mousePressed(button, x, y, false);
        }
        if (button == 0 && x >= this.x && y >= this.y && x <= this.x + this.width && y <= this.y + this.headerHeight) {
            this.beingDragged = true;
            this.dragX = this.x - x;
            this.dragY = this.y - y;
        }
    }

    private void drag(int mouseX, int mouseY) {
        if (this.beingDragged) {
            this.x = mouseX + this.dragX;
            this.y = mouseY + this.dragY;
        }
    }

    public void mouseReleased(int button, int x, int y) {
        if (this.contentPane != null) {
            this.contentPane.mouseReleased(button, x, y, false);
        }
        if (button == 0) {
            this.beingDragged = false;
        }
    }

    public void mouseMoved(int x, int y) {
        if (this.contentPane != null) {
            this.contentPane.mouseMove(x, y, false);
        }
        this.drag(x, y);
    }

    public void setContentPane(Pane contentPane) {
        this.contentPane = contentPane;
    }

    public void keyPressed(char c, int key) {
        if (this.contentPane != null) {
            this.contentPane.keyPressed(c, key);
        }
    }

    public void mouseWheel(int change) {
        if (this.contentPane != null) {
            this.contentPane.mouseWheel(change);
        }
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

