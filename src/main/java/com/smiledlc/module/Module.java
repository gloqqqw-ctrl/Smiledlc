package com.smiledlc.module;

import java.util.HashMap;
import java.util.Map;

public abstract class Module {
    private final String name;
    private final String category;
    private boolean enabled;
    protected final Map<String, Float> values = new HashMap<>();

    protected Module(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public final String getName() { return name; }
    public final String getCategory() { return category; }
    public final boolean isEnabled() { return enabled; }

    public final void toggle() {
        setEnabled(!enabled);
    }

    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) onEnable();
        else onDisable();
    }

    public final float getValue(String key) {
        return values.getOrDefault(key, 0f);
    }

    public final void setValue(String key, float value) {
        values.put(key, Math.max(0, Math.min(10, value)));
    }

    public abstract void onEnable();
    public abstract void onDisable();
}
