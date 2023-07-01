package dev.zenhao.melon.event.events.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class Render2DEvent extends Event {
    public ScaledResolution getResolution() {
        return new ScaledResolution(Minecraft.getMinecraft());
    }

}