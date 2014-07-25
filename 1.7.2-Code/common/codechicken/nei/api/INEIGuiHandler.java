package codechicken.nei.api;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import codechicken.nei.VisiblityData;

public interface INEIGuiHandler {

	VisiblityData modifyVisiblity(GuiContainer gui,
			VisiblityData currentVisibility);

	List<TaggedInventoryArea> getInventoryAreas(GuiContainer gui);

	boolean handleDragNDrop(GuiContainer gui, int mousex, int mousey,
			ItemStack draggedStack, int button);

	Iterable<Integer> getItemSpawnSlots(GuiContainer arg0, ItemStack arg1);

	boolean hideItemPanelSlot(GuiContainer arg0, int arg1, int arg2, int arg3,
			int arg4);

}
