package com.smiledlc.module.modules;

import com.smiledlc.module.Module;

public class AutoDripstone extends Module {
    public AutoDripstone() {
        super("AutoDripstone", "Player");
        addFloatSetting("value", 0f, 0f, 10f);
    }

    @Override public void onEnable() { }
    @Override public void onDisable() { }
}
