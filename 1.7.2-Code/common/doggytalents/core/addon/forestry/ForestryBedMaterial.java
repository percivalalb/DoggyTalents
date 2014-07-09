package doggytalents.core.addon.forestry;

import net.minecraft.util.IIcon;
import doggytalents.api.IDogBedMaterial;

/**
 * @author ProPercivalalb
 */
public class ForestryBedMaterial implements IDogBedMaterial {

	private Enum en;
	private static IIcon[][] icons;
	
	public ForestryBedMaterial(Enum en) {
		this.en = en;
	}

	@Override
	public IIcon getMaterialIcon(int side) {
		if(icons == null) {
			try {
				icons = (IIcon[][])ForestryAPI.woodIconField.get().get(null);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return icons[0][en.ordinal()];
	}
}
