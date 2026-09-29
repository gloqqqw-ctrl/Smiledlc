package com.smiledlc.module;

import com.smiledlc.module.modules.*;
import java.util.*;

public class ModuleManager {
    private static List<Module> modules = new ArrayList<>();
    private static Map<String, List<Module>> categorizedModules = new HashMap<>();

    public static void init() {
        registerModule(new IgnoreCobwebs());
        registerModule(new AutoLever());
        registerModule(new AutoDripstone());
        registerModule(new AntiNemos());
        registerModule(new RearStrike());
        registerModule(new AimAssist());
        registerModule(new TriggerBot());
        registerModule(new Speed());
        registerModule(new FastPlace());
        
        categorizeModules();
    }

    private static void registerModule(Module module) {
        modules.add(module);
    }

    private static void categorizeModules() {
        categorizedModules.clear();
        for (Module module : modules) {
            String category = module.getCategory();
            categorizedModules.putIfAbsent(category, new ArrayList<>());
            categorizedModules.get(category).add(module);
        }
    }

    public static List<Module> getModulesByCategory(String category) {
        return categorizedModules.getOrDefault(category, new ArrayList<>());
    }

    public static List<String> getCategories() {
        return new ArrayList<>(categorizedModules.keySet());
    }

    public static Module getModuleByName(String name) {
        return modules.stream()
            .filter(m -> m.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }

    public static List<Module> getAllModules() {
        return new ArrayList<>(modules);
    }

    public static void onTick() {
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.onTick();
            }
        }
    }
}
