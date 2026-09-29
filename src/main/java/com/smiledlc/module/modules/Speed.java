package com.smiledlc.module.modules;

import com.smiledlc.module.Module;

public class Speed extends Module {
    private float amount = 0.0f;

    public Speed() {
        super("Speed", "Movement");
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = Math.max(0, Math.min(10, amount));
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
