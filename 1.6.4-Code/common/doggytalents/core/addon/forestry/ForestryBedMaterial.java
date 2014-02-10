package doggytalents.core.addon.forestry;

import net.minecraft.util.Icon;
import doggytalents.api.IDogBedMaterial;

/**
 * @author ProPercivalalb
 */
public class ForestryBedMaterial implements IDogBedMaterial {

	private Enum en;
	private static Icon[][] icons;
	
	public ForestryBedMaterial(Enum en) {
		this.en = en;
	}

	@Override
	public Icon getMaterialIcon(int side) {
		if(icons == null) {
			try {
				icons = (Icon[][])ForestryAPI.woodIconField.get().get(null);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return icons[0][en.ordinal()];
	}
}
