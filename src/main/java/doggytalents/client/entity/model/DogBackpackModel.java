package doggytalents.client.entity.model;

import com.google.common.collect.ImmutableList;

import doggytalents.common.entity.DogEntity;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class DogBackpackModel extends SegmentedModel<DogEntity> {

    public ModelRenderer rightChest;
    public ModelRenderer leftChest;

    public DogBackpackModel(float scaleFactor) {
        this.rightChest = new ModelRenderer(this, 52, 0);
        this.rightChest.addBox(2.0F, -1F, 0F, 2, 7, 4, scaleFactor);
        this.rightChest.setRotationPoint(0.0F, 14.0F, 2.0F);
        this.leftChest = new ModelRenderer(this, 52, 0);
        this.leftChest.addBox(-4.0F, -1F, 0F, 2, 7, 4, scaleFactor);
        this.leftChest.setRotationPoint(0.0F, 14.0F, 2.0F);
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.rightChest, this.leftChest);
    }

    @Override
    public void setLivingAnimations(DogEntity dogIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        if(dogIn.isSitting()) {
            if(dogIn.isLying()) {
                this.rightChest.setRotationPoint(0.0F, 20.0F, 2.0F);
                this.rightChest.rotateAngleX = ((float)Math.PI / 2F);
                this.leftChest.setRotationPoint(0.0F, 20.0F, 2.0F);
                this.leftChest.rotateAngleX = ((float)Math.PI / 2F);
            } else {
                this.rightChest.setRotationPoint(0.0F, 18.0F, 0.0F);
                this.rightChest.rotateAngleX = ((float)Math.PI / 4F);
                this.leftChest.setRotationPoint(0.0F, 18.0F, 0.0F);
                this.leftChest.rotateAngleX = ((float)Math.PI / 4F);
            }

        }
        else {
            this.rightChest.setRotationPoint(0.0F, 14.0F, 2.0F);
            this.rightChest.rotateAngleX = ((float)Math.PI / 2F);
            this.leftChest.setRotationPoint(0.0F, 14.0F, 2.0F);
            this.leftChest.rotateAngleX = ((float)Math.PI / 2F);

        }

        this.rightChest.rotateAngleZ = dogIn.getShakeAngle(partialTickTime, -0.16F);
        this.leftChest.rotateAngleZ = this.rightChest.rotateAngleZ;
    }

    @Override
    public void setRotationAngles(DogEntity dogIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }
}