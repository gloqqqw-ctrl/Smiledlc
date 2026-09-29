package com.smiledlc.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import com.smiledlc.module.ModuleManager;
import com.smiledlc.module.Module;

import java.util.ArrayList;
import java.util.List;

public class SmiledlcScreen extends Screen {
    private String currentCategory = "Combat";
    private List<String> categories;
    private List<Module> currentModules;
    
    private int scrollOffset = 0;
    private static final int MODULE_HEIGHT = 25;
    private static final int CATEGORY_WIDTH = 90;
    private static final int MODULE_WIDTH = 180;
    private static final int SLIDER_WIDTH = 100;

    public SmiledlcScreen() {
        super(Text.literal("Smiledlc"));
    }

    @Override
    protected void init() {
        super.init();
        this.categories = new ArrayList<>(ModuleManager.getCategories());
        updateModuleList();
    }

    private void updateModuleList() {
        this.currentModules = new ArrayList<>(ModuleManager.getModulesByCategory(currentCategory));
        this.scrollOffset = 0;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        // Nagłówek
        context.drawCenteredTextWithShadow(this.textRenderer, "Smiledlc", this.width / 2, 10, 0xFFFFFF);

        // Rysuj kategorie (lewo)
        int categoryX = 10;
        int categoryY = 35;
        for (String category : categories) {
            boolean isSelected = category.equals(currentCategory);
            int bgColor = isSelected ? 0xFF00FF00 : 0xFF444444;
            int textColor = isSelected ? 0xFF000000 : 0xFFFFFFFF;
            
            // Tło przycisku kategorii
            context.fill(categoryX, categoryY, categoryX + CATEGORY_WIDTH, categoryY + MODULE_HEIGHT, bgColor);
            // Tekst kategorii
            context.drawCenteredTextWithShadow(this.textRenderer, category, 
                categoryX + CATEGORY_WIDTH / 2, categoryY + (MODULE_HEIGHT - 8) / 2, textColor);
            
            // Klik na kategorię
            if (mouseX >= categoryX && mouseX < categoryX + CATEGORY_WIDTH && 
                mouseY >= categoryY && mouseY < categoryY + MODULE_HEIGHT) {
                if (this.isMouseInBounds(mouseX, mouseY)) {
                    // Będzie obsłużone w mouseClicked
                }
            }
            
            categoryY += MODULE_HEIGHT + 5;
        }

        // Rysuj moduły (prawo)
        int moduleX = categoryX + CATEGORY_WIDTH + 20;
        int moduleY = 35;
        int maxModules = (this.height - 60) / (MODULE_HEIGHT + 5);
        int startIndex = Math.max(0, scrollOffset / (MODULE_HEIGHT + 5));
        int endIndex = Math.min(currentModules.size(), startIndex + maxModules);

        for (int i = startIndex; i < endIndex; i++) {
            Module module = currentModules.get(i);
            int displayY = moduleY + (i - startIndex) * (MODULE_HEIGHT + 5);
            
            // Tło przycisku modułu
            int moduleBgColor = module.isEnabled() ? 0xFF00AA00 : 0xFF444444;
            context.fill(moduleX, displayY, moduleX + MODULE_WIDTH, displayY + MODULE_HEIGHT, moduleBgColor);
            
            // Tekst modułu
            int textColor = module.isEnabled() ? 0xFF000000 : 0xFFFFFFFF;
            context.drawCenteredTextWithShadow(this.textRenderer, module.getName(), 
                moduleX + MODULE_WIDTH / 2, displayY + (MODULE_HEIGHT - 8) / 2, textColor);
            
            // Slider dla modułów z ustawieniami
            if (module.getName().equals("AimAssist") || module.getName().equals("Speed")) {
                int sliderX = moduleX + MODULE_WIDTH + 20;
                renderSlider(context, sliderX, displayY, module);
            }
        }
        
        // Wskaźnik myszki
        context.drawTextWithShadow(this.textRenderer, "X: " + mouseX + " Y: " + mouseY, 10, this.height - 20, 0xFFFFFF);
    }

    private void renderSlider(DrawContext context, int x, int y, Module module) {
        float value = 0;
        String label = "";
        
        if (module.getName().equals("AimAssist")) {
            value = ((com.smiledlc.module.modules.AimAssist) module).getSpeed() / 10f;
            label = "Speed: " + (int)(value * 10);
        } else if (module.getName().equals("Speed")) {
            value = ((com.smiledlc.module.modules.Speed) module).getAmount() / 10f;
            label = "Amt: " + (int)(value * 10);
        }
        
        // Tło suwaka
        context.fill(x, y, x + SLIDER_WIDTH, y + MODULE_HEIGHT, 0xFF333333);
        
        // Wypełnienie suwaka
        int filledWidth = (int)(SLIDER_WIDTH * value);
        context.fill(x, y, x + filledWidth, y + MODULE_HEIGHT, 0xFF0088FF);
        
        // Tekst
        context.drawCenteredTextWithShadow(this.textRenderer, label, 
            x + SLIDER_WIDTH / 2, y + (MODULE_HEIGHT - 8) / 2, 0xFFFFFF);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollY > 0) {
            scrollOffset = Math.max(0, scrollOffset - MODULE_HEIGHT - 5);
        } else {
            scrollOffset += MODULE_HEIGHT + 5;
        }
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int categoryX = 10;
        int categoryY = 35;
        
        // Kliknięcie na kategorię
        for (String category : categories) {
            if (mouseX >= categoryX && mouseX < categoryX + CATEGORY_WIDTH && 
                mouseY >= categoryY && mouseY < categoryY + MODULE_HEIGHT) {
                this.currentCategory = category;
                updateModuleList();
                return true;
            }
            categoryY += MODULE_HEIGHT + 5;
        }
        
        // Kliknięcie na moduł
        int moduleX = categoryX + CATEGORY_WIDTH + 20;
        int moduleY = 35;
        int maxModules = (this.height - 60) / (MODULE_HEIGHT + 5);
        int startIndex = Math.max(0, scrollOffset / (MODULE_HEIGHT + 5));
        int endIndex = Math.min(currentModules.size(), startIndex + maxModules);

        for (int i = startIndex; i < endIndex; i++) {
            Module module = currentModules.get(i);
            int displayY = moduleY + (i - startIndex) * (MODULE_HEIGHT + 5);
            
            if (mouseX >= moduleX && mouseX < moduleX + MODULE_WIDTH && 
                mouseY >= displayY && mouseY < displayY + MODULE_HEIGHT) {
                module.toggle();
                return true;
            }
            
            // Slider interaction
            if (module.getName().equals("AimAssist") || module.getName().equals("Speed")) {
                int sliderX = moduleX + MODULE_WIDTH + 20;
                if (mouseX >= sliderX && mouseX < sliderX + SLIDER_WIDTH && 
                    mouseY >= displayY && mouseY < displayY + MODULE_HEIGHT) {
                    float sliderValue = (float)(mouseX - sliderX) / SLIDER_WIDTH;
                    
                    if (module.getName().equals("AimAssist")) {
                        ((com.smiledlc.module.modules.AimAssist) module).setSpeed(sliderValue * 10);
                    } else if (module.getName().equals("Speed")) {
                        ((com.smiledlc.module.modules.Speed) module).setAmount(sliderValue * 10);
                    }
                    return true;
                }
            }
        }
        
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void close() {
        this.client.setScreen(null);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
    
    private boolean isMouseInBounds(double mouseX, double mouseY) {
        return mouseX >= 0 && mouseX < this.width && mouseY >= 0 && mouseY < this.height;
    }
}
