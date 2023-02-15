package doggytalents.client.screen.DogNewInfoScreen.store;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ToolTipOverlayManager {

    private static ToolTipOverlayManager INSTANCE;

    private List<Component> toolTipComponents;
    private boolean hasToolTip;

    private ToolTipOverlayManager() {}

    public void setComponents(List<Component> components) {
        this.hasToolTip = components != null;
        this.toolTipComponents = components;
    }

    public void reset() {
        this.setComponents(null);
    }

    public boolean hasToolTip() {
        return hasToolTip;
    }

    public void render(Screen screen, PoseStack stack, int mouseX, int mouseY) {
        if (toolTipComponents == null) return;
        screen.renderComponentTooltip(stack, toolTipComponents, mouseX, mouseY);
    }

    public static ToolTipOverlayManager get() {
        if (INSTANCE == null) {
            INSTANCE = new ToolTipOverlayManager();
        }
        return INSTANCE;
    }

    public static void finish() {
        INSTANCE = null;
    }
}
