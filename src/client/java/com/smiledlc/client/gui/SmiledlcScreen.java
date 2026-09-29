package com.smiledlc.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import com.smiledlc.module.ModuleManager;
import com.smiledlc.module.Module;

import java.util.ArrayList;
import java.util.List;

public class SmiledlcScreen extends Screen {
    private static final int PANEL_WIDTH = 250;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_SPACING = 4;

    private List<String> categories = new ArrayList<>();
    private String selectedCategory = "Combat";
    private List<Module> visibleModules = new ArrayList<>();

    public SmiledlcScreen() {
        super(Text.literal("Smiledlc"));
    }

    @Override
    protected void init() {
        super.init();
        this.categories.clear();
        this.categories.addAll(ModuleManager.getCategories());
        if (!this.categories.contains(this.selectedCategory)) {
            this.selectedCategory = this.categories.isEmpty() ? "Combat" : this.categories.get(0);
        }
        updateModules();
    }

    private void updateModules() {
        this.visibleModules.clear();
        this.visibleModules.addAll(ModuleManager.getModulesByCategory(this.selectedCategory));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        int x = 10;
        int y = 10;

        context.drawCenteredTextWithShadow(this.textRenderer, "Smiledlc", this.width / 2, y, 0xFFFFFF);
        y += 20;

        for (String category : this.categories) {
            int bgColor = category.equals(this.selectedCategory) ? 0xFF00AA00 : 0xFF333333;
            context.fill(x, y, x + PANEL_WIDTH, y + BUTTON_HEIGHT, bgColor);
            context.drawCenteredTextWithShadow(this.textRenderer, category, x + PANEL_WIDTH / 2, y + 5, 0xFFFFFF);
            y += BUTTON_HEIGHT + BUTTON_SPACING;
        }

        y += 10;

        for (Module module : this.visibleModules) {
            int moduleColor = module.isEnabled() ? 0xFF00FF00 : 0xFF444444;
            context.fill(x, y, x + PANEL_WIDTH, y + BUTTON_HEIGHT, moduleColor);
            context.drawTextWithShadow(this.textRenderer, module.getName(), x + 8, y + 5, 0xFFFFFF);

            float sliderValue = module.getFloat("value") / 10f;
            int sliderWidth = (int) ((PANEL_WIDTH - 20) * sliderValue);
            context.fill(x + 10, y + 12, x + 10 + sliderWidth, y + 16, 0xFF0088FF);

            y += BUTTON_HEIGHT + BUTTON_SPACING;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = 10;
        int y = 30;

        for (String category : this.categories) {
            if (mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= y && mouseY <= y + BUTTON_HEIGHT) {
                this.selectedCategory = category;
                updateModules();
                return true;
            }
            y += BUTTON_HEIGHT + BUTTON_SPACING;
        }

        y += 10;

        for (Module module : this.visibleModules) {
            if (mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= y && mouseY <= y + BUTTON_HEIGHT) {
                module.toggle();
                return true;
            }

            if (mouseX >= x + 10 && mouseX <= x + PANEL_WIDTH - 10 && mouseY >= y + 12 && mouseY <= y + 16) {
                float ratio = (float) (mouseX - x - 10) / (PANEL_WIDTH - 20);
                module.setFloat("value", ratio * 10f);
                return true;
            }

            y += BUTTON_HEIGHT + BUTTON_SPACING;
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
}
