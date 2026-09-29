package com.smiledlc.module;

import java.util.HashMap;
import java.util.Map;

public abstract class Module {
    private final String name;
    private final String category;
    private boolean enabled;
    private final Map<String, FloatSetting> settings = new HashMap<>();

    protected Module(String name, String category) {
        this.name = name;
        this.category = category;
    }

    protected final void addFloatSetting(String key, float defaultValue, float min, float max) {
        settings.put(key, new FloatSetting(defaultValue, min, max));
    }

    public final float getFloat(String key) {
        FloatSetting setting = settings.get(key);
        return setting == null ? 0f : setting.value;
    }

    public final void setFloat(String key, float value) {
        FloatSetting setting = settings.get(key);
        if (setting == null) {
            return;
        }
        setting.value = Math.max(setting.min, Math.min(setting.max, value));
    }

    public final String getName() {
        return name;
    }

    public final String getCategory() {
        return category;
    }

    public final boolean isEnabled() {
        return enabled;
    }

    public final void toggle() {
        setEnabled(!enabled);
    }

    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public abstract void onEnable();
    public abstract void onDisable();

    private static final class FloatSetting {
        float value;
        final float min;
        final float max;

        FloatSetting(float value, float min, float max) {
            this.value = value;
            this.min = min;
            this.max = max;
        }
    }
}
