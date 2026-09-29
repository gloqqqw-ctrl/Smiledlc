package com.smiledlc.module.modules;

import com.smiledlc.module.Module;

public class TriggerBot extends Module {
    public TriggerBot() {
        super("TriggerBot", "Combat");
        addFloatSetting("value", 0f, 0f, 10f);
    }

    @Override public void onEnable() { }
    @Override public void onDisable() { }
}
