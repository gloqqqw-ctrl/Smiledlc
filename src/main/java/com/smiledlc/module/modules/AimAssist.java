package com.smiledlc.module.modules;

import com.smiledlc.module.Module;

public class AimAssist extends Module {
    public AimAssist() {
        super("AimAssist", "Combat");
        addFloatSetting("value", 0f, 0f, 10f);
    }

    @Override public void onEnable() { }
    @Override public void onDisable() { }
}
