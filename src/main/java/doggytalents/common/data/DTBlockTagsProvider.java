package doggytalents.common.data;

import doggytalents.common.lib.Constants;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class DTBlockTagsProvider extends BlockTagsProvider {

    public DTBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "DoggyTalents Block Tags";
    }

    @Override
    protected void addTags() {

    }
}
