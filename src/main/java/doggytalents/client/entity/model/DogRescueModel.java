package doggytalents.client.entity.model;

import com.google.common.collect.ImmutableList;

import doggytalents.common.entity.DogEntity;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class DogRescueModel extends SegmentedModel<DogEntity>{

    public ModelRenderer rescueBox;

    public DogRescueModel() {
        this.rescueBox = new ModelRenderer(this, 0, 0);
        this.rescueBox.addBox(-1F, -4F, -4.5F, 4, 2, 2);
        this.rescueBox.setRotationPoint(-1F, 14F, -3F);
        this.rescueBox.rotateAngleX = (float) (Math.PI / 2);
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.rescueBox);
    }

    @Override
    public void setLivingAnimations(DogEntity dogIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        if (dogIn.isEntitySleeping()) { // Mapping is wrong isEntitySleeping should be isSitting
            if (dogIn.isLying()) {
                this.rescueBox.setRotationPoint(-1F, 20F, -2F);
                this.rescueBox.rotateAngleX = (float) (Math.PI / 2);
            }
            else {
                this.rescueBox.setRotationPoint(-1, 16, -3);
                this.rescueBox.rotateAngleX = (float) (Math.PI * 2 / 5);
            }
        }
        else {
            this.rescueBox.setRotationPoint(-1F, 14F, -3F);
            this.rescueBox.rotateAngleX = (float) (Math.PI / 2);
        }
    }

    @Override
    public void setRotationAngles(DogEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }
}