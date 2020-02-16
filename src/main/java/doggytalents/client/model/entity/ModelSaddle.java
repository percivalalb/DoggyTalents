package doggytalents.client.model.entity;

import com.google.common.collect.ImmutableList;

import doggytalents.entity.EntityDog;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelSaddle extends SegmentedModel<EntityDog> {

    public ModelRenderer chest1;
    public ModelRenderer chest2;
    public ModelRenderer chest3;

    public ModelSaddle(float scaleFactor) {
        this.chest1 = new ModelRenderer(this, 52, 11);
        this.chest1.addBox(-2.5F, 0F, 3F, 5, 6, 1, scaleFactor);
        this.chest1.setRotationPoint(0.0F, 14.0F, 2.0F);
        this.chest2 = new ModelRenderer(this, 52, 18);
        this.chest2.addBox(-2.0F, 0F, 3.5F, 4, 1, 1, scaleFactor);
        this.chest2.setRotationPoint(0.0F, 14.0F, 2.0F);
        this.chest3 = new ModelRenderer(this, 52, 18);
        this.chest3.addBox(-2.0F, 5F, 3.5F, 4, 1, 1, scaleFactor);
        this.chest3.setRotationPoint(0.0F, 14.0F, 2.0F);
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.chest1, this.chest2, this.chest3);
    }

    @Override
    public void setLivingAnimations(EntityDog dogIn, float limbSwing, float limbSwingAmount, float partialTickTime) {

        if(dogIn.isSitting()) {
            this.chest1.setRotationPoint(0.0F, 18.0F, 0.0F);
            this.chest1.rotateAngleX = ((float)Math.PI / 4F);
            this.chest2.setRotationPoint(0.0F, 18.0F, 0.0F);
            this.chest2.rotateAngleX = ((float)Math.PI / 4F);
            this.chest3.setRotationPoint(0.0F, 18.0F, 0.0F);
            this.chest3.rotateAngleX = ((float)Math.PI / 4F);
        }
        else {
            this.chest1.setRotationPoint(0.0F, 14.0F, 2.0F);
            this.chest1.rotateAngleX = ((float)Math.PI / 2F);
            this.chest2.setRotationPoint(0.0F, 14.0F, 2.0F);
            this.chest2.rotateAngleX = ((float)Math.PI / 2F);
            this.chest3.setRotationPoint(0.0F, 14.0F, 2.0F);
            this.chest3.rotateAngleX = ((float)Math.PI / 2F);
        }

        this.chest1.rotateAngleZ = dogIn.getShakeAngle(partialTickTime, -0.16F);
        this.chest2.rotateAngleZ = this.chest1.rotateAngleZ;
        this.chest3.rotateAngleZ = this.chest1.rotateAngleZ;
    }

    @Override
    public void setRotationAngles(EntityDog dogIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }
}