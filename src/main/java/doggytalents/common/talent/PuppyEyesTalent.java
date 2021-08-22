package doggytalents.common.talent;

import java.util.List;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import doggytalents.common.util.EntityUtil;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;

public class PuppyEyesTalent extends TalentInstance {

    private int cooldown;

    public PuppyEyesTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
    }

    @Override
    public TalentInstance copy() {
        PuppyEyesTalent inst = new PuppyEyesTalent(this.getTalent(), this.level);
        inst.cooldown = this.cooldown;
        return inst;
    }

    @Override
    public void init(AbstractDogEntity dogIn) {
        this.cooldown = dogIn.tickCount;
    }

    @Override
    public void writeToNBT(AbstractDogEntity dogIn, CompoundTag compound) {
        super.writeToNBT(dogIn, compound);
        int timeLeft = this.cooldown - dogIn.tickCount;
        compound.putInt("cooldown", timeLeft);
    }

    @Override
    public void readFromNBT(AbstractDogEntity dogIn, CompoundTag compound) {
        super.readFromNBT(dogIn, compound);
        this.cooldown = dogIn.tickCount + compound.getInt("cooldown");
    }

    // Left in for backwards compatibility for versions <= 2.0.0.5
    @Override
    public void onRead(AbstractDogEntity dogIn, CompoundTag compound) {
        if (compound.contains("charmercharge")) {
            this.cooldown = dogIn.tickCount + compound.getInt("charmercharge");
        }
    }

    @Override
    public void livingTick(AbstractDogEntity dogIn) {
        if (dogIn.tickCount % 40 != 0) {
            return;
        }

        if (dogIn.level.isClientSide || !dogIn.isTame()) {
            return;
        }

        if (this.level() <= 0) {
            return;
        }
        int timeLeft = this.cooldown - dogIn.tickCount;

        if (timeLeft <= 0) {
            LivingEntity owner = dogIn.getOwner();

            // Dog doesn't have owner or is offline
            if (owner == null) {
                return;
            }

            LivingEntity villager = this.getClosestVisibleVillager(dogIn, 5D);

            if (villager != null) {
                int rewardId = dogIn.getRandom().nextInt(this.level()) + (this.level() >= 5 ? 1 : 0);

                if (rewardId == 0) {
                    owner.sendMessage(new TranslatableComponent("talent.doggytalents.puppy_eyes.msg.1.line.1", dogIn.getGenderPronoun()), villager.getUUID());
                    owner.sendMessage(new TranslatableComponent("talent.doggytalents.puppy_eyes.msg.1.line.2", dogIn.getGenderSubject()), villager.getUUID());
                    villager.spawnAtLocation(Items.PORKCHOP, 2);
                } else if (rewardId == 1) {
                    owner.sendMessage(new TranslatableComponent("talent.doggytalents.puppy_eyes.msg.2.line.1", dogIn.getGenderTitle()), villager.getUUID());
                    owner.sendMessage(new TranslatableComponent("talent.doggytalents.puppy_eyes.msg.2.line.2", dogIn.getGenderTitle()), villager.getUUID());
                    owner.sendMessage(new TranslatableComponent("talent.doggytalents.puppy_eyes.msg.2.line.3", dogIn.getGenderTitle()), villager.getUUID());
                    villager.spawnAtLocation(Items.PORKCHOP, 5);
                } else if (rewardId == 2) {
                    owner.sendMessage(new TranslatableComponent("talent.doggytalents.puppy_eyes.msg.3.line.1"), villager.getUUID());
                    owner.sendMessage(new TranslatableComponent("talent.doggytalents.puppy_eyes.msg.3.line.2"), villager.getUUID());
                    owner.sendMessage(new TranslatableComponent("talent.doggytalents.puppy_eyes.msg.3.line.3"), villager.getUUID());
                    villager.spawnAtLocation(Items.IRON_INGOT, 3);
                } else if (rewardId == 3) {
                    owner.sendMessage(new TranslatableComponent("talent.doggytalents.puppy_eyes.msg.4.line.1"), villager.getUUID());
                    owner.sendMessage(new TranslatableComponent("talent.doggytalents.puppy_eyes.msg.4.line.2"), villager.getUUID());
                    owner.sendMessage(new TranslatableComponent("talent.doggytalents.puppy_eyes.msg.4.line.3"), villager.getUUID());
                    villager.spawnAtLocation(Items.GOLD_INGOT, 2);
                } else if (rewardId == 4) {
                    owner.sendMessage(new TranslatableComponent("talent.doggytalents.puppy_eyes.msg.5.line.1"), villager.getUUID());
                    owner.sendMessage(new TranslatableComponent("talent.doggytalents.puppy_eyes.msg.5.line.2"), villager.getUUID());
                    owner.sendMessage(new TranslatableComponent("talent.doggytalents.puppy_eyes.msg.5.line.3"), villager.getUUID());
                    villager.spawnAtLocation(Items.DIAMOND, 1);
                } else if (rewardId == 5) {
                    owner.sendMessage(new TranslatableComponent("talent.doggytalents.puppy_eyes.msg.6.line.1"), villager.getUUID());
                    owner.sendMessage(new TranslatableComponent("talent.doggytalents.puppy_eyes.msg.6.line.2"), villager.getUUID());
                    owner.sendMessage(new TranslatableComponent("talent.doggytalents.puppy_eyes.msg.6.line.3"), villager.getUUID());
                    villager.spawnAtLocation(Items.APPLE, 1);
                    villager.spawnAtLocation(Blocks.CAKE, 1);
                    villager.spawnAtLocation(Items.SLIME_BALL, 3);
                    villager.spawnAtLocation(Items.PORKCHOP, 5);
                }

                this.cooldown = dogIn.tickCount + (this.level() >= 5 ? 24000 : 48000);
            }
        }
    }

    public LivingEntity getClosestVisibleVillager(AbstractDogEntity dogIn, double radiusIn) {
        List<AbstractVillager> list = dogIn.level.getEntitiesOfClass(
            AbstractVillager.class,
            dogIn.getBoundingBox().inflate(radiusIn, radiusIn, radiusIn),
            (village) -> village.canSee(dogIn)
        );

        return EntityUtil.getClosestTo(dogIn, list);
    }
}
