package doggytalents.common.data;

import doggytalents.common.lib.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DTBlockTagsProvider extends BlockTagsProvider {

    public DTBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, Constants.MOD_ID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")

    @Override
    public String getName() {
        return "DoggyTalents Block Tags";
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {}
}
