package com.smiledlc.module.modules;

import com.smiledlc.module.Module;

public class AntiNemos extends Module {
    public AntiNemos() {
        super("AntiNemos", "Player");
        addFloatSetting("value", 0f, 0f, 10f);
    }

    @Override public void onEnable() { }
    @Override public void onDisable() { }
}
