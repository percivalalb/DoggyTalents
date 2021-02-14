package doggytalents.common.data;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import doggytalents.DoggyBlocks;
import doggytalents.DoggyEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.util.ResourceLocation;

public class DTLootTableProvider extends LootTableProvider {

    public DTLootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    public String getName() {
        return "DoggyTalents LootTables";
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return ImmutableList.of(
                Pair.of(Blocks::new, LootParameterSets.BLOCK),
                Pair.of(Entities::new, LootParameterSets.ENTITY)
               );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationTracker) {}

    private static class Blocks extends BlockLootTables {

        @Override
        protected void addTables() {
            dropsSelf(DoggyBlocks.DOG_BATH);
            dropDogBed(DoggyBlocks.DOG_BED);
            dropsSelf(DoggyBlocks.FOOD_BOWL); // Drop with the name of the dog bowl
        }

        private void dropDogBed(Supplier<? extends Block> block) {
            LootTable.Builder lootTableBuilder = LootTable.builder().addLootPool(withSurvivesExplosion(block.get(),
                       LootPool.builder()
                         .rolls(ConstantRange.of(1))
                         .addEntry(ItemLootEntry.builder(block.get())
                                 .acceptFunction(
                                         CopyNbt.builder(CopyNbt.Source.BLOCK_ENTITY)
                                         .replaceOperation("casingId", "doggytalents.casingId")
                                         .replaceOperation("beddingId", "doggytalents.beddingId")
                                         .replaceOperation("ownerId", "doggytalents.ownerId")
                                         .replaceOperation("name", "doggytalents.name")
                                         .replaceOperation("ownerName", "doggytalents.ownerName")
                                 ))));

            this.registerLootTable(block.get(), lootTableBuilder);
        }

        private void dropsSelf(Supplier<? extends Block> block) {
            this.registerDropSelfLootTable(block.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return DoggyBlocks.BLOCKS.getEntries().stream().map(Supplier::get).collect(Collectors.toList());
        }
    }

    private static class Entities extends EntityLootTables {

        @Override
        protected void addTables() {
            this.registerNoLoot(DoggyEntityTypes.DOG);
        }

        protected void registerNoLoot(Supplier<? extends EntityType<?>> type) {
           this.registerLootTable(type.get(), LootTable.builder());
        }

        @Override
        protected Iterable<EntityType<?>> getKnownEntities() {
            return DoggyEntityTypes.ENTITIES.getEntries().stream().map(Supplier::get).collect(Collectors.toList());
        }
    }
}
