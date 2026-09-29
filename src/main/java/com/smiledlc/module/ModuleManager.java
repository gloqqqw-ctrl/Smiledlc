package com.smiledlc.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smiledlc.module.modules.AimAssist;
import com.smiledlc.module.modules.AntiNemos;
import com.smiledlc.module.modules.AutoDripstone;
import com.smiledlc.module.modules.AutoLever;
import com.smiledlc.module.modules.FastPlace;
import com.smiledlc.module.modules.IgnoreCobwebs;
import com.smiledlc.module.modules.RearStrike;
import com.smiledlc.module.modules.Speed;
import com.smiledlc.module.modules.TriggerBot;

public final class ModuleManager {
    private static final List<Module> MODULES = new ArrayList<>();
    private static final Map<String, List<Module>> BY_CATEGORY = new HashMap<>();

    private ModuleManager() {
    }

    public static void init() {
        if (!MODULES.isEmpty()) {
            return;
        }

        register(new IgnoreCobwebs());
        register(new AutoLever());
        register(new AutoDripstone());
        register(new AntiNemos());
        register(new RearStrike());
        register(new AimAssist());
        register(new TriggerBot());
        register(new Speed());
        register(new FastPlace());
    }

    private static void register(Module module) {
        MODULES.add(module);
        BY_CATEGORY.computeIfAbsent(module.getCategory(), ignored -> new ArrayList<>()).add(module);
    }

    public static List<String> getCategories() {
        return new ArrayList<>(BY_CATEGORY.keySet());
    }

    public static List<Module> getModulesByCategory(String category) {
        return new ArrayList<>(BY_CATEGORY.getOrDefault(category, List.of()));
    }

    public static List<Module> getAllModules() {
        return new ArrayList<>(MODULES);
    }
}
