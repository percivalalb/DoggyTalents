package doggytalents.core.addon.forestry;

import java.lang.reflect.Field;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.google.common.base.Optional;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import doggytalents.core.helper.ReflectionHelper;

/**
 * @author ProPercivalalb
 */
public class ForestryAPI {

	private Optional<Class>  enumWoodTypeClass = Optional.absent();
	private Optional<Class>  forestryBlocksClass = Optional.absent();
	public static Optional<Field>  woodIconField = Optional.absent();
	public static Optional<Field>  planks1Field = Optional.absent();
	public static Optional<Field>  planks2Field = Optional.absent();
	
	private ArrayList<Enum> enumsTypes = new ArrayList<Enum>();
	private Block planks1;
	private Block planks2;
	
	public ForestryAPI(String modId) {
		if(!Loader.isModLoaded(modId))
			return;
		enumWoodTypeClass = Optional.fromNullable(ReflectionHelper.getClass(ForestryLib.ENUM_WOOD_TYPE));
		forestryBlocksClass = Optional.fromNullable(ReflectionHelper.getClass(ForestryLib.CLASS_FORESTRY_BLOCKS));
		
		if(enumWoodTypeClass.isPresent()) {
			for(Field fld : enumWoodTypeClass.get().getFields()) {
				if(fld.isEnumConstant()) {
					try {
						if((Boolean)enumWoodTypeClass.get().getField("hasPlank").get((Enum)fld.get(null)))
							enumsTypes.add((Enum)fld.get(null));
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
	 		}
			if(FMLCommonHandler.instance().getSide() == Side.CLIENT) 
				woodIconField = Optional.fromNullable(ReflectionHelper.getField(enumWoodTypeClass.get(), null, ForestryLib.FIELD_ICONS));
		}
		
		if(forestryBlocksClass.isPresent()) {
			planks1Field = Optional.fromNullable(ReflectionHelper.getField(forestryBlocksClass.get(), null, ForestryLib.FIELD_PLANKS_1));
			planks2Field = Optional.fromNullable(ReflectionHelper.getField(forestryBlocksClass.get(), null, ForestryLib.FIELD_PLANKS_2));
			try {
				if(planks1Field.isPresent())
					planks1 = (Block)planks1Field.get().get(null);
				if(planks2Field.isPresent())
					planks2 = (Block)planks2Field.get().get(null);	
			}
			catch(Exception e) {
				e.printStackTrace();
			} 
		}
	}
	
	public ArrayList<Enum> getEnums() {
		return this.enumsTypes;
	}
	
	public Block getPlank1() {
		return this.planks1;
	}
	
	public Block getPlank2() {
		return this.planks2;
	}
	
	public ItemStack getStackFromEnum(Enum en) {
		ItemStack stack = null;
		if(en.ordinal() < 16) {
			stack = new ItemStack(this.getPlank1(), 1, en.ordinal() & 15);
		}
		else {
			stack = new ItemStack(this.getPlank2(), 1, en.ordinal() & 15);
		}
		return stack;
	}
}
