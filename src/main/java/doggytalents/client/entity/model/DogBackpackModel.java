package doggytalents.client.entity.model;

import com.google.common.collect.ImmutableList;

import doggytalents.common.entity.DogEntity;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;

public class DogBackpackModel extends ListModel<DogEntity> {

    public ModelPart rightChest;
    public ModelPart leftChest;

    public DogBackpackModel(float scaleFactor) {
        this.rightChest = new ModelPart(this, 52, 0);
        this.rightChest.addBox(2.0F, -1F, 0F, 2, 7, 4, scaleFactor);
        this.rightChest.setPos(0.0F, 14.0F, 2.0F);
        this.leftChest = new ModelPart(this, 52, 0);
        this.leftChest.addBox(-4.0F, -1F, 0F, 2, 7, 4, scaleFactor);
        this.leftChest.setPos(0.0F, 14.0F, 2.0F);
    }

    @Override
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(this.rightChest, this.leftChest);
    }

    @Override
    public void prepareMobModel(DogEntity dogIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        if (dogIn.isInSittingPose()) { // Mapping is wrong isEntitySleeping should be isSitting
            if (dogIn.isLying()) {
                this.rightChest.setPos(0.0F, 20.0F, 2.0F);
                this.rightChest.xRot = ((float)Math.PI / 2F);
                this.leftChest.setPos(0.0F, 20.0F, 2.0F);
                this.leftChest.xRot = ((float)Math.PI / 2F);
            } else {
                this.rightChest.setPos(0.0F, 18.0F, 0.0F);
                this.rightChest.xRot = ((float)Math.PI / 4F);
                this.leftChest.setPos(0.0F, 18.0F, 0.0F);
                this.leftChest.xRot = ((float)Math.PI / 4F);
            }

        }
        else {
            this.rightChest.setPos(0.0F, 14.0F, 2.0F);
            this.rightChest.xRot = ((float)Math.PI / 2F);
            this.leftChest.setPos(0.0F, 14.0F, 2.0F);
            this.leftChest.xRot = ((float)Math.PI / 2F);

        }

        this.rightChest.zRot = dogIn.getShakeAngle(partialTickTime, -0.16F);
        this.leftChest.zRot = this.rightChest.zRot;
    }

    @Override
    public void setupAnim(DogEntity dogIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }
}