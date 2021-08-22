package doggytalents.client.entity.model;

import com.google.common.collect.ImmutableList;

import doggytalents.api.inferface.AbstractDogEntity;
import net.minecraft.client.model.ColorableAgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class DogModel<T extends AbstractDogEntity> extends ColorableAgeableListModel<T> {

    public ModelPart head;
    public ModelPart body;
    public ModelPart legBackRight;
    public ModelPart legBackLeft;
    public ModelPart legFrontRight;
    public ModelPart legFrontLeft;
    public ModelPart tail;
    public ModelPart mane;

    public DogModel() {
        this(0.0F);
    }

    public DogModel(float scaleFactor) {
        float f1 = 13.5F;

        // COORDS
        // x is left/right of the dog
        // y is back and forward

        //Head
        this.head = new ModelPart(this, 0, 0);
        this.head.addBox(-2.0F, -3.0F, -2.0F, 6, 6, 4, scaleFactor);
        this.head.setPos(-1.0F, f1, -7.0F);

        //Body
        this.body = new ModelPart(this, 18, 14);
        this.body.addBox(-3.0F, -2.0F, -3.0F, 6, 9, 6, scaleFactor);
        this.body.setPos(0.0F, 14.0F, 2.0F);

        //Mane
        this.mane = new ModelPart(this, 21, 0);
        this.mane.addBox(-3.0F, -3.0F, -3.0F, 8, 6, 7, scaleFactor);
        this.mane.setPos(-1.0F, 14.0F, 2.0F);

        //Limbs
        this.legBackRight = new ModelPart(this, 0, 18);
        this.legBackRight.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, scaleFactor);
        this.legBackRight.setPos(-2.5F, 16.0F, 7.0F);
        this.legBackLeft = new ModelPart(this, 0, 18);
        this.legBackLeft.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, scaleFactor);
        this.legBackLeft.setPos(0.5F, 16.0F, 7.0F);
        this.legFrontRight = new ModelPart(this, 0, 18);
        this.legFrontRight.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, scaleFactor);
        this.legFrontRight.setPos(-2.5F, 16.0F, -4.0F);
        this.legFrontLeft = new ModelPart(this, 0, 18);
        this.legFrontLeft.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, scaleFactor);
        this.legFrontLeft.setPos(0.5F, 16.0F, -4.0F);

        //Tail1
        this.tail = new ModelPart(this, 9, 18);
        this.tail.addBox(-0.5F, 0.0F, -1.0F, 2, 8, 2, scaleFactor);
        this.tail.setPos(-0.5F, 12.0F, 8.0F);

        //Tail2
        this.tail.texOffs(45, 0).addBox(0.0F, 0.0F, 0.0F, 2, 3, 1).setPos(90.0F, 0.0F, 0.0F);

        //Tail3
        this.tail.texOffs(43, 19).addBox(-1.0F, 0F, -2F, 3, 10, 3).setPos(-1.0F, 12.0F, 8.0F);

        //HeadMain EarsNormal
        this.head.texOffs(16, 14).addBox(-2.0F, -5.0F, 0.0F, 2, 2, 1, scaleFactor);
        this.head.texOffs(16, 14).addBox(2.0F, -5.0F, 0.0F, 2, 2, 1, scaleFactor);

        //HeadMain EarsBoni
        this.head.texOffs(52, 0).addBox(-3.0F, -3.0F, -1.5F, 1, 5, 3, scaleFactor);
        this.head.texOffs(52, 0).addBox(4.0F, -3.0F, -1.5F, 1, 5, 3, scaleFactor);

        //HeadMain EarsSmall
        this.head.texOffs(18, 0).addBox(-2.8F, -3.5F, -1.0F, 2, 1, 2, scaleFactor);
        this.head.texOffs(18, 0).addBox(2.8F, -3.5F, -1.0F, 2, 1, 2, scaleFactor);

        //HeadMain Nose
        this.head.texOffs(0, 10).addBox(-0.5F, 0.0F, -5.0F, 3, 3, 4, scaleFactor);
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.legBackRight, this.legBackLeft, this.legFrontRight, this.legFrontLeft, this.tail, this.mane);
    }

    @Override
    public void prepareMobModel(T dog, float limbSwing, float limbSwingAmount, float partialTickTime) {
        this.tail.yRot = dog.getWagAngle(limbSwing, limbSwingAmount, partialTickTime);

        if (dog.isInSittingPose()) { // Mapping is wrong isEntitySleeping should be isSitting
            if (dog.isLying()) {
                this.head.setPos(-1, 19.5F, -7);
                this.body.setPos(0, 20, 2);
                this.body.xRot = (float)Math.PI / 2F;
                this.mane.setPos(-1, 20, -2);
                this.mane.xRot = this.body.xRot;
                this.tail.setPos(-1, 18, 8);
                this.legBackRight.setPos(-4.5F, 23, 7);
                this.legBackRight.xRot = -(float)Math.PI / 2F;
                this.legBackLeft.setPos(2.5F, 23, 7);
                this.legBackLeft.xRot = -(float)Math.PI / 2F;
                this.legFrontRight.setPos(-4.5F, 23, -4);
                this.legFrontRight.xRot = -(float)Math.PI / 2F;
                this.legFrontLeft.setPos(2.5F, 23, -4);
                this.legFrontLeft.xRot = -(float)Math.PI / 2F;

//                this.body.setRotationPoint(0.0F, 14.0F, 0.0F);
//                this.body.rotateAngleX = ((float)Math.PI / 2F);
//                this.mane.setRotationPoint(-1.0F, 19.0F, -3.0F);
//                this.mane.rotateAngleX = this.body.rotateAngleX;
//                this.head.setRotationPoint(-1.0F, 17.0F, -7.0F);
//
//                this.tail.setRotationPoint(-0.5F, 17.0F, 8.0F); // +4.0D
//                this.legBackRight.setRotationPoint(-4.5F, 20.0F, 7.0F);
//                this.legBackLeft.setRotationPoint(2.5F, 20.0F, 7.0F);
//                this.legFrontRight.setRotationPoint(-3.0F, 22.0F, -3.0F);
//                this.legFrontLeft.setRotationPoint(1.0F, 22.0F, -3.0F);
//
//                this.legBackRight.rotateAngleX = -(float)Math.PI / 2.6F;
//                this.legBackLeft.rotateAngleX = -(float)Math.PI / 2.6F;
//
//                this.legFrontRight.rotateAngleX = -(float)Math.PI / 2;
//                this.legFrontRight.rotateAngleY = (float)Math.PI / 10;
//                this.legFrontLeft.rotateAngleX = -(float)Math.PI / 2;
//                this.legFrontLeft.rotateAngleY = -(float)Math.PI / 10;
            } else if (dog.isLying() && false) {
                this.body.setPos(0.0F, 19.0F, 2.0F);
                this.body.xRot = ((float)Math.PI / 2F);
                this.mane.setPos(-1.0F, 19.0F, -3.0F);
                this.mane.xRot = this.body.xRot;
                this.head.setPos(-1.0F, 17.0F, -7.0F);

                this.tail.setPos(-0.5F, 17.0F, 8.0F); // +4.0D
                this.legBackRight.setPos(-4.5F, 20.0F, 7.0F);
                this.legBackLeft.setPos(2.5F, 20.0F, 7.0F);
                this.legFrontRight.setPos(-3.0F, 22.0F, -3.0F);
                this.legFrontLeft.setPos(1.0F, 22.0F, -3.0F);

                this.legBackRight.xRot = -(float)Math.PI / 2.6F;
                this.legBackLeft.xRot = -(float)Math.PI / 2.6F;

                this.legFrontRight.xRot = -(float)Math.PI / 2;
                this.legFrontRight.yRot = (float)Math.PI / 10;
                this.legFrontLeft.xRot = -(float)Math.PI / 2;
                this.legFrontLeft.yRot = -(float)Math.PI / 10;
            } else {
                this.head.setPos(-1.0F, 13.5F, -7.0F);
                this.mane.setPos(-1.0F, 16.0F, -3.0F);
                this.mane.xRot = ((float)Math.PI * 2F / 5F);
                this.mane.yRot = 0.0F;
                this.body.setPos(0.0F, 18.0F, 0.0F);
                this.body.xRot = ((float)Math.PI / 4F);
                this.tail.setPos(-0.5F, 21.0F, 6.0F);
                this.legBackRight.setPos(-2.5F, 22.0F, 2.0F);
                this.legBackRight.xRot = ((float)Math.PI * 3F / 2F);
                this.legBackLeft.setPos(0.5F, 22.0F, 2.0F);
                this.legBackLeft.xRot = ((float)Math.PI * 3F / 2F);
                this.legFrontRight.xRot = 5.811947F;
                this.legFrontRight.setPos(-2.49F, 17.0F, -4.0F);
                this.legFrontLeft.xRot = 5.811947F;
                this.legFrontLeft.setPos(0.51F, 17.0F, -4.0F);


                this.head.setPos(-1.0F, 13.5F, -7.0F);
                this.legFrontRight.yRot = 0;
                this.legFrontLeft.yRot = 0;
            }
        } else {
            this.body.setPos(0.0F, 14.0F, 2.0F);
            this.body.xRot = ((float)Math.PI / 2F);
            this.mane.setPos(-1.0F, 14.0F, -3.0F);
            this.mane.xRot = this.body.xRot;
            this.tail.setPos(-0.5F, 12.0F, 8.0F);
            this.legBackRight.setPos(-2.5F, 16.0F, 7.0F);
            this.legBackLeft.setPos(0.5F, 16.0F, 7.0F);
            this.legFrontRight.setPos(-2.5F, 16.0F, -4.0F);
            this.legFrontLeft.setPos(0.5F, 16.0F, -4.0F);
            this.legBackRight.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.legBackLeft.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
            this.legFrontRight.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
            this.legFrontLeft.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

            this.head.setPos(-1.0F, 13.5F, -7.0F);
            this.legFrontRight.yRot = 0.0F;
            this.legFrontLeft.yRot = 0.0F;
        }

        this.head.zRot = dog.getInterestedAngle(partialTickTime) + dog.getShakeAngle(partialTickTime, 0.0F);
        this.mane.zRot = dog.getShakeAngle(partialTickTime, -0.08F);
        this.body.zRot = dog.getShakeAngle(partialTickTime, -0.16F);
        this.tail.zRot = dog.getShakeAngle(partialTickTime, -0.2F);
    }

    @Override
    public void setupAnim(T dogIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        this.head.yRot = netHeadYaw * (dogIn.isInSittingPose() && dogIn.isLying() ? 0.005F : (float)Math.PI / 180F);
        this.tail.xRot = ageInTicks;
    }

    public void setVisible(boolean visible) {
        this.head.visible = visible;
        this.body.visible = visible;
        this.legBackRight.visible = visible;
        this.legBackLeft.visible = visible;
        this.legFrontRight.visible = visible;
        this.legFrontLeft.visible = visible;
        this.tail.visible = visible;
        this.mane.visible = visible;
    }
}
