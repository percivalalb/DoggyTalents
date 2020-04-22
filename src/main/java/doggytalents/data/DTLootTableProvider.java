package doggytalents.data;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import doggytalents.ModBlocks;
import doggytalents.ModEntities;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.ConstantRange;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.LootParameterSet;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTable.Builder;
import net.minecraft.world.storage.loot.ValidationTracker;
import net.minecraft.world.storage.loot.functions.CopyNbt;

public class DTLootTableProvider extends LootTableProvider {

    public DTLootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    public String getName() {
        return "DoggyTalents LootTables";
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootParameterSet>> getTables() {
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
            dropsSelf(ModBlocks.DOG_BATH.delegate);
            dropDogBed(ModBlocks.DOG_BED.delegate);
            dropsSelf(ModBlocks.FOOD_BOWL.delegate); // Drop with the name of the dog bowl
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
                                 ))));

            this.registerLootTable(block.get(), lootTableBuilder);
        }

        private void dropsSelf(Supplier<? extends Block> block) {
            this.registerDropSelfLootTable(block.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            // return DoggyBlocks.BLOCKS.getEntries().stream().map(Supplier::get).collect(Collectors.toList());
            return Arrays.asList(ModBlocks.DOG_BATH, ModBlocks.DOG_BED, ModBlocks.FOOD_BOWL);
        }
    }

    private static class Entities extends EntityLootTables {

        @Override
        protected void addTables() {
            this.registerNoLoot(ModEntities.DOG.delegate);
        }


        protected void registerNoLoot(Supplier<? extends EntityType<?>> type) {
           this.registerLootTable(type.get(), LootTable.builder());
        }

        @Override
        protected Iterable<EntityType<?>> getKnownEntities() {
            //return DoggyEntityTypes.ENTITIES.getEntries().stream().map(Supplier::get).collect(Collectors.toList());
            return Arrays.asList(ModEntities.DOG);
        }
    }
}