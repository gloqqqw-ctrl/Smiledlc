package com.smiledlc.module.modules;

import com.smiledlc.module.Module;

public class RearStrike extends Module {
    public RearStrike() {
        super("RearStrike", "Combat");
        addFloatSetting("value", 0f, 0f, 10f);
    }

    @Override public void onEnable() { }
    @Override public void onDisable() { }
}
