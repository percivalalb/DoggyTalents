package doggytalents.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import doggytalents.ModTalents;
import doggytalents.client.model.entity.ModelChest;
import doggytalents.client.model.entity.ModelDog;
import doggytalents.client.model.entity.ModelSaddle;
import doggytalents.client.model.entity.ModelWings;
import doggytalents.client.renderer.RenderUtil;
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
import net.minecraft.util.text.TranslationTextComponent;

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
    public void render(EntityDog dogIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (dogIn.isDogWet()) {
           float f = dogIn.getBrightness() * dogIn.getShadingWhileWet(partialTicks);
           this.entityModel.func_228253_a_(f, f, f);
        }

        super.render(dogIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        if (this.canRenderName(dogIn)) {

            double d0 = this.renderManager.squareDistanceTo(dogIn);
            if(d0 <= 64 * 64) {
                String tip = dogIn.isIncapacicated() ? "dog.mode.incapacitated.indicator" : dogIn.MODE.getMode().getTip();
                String label = String.format(ConfigValues.DOG_GENDER ? "%s(%d)%s" : "%s(%d)",
                        new TranslationTextComponent(tip).getFormattedText(),
                        dogIn.getDogHunger(),
                        dogIn.GENDER.getGenderTip().getFormattedText());

                RenderUtil.renderLabelWithScale(dogIn, this, label, matrixStackIn, bufferIn, packedLightIn, 0.01F, -12);

                if(d0 <= 5 * 5) {
                    if(this.renderManager.info.getRenderViewEntity().isShiftKeyDown()) {
                        RenderUtil.renderLabelWithScale(dogIn, this, dogIn.getOwnersName().getFormattedText(), matrixStackIn, bufferIn, packedLightIn, 0.01F, 24);
                    }
                }
            }
        }

        if (dogIn.isDogWet()) {
           this.entityModel.func_228253_a_(1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public ResourceLocation getEntityTexture(EntityDog dog) {
        return ConfigValues.USE_DT_TEXTURES ? ResourceLib.getTameSkin(dog.getTameSkin()) : ResourceLib.MOB_DOG_TAME;
    }

    @Override
    protected void preRenderCallback(EntityDog entitylivingbaseIn, MatrixStack matrixStack, float partialTickTime) {
        EntityDog dog = entitylivingbaseIn;
        float size = dog.getDogSize() * 0.3F + 0.1F;
        RenderSystem.scalef(size, size, size);
        this.shadowSize = size * 0.5F;
    }
}