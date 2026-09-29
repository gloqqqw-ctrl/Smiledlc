package com.smiledlc.module;

import java.util.*;
import com.smiledlc.module.modules.*;

public class ModuleManager {
    private static final Map<String, List<Module>> MODULES_BY_CATEGORY = new HashMap<>();
    private static final List<Module> ALL_MODULES = new ArrayList<>();

    public static void init() {
        if (!ALL_MODULES.isEmpty()) return;

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
        ALL_MODULES.add(module);
        MODULES_BY_CATEGORY.computeIfAbsent(module.getCategory(), k -> new ArrayList<>()).add(module);
    }

    public static List<String> getCategories() {
        return new ArrayList<>(MODULES_BY_CATEGORY.keySet());
    }

    public static List<Module> getByCategory(String category) {
        return new ArrayList<>(MODULES_BY_CATEGORY.getOrDefault(category, new ArrayList<>()));
    }

    public static List<Module> getAll() {
        return new ArrayList<>(ALL_MODULES);
    }
}
