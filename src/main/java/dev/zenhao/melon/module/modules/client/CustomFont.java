package dev.zenhao.melon.module.modules.client;

import dev.zenhao.melon.module.Category;
import dev.zenhao.melon.module.Module;
import dev.zenhao.melon.setting.ModeSetting;
import dev.zenhao.melon.utils.font.CFontRenderer;
import dev.zenhao.melon.utils.font.FontUtils;

@Module.Info(name = "CustomFont", category = Category.CLIENT, description = "Custom Font That We can change")
public class CustomFont extends Module {
    public static CustomFont INSTANCE;
    public ModeSetting<?> CGfont = this.msetting("CG Font", FontRender.ROBOTO);
    public ModeSetting<?> SetPanFont = this.msetting("SetPanFont", FontRender.MONTSERRAT);
    public ModeSetting<?> HUDfont = this.msetting("HUD Font", FontRender.CALIBRI);
    public ModeSetting<?> IDKfont = this.msetting("IDK Font", FontRender.CALIBRI);

    public static CFontRenderer getCGFont() {
        return INSTANCE != null ? certifiedFont((FontRender) INSTANCE.CGfont.getValue()) : FontUtils.Calibri;
    }

    public static CFontRenderer getSetPanFontFont() {
        return INSTANCE != null ? certifiedFont((FontRender) INSTANCE.SetPanFont.getValue()) : FontUtils.Calibri;
    }

    public static CFontRenderer getHUDFont() {
        return INSTANCE != null ? certifiedFont((FontRender) INSTANCE.HUDfont.getValue()) : FontUtils.Calibri;
    }

    public static CFontRenderer getIDKFont() {
        return INSTANCE != null ? certifiedFont((FontRender) INSTANCE.IDKfont.getValue()) : FontUtils.Calibri;
    }

    public static CFontRenderer certifiedFont(FontRender font) {
        switch (font) {
            case CALIBRI: {
                return FontUtils.Calibri;
            }
            case COMFORTAA: {
                return FontUtils.Comfortaa;
            }
            case FOUGHTKNIGHT: {
                return FontUtils.FoughtKnight;
            }
            case GOLDMAN: {
                return FontUtils.Goldman;
            }
            case LEMONMILK: {
                return FontUtils.LemonMilk;
            }
            case MODERNSPACE: {
                return FontUtils.ModernSpace;
            }
            case MONTSERRAT: {
                return FontUtils.Montserrat;
            }
            case NINJAGO: {
                return FontUtils.Ninjago;
            }
            case ROBOTO: {
                return FontUtils.Roboto;
            }
        }
        return FontUtils.Goldman;
    }

    @Override
    public void onInit() {
        INSTANCE = this;
    }

    public enum FontRender {
        CALIBRI,
        COMFORTAA,
        FOUGHTKNIGHT,
        GOLDMAN,
        LEMONMILK,
        MODERNSPACE,
        MONTSERRAT,
        NINJAGO,
        ROBOTO
    }
}
