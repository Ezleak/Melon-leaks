package dev.zenhao.melon.mixin.client.gui;

import dev.zenhao.melon.command.Command;
import dev.zenhao.melon.gui.mcguichat.MelonGUIChat;
import dev.zenhao.melon.utils.Wrapper;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {GuiChat.class})
public abstract class MixinGuiChat {
    @Shadow
    public GuiTextField inputField;
    @Shadow
    public String historyBuffer;
    @Shadow
    public int sentHistoryCursor;

    @Inject(method = "keyTyped(CI)V", at = @At("RETURN"))
    public void returnKeyTyped(char typedChar, int keyCode, CallbackInfo info) {
        if (!(Wrapper.getMinecraft().currentScreen instanceof GuiChat) || Wrapper.getMinecraft().currentScreen instanceof MelonGUIChat)
            return;
        if (inputField.getText().startsWith(Command.getCommandPrefix())) {
            Wrapper.getMinecraft().displayGuiScreen(new MelonGUIChat(inputField.getText(), historyBuffer, sentHistoryCursor));
        }
    }
}

