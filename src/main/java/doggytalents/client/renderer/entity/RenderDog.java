package doggytalents.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import doggytalents.ModTalents;
import doggytalents.client.model.entity.ModelChest;
import doggytalents.client.model.entity.ModelDog;
import doggytalents.client.model.entity.ModelSaddle;
import doggytalents.client.model.entity.ModelWings;
import doggytalents.client.renderer.entity.layer.LayerBone;
import doggytalents.client.renderer.entity.layer.LayerCape;
import doggytalents.client.renderer.entity.layer.LayerCover;
import doggytalents.client.renderer.entity.layer.LayerDogCollar;
import doggytalents.client.renderer.entity.layer.LayerDogHurt;
import doggytalents.client.renderer.entity.layer.LayerModel;
import doggytalents.client.renderer.entity.layer.LayerRadioCollar;
import doggytalents.entity.EntityDog;
import doggytalents.lib.ConfigValues;
import doggytalents.lib.ResourceLib;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

/**
 * @author ProPercivalalb
 */
public class RenderDog extends MobRenderer<EntityDog, ModelDog> {

    public RenderDog(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ModelDog(0.0F), 0.5F);
        this.addLayer(new LayerCape(this));
        this.addLayer(new LayerRadioCollar(this));
        this.addLayer(new LayerDogCollar(this));
        this.addLayer(new LayerDogHurt(this));
        this.addLayer(new LayerBone(this));

        this.addLayer(new LayerCover(this, new ModelDog(0.4F), ResourceLib.MOB_LAYER_SUNGLASSES, EntityDog::hasSunglasses));

        this.addLayer(new LayerModel(this, new ModelDog(0.4F), ResourceLib.MOB_LAYER_ARMOR, dog -> ConfigValues.RENDER_ARMOUR && dog.TALENTS.getLevel(ModTalents.GUARD_DOG) > 0));
        this.addLayer(new LayerModel(this, new ModelWings(), ResourceLib.MOB_LAYER_WINGS, dog -> ConfigValues.RENDER_WINGS && dog.TALENTS.getLevel(ModTalents.PILLOW_PAW) == 5));
        this.addLayer(new LayerModel(this, new ModelSaddle(0.0F), ResourceLib.MOB_LAYER_SADDLE, dog -> ConfigValues.RENDER_SADDLE && dog.TALENTS.getLevel(ModTalents.WOLF_MOUNT) > 0));
        this.addLayer(new LayerModel(this, new ModelChest(0.0F), ResourceLib.MOB_LAYER_CHEST, dog -> ConfigValues.RENDER_CHEST && dog.TALENTS.getLevel(ModTalents.PACK_PUPPY) > 0));
    }

    @Override
    protected float handleRotationFloat(EntityDog dog, float partialTickTime) {
        return dog.getTailRotation();
    }

    //TODO @Override
    //protected void renderLivingAt(EntityDog dog, double x, double y, double z) {
    //    if(dog.isAlive() && dog.isSleeping())
    //        super.renderLivingAt(dog, x, y + 0.5F, z);
    //    else
    //        super.renderLivingAt(dog, x, y, z);
    //}

    @Override
    public void func_225623_a_(EntityDog dogIn, float entityYaw, float partialTicks, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) { // 1.14 doRender
        if (dogIn.isDogWet()) {
           float f = dogIn.getBrightness() * dogIn.getShadingWhileWet(partialTicks);
           this.entityModel.func_228253_a_(f, f, f);
        }

        super.func_225623_a_(dogIn, entityYaw, partialTicks, p_225623_4_, p_225623_5_, p_225623_6_);
        if (dogIn.isDogWet()) {
           this.entityModel.func_228253_a_(1.0F, 1.0F, 1.0F);
        }

     }

    @Override
    public ResourceLocation getEntityTexture(EntityDog dog) {
        if(ConfigValues.USE_DT_TEXTURES)
             return ResourceLib.getTameSkin(dog.getTameSkin());
         else
             return ResourceLib.MOB_DOG_TAME;
    }

    // TODO
    /**
    @Override
    public void renderName(EntityDog dog, double x, double y, double z) {
        if(this.canRenderName(dog)) {
            RenderSystem.alphaFunc(516, 0.1F);
            double d0 = dog.getDistanceSq(this.renderManager.info.getProjectedView());

            y += this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15F * 0.016666668F * 0.7F;

            String tip = dog.MODE.getMode().getTip();

            if(dog.isIncapacicated())
                tip = "dog.mode.incapacitated.indicator";

            String label = String.format("%s(%d)",
                    new TranslationTextComponent(tip).getFormattedText(),
                    dog.getDogHunger());
            if(ConfigValues.DOG_GENDER) {
                label += dog.GENDER.getGenderTip().getFormattedText();
            }
            if(d0 <= 64 * 64) {
                boolean flag = dog.addedToChunk.isCrouching();
                float f = this.renderManager.playerViewY;
                float f1 = this.renderManager.playerViewX;
                boolean flag1 = this.renderManager.options.thirdPersonView == 2;
                float f2 = dog.getSize(dog.getPose()).height + 0.42F - (flag ? 0.25F : 0.0F) - (dog.isSleeping() ? 0.5F : 0);

                RenderUtil.renderLabelWithScale(this.getFontRendererFromRenderManager(), label, (float)x, (float)y + f2, (float)z, 0, f, f1, flag1, flag, 0.01F);
                RenderUtil.renderLabelWithScale(this.getFontRendererFromRenderManager(), dog.getDisplayName().getFormattedText(), (float)x, (float)y + f2 - 0.12F, (float)z, 0, f, f1, flag1, flag, 0.026F);

                if(d0 <= 5 * 5) {
                    if(this.renderManager.info.getRenderViewEntity().isCrouching()()) {
                        String ownerName = dog.getOwnersName().getFormattedText();


                        RenderUtil.renderLabelWithScale(this.getFontRendererFromRenderManager(), ownerName, (float)x, (float)y + f2 - 0.34F, (float)z, 0, f, f1, flag1, flag, 0.01F);
                    }
                }
            }
        }
    }**/

    @Override // 1.14 preRenderCallback
    protected void func_225620_a_(EntityDog entitylivingbaseIn, MatrixStack matrixStack, float partialTickTime) {
        EntityDog dog = entitylivingbaseIn;
        float size = dog.getDogSize() * 0.3F + 0.1F;
        RenderSystem.scalef(size, size, size);
        this.shadowSize = size * 0.5F;
    }
}