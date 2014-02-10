package doggytalents.client.gui;

import java.util.ArrayList;

public class GuiTalentDecrease extends GuiSmallButton {

	public GuiTalentDecrease(int id, int xPosition, int yPosition, int width, int height, String text) {
		super(id, xPosition, yPosition, width, height, text);
	}

	@Override
	public ArrayList getToolTip() {
		ArrayList ret = new ArrayList();
		ret.add("-1 to Skill");
		ret.add("Costs: 3");
		return ret;
	}

}
