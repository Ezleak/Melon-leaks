package dev.zenhao.melon.utils.render.gui;

import java.awt.Color;

public class Rainbow {
    public static int getRainbow(float speed, float saturation, float brightness) {
        float hue = (System.currentTimeMillis() % (int)(speed * 1000)) / (speed * 1000);
        return Color.HSBtoRGB(hue , saturation, brightness);
    }

    public static int getRainbow(float speed, float saturation, float brightness, long add) {
        float hue = ((System.currentTimeMillis() + add) % (int)(speed * 1000)) / (speed * 1000);
        return Color.HSBtoRGB(hue , saturation, brightness);
    }
}
