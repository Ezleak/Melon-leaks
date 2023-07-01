package dev.zenhao.melon.module.modules.render;

import dev.zenhao.melon.module.Category;
import dev.zenhao.melon.module.Module;
import dev.zenhao.melon.setting.BooleanSetting;
import dev.zenhao.melon.setting.DoubleSetting;
import dev.zenhao.melon.setting.BooleanSetting;
import dev.zenhao.melon.setting.DoubleSetting;

@Module.Info(name="CameraClip", category=Category.RENDER)
public class CameraClip
extends Module {
    private static CameraClip INSTANCE;
    public BooleanSetting extend = this.bsetting("Extend", false);
    public DoubleSetting distance = this.dsetting("Distance", 0.0, 0.0, 100.0);

    @Override
    public void onInit() {
        INSTANCE = this;
    }

    public static CameraClip getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CameraClip();
        }
        return INSTANCE;
    }
}

