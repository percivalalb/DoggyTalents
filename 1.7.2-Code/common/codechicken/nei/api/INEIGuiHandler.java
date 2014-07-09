package codechicken.nei.api;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import codechicken.nei.VisiblityData;

public interface INEIGuiHandler {

	int getItemSpawnSlot(GuiContainer gui, ItemStack item);

	VisiblityData modifyVisiblity(GuiContainer gui,
			VisiblityData currentVisibility);

	List<TaggedInventoryArea> getInventoryAreas(GuiContainer gui);

	boolean handleDragNDrop(GuiContainer gui, int mousex, int mousey,
			ItemStack draggedStack, int button);

}
