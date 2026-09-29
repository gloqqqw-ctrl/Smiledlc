package com.smiledlc.module.modules;

import com.smiledlc.module.Module;

public class AimAssist extends Module {
    private float speed = 0.0f;

    public AimAssist() {
        super("AimAssist", "Combat");
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = Math.max(0, Math.min(10, speed));
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onTick() {
    }
}
