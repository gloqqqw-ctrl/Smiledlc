package com.smiledlc.module.modules;

import com.smiledlc.module.Module;

public class FastPlace extends Module {
    public FastPlace() {
        super("FastPlace", "Player");
        addFloatSetting("value", 0f, 0f, 10f);
    }

    @Override public void onEnable() { }
    @Override public void onDisable() { }
}
