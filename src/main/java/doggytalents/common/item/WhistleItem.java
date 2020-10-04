package doggytalents.common.item;

import java.util.List;
import java.util.stream.Collectors;

import doggytalents.DoggySounds;
import doggytalents.DoggyTalents;
import doggytalents.api.feature.EnumMode;
import doggytalents.common.config.ConfigValues;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.entity.DoggyBeamEntity;
import doggytalents.common.talent.RoaringGaleTalent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class WhistleItem extends Item {

    public WhistleItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (player.isSneaking()) {
            if (!world.isRemote) {
                if (!stack.hasTag()) {
                    stack.setTag(new CompoundNBT());
                    stack.getTag().putByte("mode", (byte)0);
                }

                int mode = stack.getTag().getInt("mode");
                stack.getTag().putInt("mode", (mode + 1) % 7);
            }

            return new ActionResult<ItemStack>(ActionResultType.SUCCESS, stack);
        }
        else {
            byte mode = 0;

            if (stack.hasTag() && stack.getTag().contains("mode", Constants.NBT.TAG_ANY_NUMERIC)) {
                mode = stack.getTag().getByte("mode");
            }

            List<DogEntity> dogsList = world.getEntitiesWithinAABB(DogEntity.class, player.getBoundingBox().grow(100D, 50D, 100D), dog -> dog.isOwner(player));
            boolean successful = false;

            if (mode == 0) { // Stand
                if (!world.isRemote) {
                    for (DogEntity dog : dogsList) {
                        dog.func_233687_w_(false);
                        dog.getNavigator().clearPath();
                        dog.setAttackTarget(null);
                        if (dog.getMode() == EnumMode.WANDERING) {
                            dog.setMode(EnumMode.DOCILE);
                        }
                        successful = true;
                    }

                    if (ConfigValues.WHISTLE_SOUNDS)
                    world.playSound(null, player.getPosition(), DoggySounds.WHISTLE_LONG.get(), SoundCategory.PLAYERS, 0.6F + world.rand.nextFloat() * 0.1F, 0.8F + world.rand.nextFloat() * 0.2F);

                    if (successful) {
                        player.sendMessage(new TranslationTextComponent("dogcommand.come"), Util.DUMMY_UUID);
                    }
                }

                return new ActionResult<ItemStack>(ActionResultType.SUCCESS, stack);
            }
            else if (mode == 1) { // Heel
                if (!world.isRemote) {
                    for (DogEntity dog : dogsList) {
                        if (!dog.isSitting() && dog.getMode() != EnumMode.WANDERING) {
                            //TODO DogUtil.teleportDogToOwner(player, dog, world, dog.getNavigator());
                            successful = true;
                        }
                    }

                    if (ConfigValues.WHISTLE_SOUNDS)
                    world.playSound(null, player.getPosition(), DoggySounds.WHISTLE_LONG.get(), SoundCategory.PLAYERS, 0.6F + world.rand.nextFloat() * 0.1F, 0.8F + world.rand.nextFloat() * 0.2F);

                    if (successful) {
                        player.sendMessage(new TranslationTextComponent("dogcommand.heel"), Util.DUMMY_UUID);
                    }
                }

                return new ActionResult<ItemStack>(ActionResultType.SUCCESS, stack);
            }
            else if (mode == 2) { // Stay
                if (!world.isRemote) {
                    for (DogEntity dog : dogsList) {
                        dog.func_233687_w_(true);
                        dog.getNavigator().clearPath();
                        dog.setAttackTarget(null);
                        if (dog.getMode() == EnumMode.WANDERING) {
                            dog.setMode(EnumMode.DOCILE);
                        }
                        successful = true;
                    }

                    if (ConfigValues.WHISTLE_SOUNDS)
                    world.playSound(null, player.getPosition(), DoggySounds.WHISTLE_SHORT.get(), SoundCategory.PLAYERS, 0.6F + world.rand.nextFloat() * 0.1F, 0.8F + world.rand.nextFloat() * 0.2F);

                    if (successful) {
                        player.sendMessage(new TranslationTextComponent("dogcommand.stay"), Util.DUMMY_UUID);
                    }
                }

                return new ActionResult<ItemStack>(ActionResultType.SUCCESS, stack);
            }
            else if (mode == 3) { // Ok
                if (!world.isRemote) {
                    for (DogEntity dog : dogsList) {
                        if (dog.getMaxHealth() / 2 >= dog.getHealth()) {
                            dog.func_233687_w_(true);
                            dog.getNavigator().clearPath();
                            dog.setAttackTarget(null);
                        }
                        else {
                            dog.func_233687_w_(false);
                            dog.getNavigator().clearPath();
                            dog.setAttackTarget(null);
                        }
                        successful = true;
                    }

                    if (ConfigValues.WHISTLE_SOUNDS)
                    world.playSound(null, player.getPosition(), DoggySounds.WHISTLE_LONG.get(), SoundCategory.PLAYERS, 0.6F + world.rand.nextFloat() * 0.1F, 0.4F + world.rand.nextFloat() * 0.2F);

                    if (successful) {
                        player.sendMessage(new TranslationTextComponent("dogcommand.ok"), Util.DUMMY_UUID);
                    }

                    return new ActionResult<ItemStack>(ActionResultType.SUCCESS, stack);
                }
            }
            else if (mode == 4) {
                if (!world.isRemote) {
                    if (ConfigValues.WHISTLE_SOUNDS)
                    world.playSound(null, player.getPosition(), DoggySounds.WHISTLE_SHORT.get(), SoundCategory.PLAYERS, 0.6F + world.rand.nextFloat() * 0.1F, 0.8F + world.rand.nextFloat() * 0.2F);

                    //player.sendMessage(new TranslationTextComponent("dogcommand.shepherd"));
                }

                return new ActionResult<ItemStack>(ActionResultType.SUCCESS, stack);
            } else if (mode == 5) {
                if (!world.isRemote) {
                    if (ConfigValues.WHISTLE_SOUNDS)
                    world.playSound((PlayerEntity)null, player.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

                    DoggyBeamEntity doggyBeam = new DoggyBeamEntity(world, player);
                    doggyBeam.shoot(player.rotationPitch, player.rotationYaw, 0.0F, 2.0F, 1.0F);
                    world.addEntity(doggyBeam);
                }

                return new ActionResult<>(ActionResultType.CONSUME, player.getHeldItem(hand));
            } else if (mode == 6) {
                if (!world.isRemote) {
                    List<DogEntity> roarDogs = dogsList.stream().filter(dog -> dog.getLevel(DoggyTalents.ROARING_GALE) > 0).collect(Collectors.toList());
                    if (roarDogs.isEmpty()) {
                        player.sendStatusMessage(new TranslationTextComponent("talent.doggytalents.roaring_gale.level"), true);
                    } else {
                        List<DogEntity> cdDogs = roarDogs.stream().filter(dog -> dog.getDataOrDefault(RoaringGaleTalent.COOLDOWN, 0) == 0).collect(Collectors.toList());
                        if (cdDogs.isEmpty()) {
                            player.sendStatusMessage(new TranslationTextComponent("talent.doggytalents.roaring_gale.cooldown"), true);
                        } else {
                            for (DogEntity dog : dogsList) {
                                int level = dog.getLevel(DoggyTalents.ROARING_GALE);
                                int roarCooldown = dog.getDataOrDefault(RoaringGaleTalent.COOLDOWN, 0);

                                roarCooldown = level == 5 ? 60 : 100;

                                byte damage = (byte)(level > 4 ? level * 2 : level);

                                /*
                                 * If level = 1, set duration to  20 ticks (1 second); level = 2, set duration to 24 ticks (1.2 seconds)
                                 * If level = 3, set duration to 36 ticks (1.8 seconds); If level = 4, set duration to 48 ticks (2.4 seconds)
                                 * If level = max (5), set duration to 70 ticks (3.5 seconds);
                                 */
                                byte effectDuration = (byte)(level > 4 ? level * 14 : level * (level == 1 ? 20 : 12));
                                byte knockback = (byte)level;

                                boolean hit = false;
                                List<LivingEntity> list = dog.world.<LivingEntity>getEntitiesWithinAABB(LivingEntity.class, dog.getBoundingBox().grow(level * 4, 4D, level * 4));
                                for (LivingEntity mob : list) {
                                    if (mob instanceof IMob) {
                                        hit = true;
                                        mob.attackEntityFrom(DamageSource.GENERIC, damage);
                                        mob.addPotionEffect(new EffectInstance(Effects.SLOWNESS, effectDuration, 127, false, false));
                                        mob.addPotionEffect(new EffectInstance(Effects.GLOWING, effectDuration, 1, false, false));
                                        mob.addVelocity(MathHelper.sin(mob.rotationYaw * (float) Math.PI / 180.0F) * knockback * 0.5F, 0.1D, -MathHelper.cos(mob.rotationYaw * (float) Math.PI / 180.0F) * knockback * 0.5F);
                                    }
                                }

                                if (hit) {
                                    dog.playSound(SoundEvents.ENTITY_WOLF_GROWL, 0.7F, 1.0F);
                                } else {
                                    dog.playSound(SoundEvents.ENTITY_WOLF_AMBIENT, 1F, 1.2F);
                                    roarCooldown /= 2;
                                }

                                dog.setData(RoaringGaleTalent.COOLDOWN, roarCooldown);
                            }
                        }
                    }
                }

                return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
            }

            //world.playSound(null, player.getPosition(), DoggySounds.WHISTLE_LONG, SoundCategory.PLAYERS, 0.8F, 0.8F + world.rand.nextFloat() * 0.2F);
            //world.playSound(null, player.getPosition(), DoggySounds.WHISTLE_SHORT, SoundCategory.PLAYERS, 0.8F, 0.6F + world.rand.nextFloat() * 0.2F);
        }

        return new ActionResult<>(ActionResultType.FAIL, player.getHeldItem(hand));
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        byte mode = 0;

        if (stack.hasTag() && stack.getTag().contains("mode", Constants.NBT.TAG_ANY_NUMERIC)) {
            mode = stack.getTag().getByte("mode");
        }
        return this.getTranslationKey() + "." + mode;

    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return Rarity.UNCOMMON;
    }
}
