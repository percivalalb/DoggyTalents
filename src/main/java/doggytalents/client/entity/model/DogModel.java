package doggytalents.client.entity.model;

import com.google.common.collect.ImmutableList;

import doggytalents.api.inferface.AbstractDogEntity;
import net.minecraft.client.renderer.entity.model.TintedAgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class DogModel<T extends AbstractDogEntity> extends TintedAgeableModel<T> {

    public ModelRenderer head;
    public ModelRenderer body;
    public ModelRenderer legBackRight;
    public ModelRenderer legBackLeft;
    public ModelRenderer legFrontRight;
    public ModelRenderer legFrontLeft;
    public ModelRenderer tail;
    public ModelRenderer mane;

    public DogModel() {
        this(0.0F);
    }

    public DogModel(float scaleFactor) {
        float f1 = 13.5F;

        // COORDS
        // x is left/right of the dog
        // y is back and forward

        //Head
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-2.0F, -3.0F, -2.0F, 6, 6, 4, scaleFactor);
        this.head.setRotationPoint(-1.0F, f1, -7.0F);

        //Body
        this.body = new ModelRenderer(this, 18, 14);
        this.body.addBox(-3.0F, -2.0F, -3.0F, 6, 9, 6, scaleFactor);
        this.body.setRotationPoint(0.0F, 14.0F, 2.0F);

        //Mane
        this.mane = new ModelRenderer(this, 21, 0);
        this.mane.addBox(-3.0F, -3.0F, -3.0F, 8, 6, 7, scaleFactor);
        this.mane.setRotationPoint(-1.0F, 14.0F, 2.0F);

        //Limbs
        this.legBackRight = new ModelRenderer(this, 0, 18);
        this.legBackRight.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, scaleFactor);
        this.legBackRight.setRotationPoint(-2.5F, 16.0F, 7.0F);
        this.legBackLeft = new ModelRenderer(this, 0, 18);
        this.legBackLeft.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, scaleFactor);
        this.legBackLeft.setRotationPoint(0.5F, 16.0F, 7.0F);
        this.legFrontRight = new ModelRenderer(this, 0, 18);
        this.legFrontRight.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, scaleFactor);
        this.legFrontRight.setRotationPoint(-2.5F, 16.0F, -4.0F);
        this.legFrontLeft = new ModelRenderer(this, 0, 18);
        this.legFrontLeft.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, scaleFactor);
        this.legFrontLeft.setRotationPoint(0.5F, 16.0F, -4.0F);

        //Tail1
        this.tail = new ModelRenderer(this, 9, 18);
        this.tail.addBox(-0.5F, 0.0F, -1.0F, 2, 8, 2, scaleFactor);
        this.tail.setRotationPoint(-0.5F, 12.0F, 8.0F);

        //Tail2
        this.tail.setTextureOffset(45, 0).addBox(0.0F, 0.0F, 0.0F, 2, 3, 1).setRotationPoint(90.0F, 0.0F, 0.0F);

        //Tail3
        this.tail.setTextureOffset(43, 19).addBox(-1.0F, 0F, -2F, 3, 10, 3).setRotationPoint(-1.0F, 12.0F, 8.0F);

        //HeadMain EarsNormal
        this.head.setTextureOffset(16, 14).addBox(-2.0F, -5.0F, 0.0F, 2, 2, 1, scaleFactor);
        this.head.setTextureOffset(16, 14).addBox(2.0F, -5.0F, 0.0F, 2, 2, 1, scaleFactor);

        //HeadMain EarsBoni
        this.head.setTextureOffset(52, 0).addBox(-3.0F, -3.0F, -1.5F, 1, 5, 3, scaleFactor);
        this.head.setTextureOffset(52, 0).addBox(4.0F, -3.0F, -1.5F, 1, 5, 3, scaleFactor);

        //HeadMain EarsSmall
        this.head.setTextureOffset(18, 0).addBox(-2.8F, -3.5F, -1.0F, 2, 1, 2, scaleFactor);
        this.head.setTextureOffset(18, 0).addBox(2.8F, -3.5F, -1.0F, 2, 1, 2, scaleFactor);

        //HeadMain Nose
        this.head.setTextureOffset(0, 10).addBox(-0.5F, 0.0F, -5.0F, 3, 3, 4, scaleFactor);
    }

    @Override
    protected Iterable<ModelRenderer> getHeadParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelRenderer> getBodyParts() {
        return ImmutableList.of(this.body, this.legBackRight, this.legBackLeft, this.legFrontRight, this.legFrontLeft, this.tail, this.mane);
    }

    @Override
    public void setLivingAnimations(T dog, float limbSwing, float limbSwingAmount, float partialTickTime) {
        this.tail.rotateAngleY = dog.getWagAngle(limbSwing, limbSwingAmount, partialTickTime);

        if (dog.isEntitySleeping()) { // Mapping is wrong isEntitySleeping should be isSitting
            if (dog.isLying()) {
                this.head.setRotationPoint(-1, 19.5F, -7);
                this.body.setRotationPoint(0, 20, 2);
                this.body.rotateAngleX = (float)Math.PI / 2F;
                this.mane.setRotationPoint(-1, 20, -2);
                this.mane.rotateAngleX = this.body.rotateAngleX;
                this.tail.setRotationPoint(-1, 18, 8);
                this.legBackRight.setRotationPoint(-4.5F, 23, 7);
                this.legBackRight.rotateAngleX = -(float)Math.PI / 2F;
                this.legBackLeft.setRotationPoint(2.5F, 23, 7);
                this.legBackLeft.rotateAngleX = -(float)Math.PI / 2F;
                this.legFrontRight.setRotationPoint(-4.5F, 23, -4);
                this.legFrontRight.rotateAngleX = -(float)Math.PI / 2F;
                this.legFrontLeft.setRotationPoint(2.5F, 23, -4);
                this.legFrontLeft.rotateAngleX = -(float)Math.PI / 2F;

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
                this.body.setRotationPoint(0.0F, 19.0F, 2.0F);
                this.body.rotateAngleX = ((float)Math.PI / 2F);
                this.mane.setRotationPoint(-1.0F, 19.0F, -3.0F);
                this.mane.rotateAngleX = this.body.rotateAngleX;
                this.head.setRotationPoint(-1.0F, 17.0F, -7.0F);

                this.tail.setRotationPoint(-0.5F, 17.0F, 8.0F); // +4.0D
                this.legBackRight.setRotationPoint(-4.5F, 20.0F, 7.0F);
                this.legBackLeft.setRotationPoint(2.5F, 20.0F, 7.0F);
                this.legFrontRight.setRotationPoint(-3.0F, 22.0F, -3.0F);
                this.legFrontLeft.setRotationPoint(1.0F, 22.0F, -3.0F);

                this.legBackRight.rotateAngleX = -(float)Math.PI / 2.6F;
                this.legBackLeft.rotateAngleX = -(float)Math.PI / 2.6F;

                this.legFrontRight.rotateAngleX = -(float)Math.PI / 2;
                this.legFrontRight.rotateAngleY = (float)Math.PI / 10;
                this.legFrontLeft.rotateAngleX = -(float)Math.PI / 2;
                this.legFrontLeft.rotateAngleY = -(float)Math.PI / 10;
            } else {
                this.head.setRotationPoint(-1.0F, 13.5F, -7.0F);
                this.mane.setRotationPoint(-1.0F, 16.0F, -3.0F);
                this.mane.rotateAngleX = ((float)Math.PI * 2F / 5F);
                this.mane.rotateAngleY = 0.0F;
                this.body.setRotationPoint(0.0F, 18.0F, 0.0F);
                this.body.rotateAngleX = ((float)Math.PI / 4F);
                this.tail.setRotationPoint(-0.5F, 21.0F, 6.0F);
                this.legBackRight.setRotationPoint(-2.5F, 22.0F, 2.0F);
                this.legBackRight.rotateAngleX = ((float)Math.PI * 3F / 2F);
                this.legBackLeft.setRotationPoint(0.5F, 22.0F, 2.0F);
                this.legBackLeft.rotateAngleX = ((float)Math.PI * 3F / 2F);
                this.legFrontRight.rotateAngleX = 5.811947F;
                this.legFrontRight.setRotationPoint(-2.49F, 17.0F, -4.0F);
                this.legFrontLeft.rotateAngleX = 5.811947F;
                this.legFrontLeft.setRotationPoint(0.51F, 17.0F, -4.0F);


                this.head.setRotationPoint(-1.0F, 13.5F, -7.0F);
                this.legFrontRight.rotateAngleY = 0;
                this.legFrontLeft.rotateAngleY = 0;
            }
        } else {
            this.body.setRotationPoint(0.0F, 14.0F, 2.0F);
            this.body.rotateAngleX = ((float)Math.PI / 2F);
            this.mane.setRotationPoint(-1.0F, 14.0F, -3.0F);
            this.mane.rotateAngleX = this.body.rotateAngleX;
            this.tail.setRotationPoint(-0.5F, 12.0F, 8.0F);
            this.legBackRight.setRotationPoint(-2.5F, 16.0F, 7.0F);
            this.legBackLeft.setRotationPoint(0.5F, 16.0F, 7.0F);
            this.legFrontRight.setRotationPoint(-2.5F, 16.0F, -4.0F);
            this.legFrontLeft.setRotationPoint(0.5F, 16.0F, -4.0F);
            this.legBackRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.legBackLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
            this.legFrontRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
            this.legFrontLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

            this.head.setRotationPoint(-1.0F, 13.5F, -7.0F);
            this.legFrontRight.rotateAngleY = 0.0F;
            this.legFrontLeft.rotateAngleY = 0.0F;
        }

        this.head.rotateAngleZ = dog.getInterestedAngle(partialTickTime) + dog.getShakeAngle(partialTickTime, 0.0F);
        this.mane.rotateAngleZ = dog.getShakeAngle(partialTickTime, -0.08F);
        this.body.rotateAngleZ = dog.getShakeAngle(partialTickTime, -0.16F);
        this.tail.rotateAngleZ = dog.getShakeAngle(partialTickTime, -0.2F);
    }

    @Override
    public void setRotationAngles(T dogIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        this.head.rotateAngleY = netHeadYaw * (dogIn.isEntitySleeping() && dogIn.isLying() ? 0.005F : (float)Math.PI / 180F);
        this.tail.rotateAngleX = ageInTicks;
    }

    public void setVisible(boolean visible) {
        this.head.showModel = visible;
        this.body.showModel = visible;
        this.legBackRight.showModel = visible;
        this.legBackLeft.showModel = visible;
        this.legFrontRight.showModel = visible;
        this.legFrontLeft.showModel = visible;
        this.tail.showModel = visible;
        this.mane.showModel = visible;
    }
}
