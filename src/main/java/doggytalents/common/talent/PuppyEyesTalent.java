package doggytalents.common.talent;

import java.util.Collections;
import java.util.List;

import doggytalents.api.feature.DataKey;
import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import doggytalents.common.util.EntityUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TranslationTextComponent;

public class PuppyEyesTalent extends Talent {

    private static DataKey<Integer> COOLDOWN = DataKey.make();

    @Override
    public void init(AbstractDogEntity dogIn) {
        dogIn.setDataIfEmpty(COOLDOWN, dogIn.ticksExisted);
    }

    @Override
    public void write(AbstractDogEntity dogIn, CompoundNBT compound) {
        int timeLeft = dogIn.getDataOrDefault(COOLDOWN, dogIn.ticksExisted) - dogIn.ticksExisted;
        compound.putInt("charmercharge", timeLeft);
    }

    @Override
    public void read(AbstractDogEntity dogIn, CompoundNBT compound) {
        dogIn.setData(COOLDOWN, dogIn.ticksExisted + compound.getInt("charmercharge"));
    }

    @Override
    public void livingTick(AbstractDogEntity dogIn) {
        if (dogIn.ticksExisted % 40 != 0) {
            return;
        }

        if (dogIn.world.isRemote || !dogIn.isTamed()) {
            return;
        }
        int level = dogIn.getLevel(this);

        if (level <= 0) {
            return;
        }
        int timeLeft = dogIn.getDataOrDefault(COOLDOWN, dogIn.ticksExisted) - dogIn.ticksExisted;

        if (timeLeft <= 0) {
            LivingEntity owner = dogIn.getOwner();

            // Dog doesn't have owner or is offline
            if (owner == null) {
                return;
            }

            LivingEntity villager = this.getClosestVisibleVillager(dogIn, 5D);

            if (villager != null) {
                int rewardId = dogIn.getRNG().nextInt(level) + (level >= 5 ? 1 : 0);

                if (rewardId == 0) {
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.1.line.1", dogIn.getGenderPronoun()));
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.1.line.2", dogIn.getGenderSubject()));
                    villager.entityDropItem(Items.PORKCHOP, 2);
                } else if (rewardId == 1) {
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.2.line.1", dogIn.getGenderTitle()));
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.2.line.2", dogIn.getGenderTitle()));
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.2.line.3", dogIn.getGenderTitle()));
                    villager.entityDropItem(Items.PORKCHOP, 5);
                } else if (rewardId == 2) {
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.3.line.1"));
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.3.line.2"));
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.3.line.3"));
                    villager.entityDropItem(Items.IRON_INGOT, 3);
                } else if (rewardId == 3) {
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.4.line.1"));
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.4.line.2"));
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.4.line.3"));
                    villager.entityDropItem(Items.GOLD_INGOT, 2);
                } else if (rewardId == 4) {
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.5.line.1"));
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.5.line.2"));
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.5.line.3"));
                    villager.entityDropItem(Items.DIAMOND, 1);
                } else if (rewardId == 5) {
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.6.line.1"));
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.6.line.2"));
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.6.line.3"));
                    villager.entityDropItem(Items.APPLE, 1);
                    villager.entityDropItem(Blocks.CAKE, 1);
                    villager.entityDropItem(Items.SLIME_BALL, 3);
                    villager.entityDropItem(Items.PORKCHOP, 5);
                }

                dogIn.setData(COOLDOWN, dogIn.ticksExisted + (level >= 5 ? 24000 : 48000));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public LivingEntity getClosestVisibleVillager(AbstractDogEntity dogIn, double radiusIn) {
        List<AbstractVillagerEntity> list = dogIn.world.getEntitiesWithinAABB(AbstractVillagerEntity.class, dogIn.getBoundingBox().grow(radiusIn, radiusIn, radiusIn), village -> village.canEntityBeSeen(dogIn));
        Collections.sort(list, new EntityUtil.Sorter(dogIn));

        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }
}
