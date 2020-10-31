package doggytalents.common.data;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import doggytalents.DoggyEntityTypes;
import doggytalents.DoggyItems;
import doggytalents.common.util.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.TameAnimalTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.AdvancementProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

public class DTAdvancementProvider extends AdvancementProvider {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private final DataGenerator generator;

    public DTAdvancementProvider(DataGenerator generatorIn) {
        super(generatorIn);
        this.generator = generatorIn;
    }

    @Override
    public String getName() {
        return "DoggyTalents Advancements";
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        Path path = this.generator.getOutputFolder();
        Set<ResourceLocation> set = Sets.newHashSet();
        Consumer<Advancement> consumer = (p_204017_3_) -> {
            if (!set.add(p_204017_3_.getId())) {
                throw new IllegalStateException("Duplicate advancement " + p_204017_3_.getId());
            } else {
                Path path1 = getPath(path, p_204017_3_);

                try {
                    IDataProvider.save(GSON, cache, p_204017_3_.copy().serialize(), path1);
                } catch (IOException ioexception) {
                    LOGGER.error("Couldn't save advancement {}", path1, ioexception);
                }
            }
        };

        // Disable advancements for now
        if (true) return;

        Advancement advancement = Advancement.Builder.builder()
                .withDisplay(DisplayInfoBuilder.create().icon(DoggyItems.TRAINING_TREAT).frame(FrameType.TASK).translate("dog.root").background("stone.png").noToast().noAnnouncement().build())
                .withCriterion("tame_dog", TameAnimalTrigger.Instance.create(EntityPredicate.Builder.create().type(DoggyEntityTypes.DOG.get()).build()))
                //.withCriterion("get_dog", ItemUseTrigger.TameAnimalTrigger.Instance.create(EntityPredicate.Builder.create().type(DoggyEntityTypes.DOG.get()).build()))
                .withRequirementsStrategy(IRequirementsStrategy.OR)
                .register(consumer, Util.getResourcePath("default/tame_dog"));

        Advancement advancement1 = Advancement.Builder.builder()
                .withParent(advancement)
                .withDisplay(DisplayInfoBuilder.create().icon(Items.WOODEN_PICKAXE).frame(FrameType.TASK).translate("dog.level_talent").build())
                .withCriterion("level_talent", InventoryChangeTrigger.Instance.forItems(Blocks.COBBLESTONE))
                .register(consumer, Util.getResourcePath("default/level_talent"));
        Advancement advancement2 = Advancement.Builder.builder()
                .withParent(advancement1)
                .withDisplay(DisplayInfoBuilder.create().icon(DoggyItems.CAPE).frame(FrameType.TASK).translate("dog.accessorise").build())
                .withCriterion("accessorise", InventoryChangeTrigger.Instance.forItems(Items.STONE_PICKAXE))
                .register(consumer, Util.getResourcePath("default/accessorise"));

        Advancement advancement3 = Advancement.Builder.builder()
                .withParent(advancement2)
                .withDisplay(DisplayInfoBuilder.create().icon(DoggyItems.RADIO_COLLAR).frame(FrameType.TASK).translate("dog.radio_collar").build())
                .withCriterion("iron", InventoryChangeTrigger.Instance.forItems(Items.IRON_INGOT))
                .register(consumer, Util.getResourcePath("default/radio_collar"));
    }

    private static Path getPath(Path pathIn, Advancement advancementIn) {
        return pathIn.resolve("data/" + advancementIn.getId().getNamespace() + "/advancements/" + advancementIn.getId().getPath() + ".json");
    }
}
