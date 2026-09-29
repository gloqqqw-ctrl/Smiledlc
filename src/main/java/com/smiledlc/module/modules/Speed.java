package com.smiledlc.module.modules;

import com.smiledlc.module.Module;

public class Speed extends Module {
    public Speed() {
        super("Speed", "Movement");
        addFloatSetting("value", 0f, 0f, 10f);
    }

    @Override public void onEnable() { }
    @Override public void onDisable() { }
}
