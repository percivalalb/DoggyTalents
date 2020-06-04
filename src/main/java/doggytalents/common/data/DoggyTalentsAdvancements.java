package doggytalents.common.data;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import doggytalents.DoggyItems;
import doggytalents.common.util.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class DoggyTalentsAdvancements implements Consumer<Consumer<Advancement>> {

    @Override
    public void accept(Consumer<Advancement> register) {
        Advancement advancement = Advancement.Builder.builder().withDisplay(DoggyItems.TRAINING_TREAT.get(), new TranslationTextComponent("advancements.dog.root.title"), new TranslationTextComponent("advancements.dog.root.description"), new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"), FrameType.TASK, false, false, false).withCriterion("crafting_table", InventoryChangeTrigger.Instance.forItems(Blocks.CRAFTING_TABLE)).register(register, Util.getResourcePath("dog/find_dog"));
        Advancement advancement1 = Advancement.Builder.builder().withParent(advancement).withDisplay(Items.WOODEN_PICKAXE, new TranslationTextComponent("advancements.dog.mine_stone.title"), new TranslationTextComponent("advancements.dog.mine_stone.description"), (ResourceLocation)null, FrameType.TASK, true, true, false).withCriterion("get_stone", InventoryChangeTrigger.Instance.forItems(Blocks.COBBLESTONE)).register(register, Util.getResourcePath("dog/level_talent"));
        Advancement advancement2 = Advancement.Builder.builder().withParent(advancement1).withDisplay(DoggyItems.CAPE.get(), new TranslationTextComponent("advancements.dog.upgrade_tools.title"), new TranslationTextComponent("advancements.dog.upgrade_tools.description"), (ResourceLocation)null, FrameType.TASK, true, true, false).withCriterion("stone_pickaxe", InventoryChangeTrigger.Instance.forItems(Items.STONE_PICKAXE)).register(register, Util.getResourcePath("dog/accessorise"));
        Advancement advancement3 = Advancement.Builder.builder().withParent(advancement2).withDisplay(DoggyItems.RADIO_COLLAR.get(), new TranslationTextComponent("advancements.dog.smelt_iron.title"), new TranslationTextComponent("advancements.dog.smelt_iron.description"), (ResourceLocation)null, FrameType.TASK, true, true, false).withCriterion("iron", InventoryChangeTrigger.Instance.forItems(Items.IRON_INGOT)).register(register, Util.getResourcePath("dog/radio_collar"));
    }

    public static class Builder {
        private ResourceLocation parentId;
        private Advancement parent;
        private DisplayInfo display;
        private AdvancementRewards rewards = AdvancementRewards.EMPTY;
        private Map<String, Criterion> criteria = Maps.newLinkedHashMap();
        private String[][] requirements;
        private IRequirementsStrategy requirementsStrategy = IRequirementsStrategy.AND;

        private Builder(@Nullable ResourceLocation parentIdIn, @Nullable DisplayInfo displayIn, AdvancementRewards rewardsIn, Map<String, Criterion> criteriaIn, String[][] requirementsIn) {
           this.parentId = parentIdIn;
           this.display = displayIn;
           this.rewards = rewardsIn;
           this.criteria = criteriaIn;
           this.requirements = requirementsIn;
        }

        private Builder() {
        }

        public static DoggyTalentsAdvancements.Builder builder() {
           return new DoggyTalentsAdvancements.Builder();
        }

        public DoggyTalentsAdvancements.Builder withParent(Advancement parentIn) {
           this.parent = parentIn;
           return this;
        }

        public DoggyTalentsAdvancements.Builder withParentId(ResourceLocation parentIdIn) {
           this.parentId = parentIdIn;
           return this;
        }

        public DoggyTalentsAdvancements.Builder withDisplay(ItemStack stack, ITextComponent title, ITextComponent description, @Nullable ResourceLocation background, FrameType frame, boolean showToast, boolean announceToChat, boolean hidden) {
           return this.withDisplay(new DisplayInfo(stack, title, description, background, frame, showToast, announceToChat, hidden));
        }

        public DoggyTalentsAdvancements.Builder withDisplay(IItemProvider itemIn, ITextComponent title, ITextComponent description, @Nullable ResourceLocation background, FrameType frame, boolean showToast, boolean announceToChat, boolean hidden) {
           return this.withDisplay(new DisplayInfo(new ItemStack(itemIn.asItem()), title, description, background, frame, showToast, announceToChat, hidden));
        }

        public DoggyTalentsAdvancements.Builder withDisplay(DisplayInfo displayIn) {
           this.display = displayIn;
           return this;
        }

        public DoggyTalentsAdvancements.Builder withRewards(AdvancementRewards.Builder rewardsBuilder) {
           return this.withRewards(rewardsBuilder.build());
        }

        public DoggyTalentsAdvancements.Builder withRewards(AdvancementRewards p_200274_1_) {
           this.rewards = p_200274_1_;
           return this;
        }

        /**
         * Adds a criterion to the list of criteria
         */
        public DoggyTalentsAdvancements.Builder withCriterion(String key, ICriterionInstance criterionIn) {
           return this.withCriterion(key, new Criterion(criterionIn));
        }

        /**
         * Adds a criterion to the list of criteria
         */
        public DoggyTalentsAdvancements.Builder withCriterion(String key, Criterion criterionIn) {
           if (this.criteria.containsKey(key)) {
              throw new IllegalArgumentException("Duplicate criterion " + key);
           } else {
              this.criteria.put(key, criterionIn);
              return this;
           }
        }

        public DoggyTalentsAdvancements.Builder withRequirementsStrategy(IRequirementsStrategy strategy) {
           this.requirementsStrategy = strategy;
           return this;
        }

        /**
         * Tries to resolve the parent of this advancement, if possible. Returns true on success.
         */
        public boolean resolveParent(Function<ResourceLocation, Advancement> lookup) {
           if (this.parentId == null) {
              return true;
           } else {
              if (this.parent == null) {
                 this.parent = lookup.apply(this.parentId);
              }

              return this.parent != null;
           }
        }

        public Advancement build(ResourceLocation id) {
           if (!this.resolveParent((p_199750_0_) -> {
              return null;
           })) {
              throw new IllegalStateException("Tried to build incomplete advancement!");
           } else {
              if (this.requirements == null) {
                 this.requirements = this.requirementsStrategy.createRequirements(this.criteria.keySet());
              }

              return new Advancement(id, this.parent, this.display, this.rewards, this.criteria, this.requirements);
           }
        }

        public Advancement register(Consumer<Advancement> consumer, String id) {
            Advancement advancement = this.build(new ResourceLocation(id));
            consumer.accept(advancement);
            return advancement;
         }
    }
}