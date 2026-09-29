package com.smiledlc.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.smiledlc.module.Module;
import com.smiledlc.module.ModuleManager;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class SmiledlcScreen extends Screen {
    private static final int CATEGORY_X = 14;
    private static final int CATEGORY_Y = 28;
    private static final int CATEGORY_WIDTH = 110;
    private static final int CATEGORY_HEIGHT = 22;
    private static final int MODULE_X = 150;
    private static final int MODULE_WIDTH = 180;
    private static final int MODULE_HEIGHT = 18;
    private static final int SLIDER_WIDTH = 110;

    private final List<String> categories = new ArrayList<>();
    private String selectedCategory = "Combat";
    private List<Module> visibleModules = new ArrayList<>();
    private int scrollOffset;

    public SmiledlcScreen() {
        super(Text.literal("Smiledlc"));
    }

    @Override
    protected void init() {
        super.init();
        categories.clear();
        categories.addAll(ModuleManager.getCategories());
        if (!categories.contains(selectedCategory)) {
            selectedCategory = categories.isEmpty() ? "Combat" : categories.get(0);
        }
        refreshModules();
    }

    private void refreshModules() {
        visibleModules = ModuleManager.getModulesByCategory(selectedCategory);
        scrollOffset = 0;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, "Smiledlc", this.width / 2, 10, 0xFFFFFFFF);

        int y = CATEGORY_Y;
        for (String category : categories) {
            int color = category.equals(selectedCategory) ? 0xFF2E7D32 : 0xFF3C3C3C;
            context.fill(CATEGORY_X, y, CATEGORY_X + CATEGORY_WIDTH, y + CATEGORY_HEIGHT, color);
            context.drawCenteredTextWithShadow(this.textRenderer, category, CATEGORY_X + CATEGORY_WIDTH / 2, y + 5, 0xFFFFFFFF);
            y += CATEGORY_HEIGHT + 6;
        }

        int moduleY = 28;
        int count = 0;
        for (Module module : visibleModules) {
            int renderY = moduleY + count * (MODULE_HEIGHT + 6) - scrollOffset;
            if (renderY < 20 || renderY > this.height) {
                count++;
                continue;
            }

            int color = module.isEnabled() ? 0xFF00C853 : 0xFF424242;
            context.fill(MODULE_X, renderY, MODULE_X + MODULE_WIDTH, renderY + MODULE_HEIGHT, color);
            context.drawTextWithShadow(this.textRenderer, module.getName(), MODULE_X + 8, renderY + 4, 0xFFFFFFFF);

            for (String key : List.of("value")) {
                if (module.getFloat(key) == 0f) {
                    continue;
                }
                int sliderX = MODULE_X + MODULE_WIDTH + 16;
                int sliderY = renderY;
                int valueWidth = (int) ((module.getFloat(key) / 10f) * SLIDER_WIDTH);
                context.fill(sliderX, sliderY, sliderX + SLIDER_WIDTH, sliderY + MODULE_HEIGHT, 0xFF2A2A2A);
                context.fill(sliderX, sliderY, sliderX + valueWidth, sliderY + MODULE_HEIGHT, 0xFF00B0FF);
                context.drawTextWithShadow(this.textRenderer, "0..10", sliderX + 4, sliderY + 4, 0xFFFFFFFF);
            }

            count++;
        }

        context.drawTextWithShadow(this.textRenderer, "Right Shift = open menu", 12, this.height - 18, 0xFFB0B0B0);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int categoryY = CATEGORY_Y;
        for (String category : categories) {
            if (mouseX >= CATEGORY_X && mouseX <= CATEGORY_X + CATEGORY_WIDTH && mouseY >= categoryY && mouseY <= categoryY + CATEGORY_HEIGHT) {
                selectedCategory = category;
                refreshModules();
                return true;
            }
            categoryY += CATEGORY_HEIGHT + 6;
        }

        int moduleY = 28;
        int index = 0;
        for (Module module : visibleModules) {
            int renderY = moduleY + index * (MODULE_HEIGHT + 6) - scrollOffset;
            if (mouseX >= MODULE_X && mouseX <= MODULE_X + MODULE_WIDTH && mouseY >= renderY && mouseY <= renderY + MODULE_HEIGHT) {
                module.toggle();
                return true;
            }

            int sliderX = MODULE_X + MODULE_WIDTH + 16;
            int sliderY = renderY;
            if (mouseX >= sliderX && mouseX <= sliderX + SLIDER_WIDTH && mouseY >= sliderY && mouseY <= sliderY + MODULE_HEIGHT) {
                float ratio = (float) (mouseX - sliderX) / (float) SLIDER_WIDTH;
                module.setFloat("value", ratio * 10f);
                return true;
            }
            index++;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollOffset += verticalAmount > 0 ? 18 : -18;
        scrollOffset = Math.max(0, scrollOffset);
        return true;
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
