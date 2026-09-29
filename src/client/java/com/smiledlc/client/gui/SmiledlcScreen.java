package com.smiledlc.client.gui;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import com.smiledlc.module.ModuleManager;
import com.smiledlc.module.Module;

public class SmiledlcScreen extends Screen {
    private static final int PANEL_WIDTH = 280;
    private static final int BUTTON_HEIGHT = 24;
    private static final int SPACING = 6;
    private static final int SLIDER_HEIGHT = 6;

    private final List<String> categories = new ArrayList<>();
    private String selectedCategory = "Combat";
    private final List<Module> visibleModules = new ArrayList<>();
    private int scrollOffset = 0;

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
        this.visibleModules.addAll(ModuleManager.getByCategory(this.selectedCategory));
        this.scrollOffset = 0;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        int x = 15;
        int y = 15;

        // Title
        context.drawCenteredTextWithShadow(this.textRenderer, "Smiledlc", this.width / 2, y, 0xFFFFFF);
        y += 25;

        // Categories
        int catX = x;
        int catY = y;
        for (String category : this.categories) {
            int color = category.equals(this.selectedCategory) ? 0xFF2E7D32 : 0xFF424242;
            context.fill(catX, catY, catX + PANEL_WIDTH, catY + BUTTON_HEIGHT, color);
            context.drawCenteredTextWithShadow(this.textRenderer, category, catX + PANEL_WIDTH / 2, catY + 7, 0xFFFFFF);
            catY += BUTTON_HEIGHT + SPACING;
        }

        y = catY + 10;

        // Modules
        int modY = y;
        for (int i = 0; i < this.visibleModules.size(); i++) {
            Module module = this.visibleModules.get(i);
            int renderY = modY + i * (BUTTON_HEIGHT + SPACING) - this.scrollOffset;

            if (renderY < y - 20 || renderY > this.height - 20) continue;

            int color = module.isEnabled() ? 0xFF00AA00 : 0xFF424242;
            context.fill(x, renderY, x + PANEL_WIDTH, renderY + BUTTON_HEIGHT, color);
            context.drawTextWithShadow(this.textRenderer, module.getName(), x + 10, renderY + 6, 0xFFFFFF);

            // Slider
            float value = module.getValue("speed");
            if (value > 0 || module.getName().contains("AimAssist") || module.getName().contains("Speed")) {
                int sliderX = x + 10;
                int sliderY = renderY + 16;
                int sliderW = PANEL_WIDTH - 20;
                int filled = (int) ((value / 10f) * sliderW);

                context.fill(sliderX, sliderY, sliderX + sliderW, sliderY + SLIDER_HEIGHT, 0xFF333333);
                context.fill(sliderX, sliderY, sliderX + filled, sliderY + SLIDER_HEIGHT, 0xFF00B0FF);
                context.drawTextWithShadow(this.textRenderer, (int) value + "/10", sliderX + 5, sliderY - 8, 0xAAAAAA);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = 15;
        int y = 40;

        // Category click
        for (String category : this.categories) {
            if (mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= y && mouseY <= y + BUTTON_HEIGHT) {
                this.selectedCategory = category;
                updateModules();
                return true;
            }
            y += BUTTON_HEIGHT + SPACING;
        }

        y += 10;

        // Module click
        for (int i = 0; i < this.visibleModules.size(); i++) {
            Module module = this.visibleModules.get(i);
            int renderY = y + i * (BUTTON_HEIGHT + SPACING) - this.scrollOffset;

            if (mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= renderY && mouseY <= renderY + BUTTON_HEIGHT) {
                module.toggle();
                return true;
            }

            // Slider click
            int sliderX = x + 10;
            int sliderY = renderY + 16;
            int sliderW = PANEL_WIDTH - 20;
            if (mouseX >= sliderX && mouseX <= sliderX + sliderW && mouseY >= sliderY && mouseY <= sliderY + SLIDER_HEIGHT) {
                float ratio = (float) (mouseX - sliderX) / sliderW;
                module.setValue("speed", ratio * 10f);
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        this.scrollOffset = Math.max(0, (int) (this.scrollOffset + verticalAmount * 20));
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
