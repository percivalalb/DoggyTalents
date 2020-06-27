package doggytalents.client.entity.model;

import com.google.common.collect.ImmutableList;

import doggytalents.common.entity.DogEntity;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class BowTieModel extends SegmentedModel<DogEntity> {

    public ModelRenderer rightChest;
    public ModelRenderer middleChest;
    public ModelRenderer leftChest;

    public BowTieModel(float scaleFactor) {
        this.textureHeight = 16;
        this.textureWidth = 16;
        this.rightChest = new ModelRenderer(this, 52, 0);
        this.rightChest.addBox(-2.0F, 1.5F, -9.0F, 1, 2, 1, scaleFactor);
        this.rightChest.setRotationPoint(0.0F, 14.0F, 2.0F);
        this.middleChest = new ModelRenderer(this, 52, 0);
        this.middleChest.addBox(0.0F, 14.0F, -0.0F, 2, 1, 1, scaleFactor);
        this.middleChest.setRotationPoint(-1.0F, 2.0F, -9.0F);//0.0F, 14.0F, 2.0F);
        this.leftChest = new ModelRenderer(this, 52, 0);
        this.leftChest.addBox(1.0F, 1.5F, -9.0F, 1, 2, 1, scaleFactor);
        this.leftChest.setRotationPoint(0.0F, 14.0F, 2.0F);
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.rightChest, this.middleChest, this.leftChest);
    }

    @Override
    public void setLivingAnimations(DogEntity dogIn, float limbSwing, float limbSwingAmount, float partialTickTime) {

        this.rightChest.rotateAngleX += 0.1;
        this.middleChest.rotateAngleX += 0.1;
        this.leftChest.rotateAngleX += 0.1;
        this.rightChest.rotateAngleZ = dogIn.getShakeAngle(partialTickTime, -0.16F);
        this.leftChest.rotateAngleZ = this.rightChest.rotateAngleZ;
    }

    @Override
    public void setRotationAngles(DogEntity dogIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }
}