package dev.zenhao.melon.command.commands.comm;

import dev.zenhao.melon.command.Command;
import dev.zenhao.melon.command.syntax.ChunkBuilder;
import dev.zenhao.melon.command.syntax.parsers.ModuleParser;
import dev.zenhao.melon.module.IModule;
import dev.zenhao.melon.module.ModuleManager;
import dev.zenhao.melon.setting.BooleanSetting;
import dev.zenhao.melon.setting.Setting;
import dev.zenhao.melon.utils.chat.ChatUtil;
import dev.zenhao.melon.utils.Wrapper;
import org.lwjgl.input.Keyboard;

public class BindCommand
extends Command {
    public static Setting<Boolean> modifiersEnabled = new BooleanSetting("modifiersEnabled", null, false);
    public static BindCommand INSTANCE;

    public BindCommand() {
        super("bind", new ChunkBuilder().append("[module]|modifiers", true, new ModuleParser()).append("[key]|[on|off]", true).build(), "b");
        this.setDescription("Binds a module to a key, or allows you to change modifier options");
        INSTANCE = this;
    }

    @Override
    public void call(String[] args2) {
        if (args2.length == 1) {
            ChatUtil.NoSpam.sendWarnMessage("Please specify a module.");
            return;
        }
        String module = args2[0];
        String rkey = args2[1];
        if (module.equalsIgnoreCase("modifiers")) {
            if (rkey == null) {
                ChatUtil.NoSpam.sendWarnMessage("Expected: on or off");
                return;
            }
            if (rkey.equalsIgnoreCase("on")) {
                modifiersEnabled.setValue(true);
                ChatUtil.NoSpam.sendMessage("Turned modifiers on.");
            } else if (rkey.equalsIgnoreCase("off")) {
                modifiersEnabled.setValue(false);
                ChatUtil.NoSpam.sendMessage("Turned modifiers off.");
            } else {
                ChatUtil.NoSpam.sendWarnMessage("Expected: on or off");
            }
            return;
        }
        IModule m = ModuleManager.getModuleByName(module);
        if (m == null) {
            ChatUtil.NoSpam.sendErrorMessage("Unknown module '" + module + "'!");
            return;
        }
        if (rkey == null) {
            ChatUtil.NoSpam.sendMessage(m.getName() + " is bound to &b" + Keyboard.getKeyName((int)m.getBind()));
            return;
        }
        int key = Wrapper.getKey(rkey);
        if (rkey.equalsIgnoreCase("none")) {
            key = -1;
        }
        if (key == 0) {
            ChatUtil.NoSpam.sendErrorMessage("Unknown key '" + rkey + "'!");
            return;
        }
        m.setBind(key);
        ChatUtil.NoSpam.sendMessage("Bind for &b" + m.getName() + "&r set to &b" + rkey.toUpperCase());
    }
}

