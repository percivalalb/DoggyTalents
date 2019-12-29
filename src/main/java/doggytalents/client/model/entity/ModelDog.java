package doggytalents.client.model.entity;

import com.google.common.collect.ImmutableList;

import doggytalents.entity.EntityDog;
import net.minecraft.client.renderer.entity.model.TintedAgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author ProPercivalalb
 */
@OnlyIn(Dist.CLIENT)
public class ModelDog extends TintedAgeableModel<EntityDog> {

    public ModelRenderer head;
    public ModelRenderer body;
    public ModelRenderer legBackRight;
    public ModelRenderer legBackLeft;
    public ModelRenderer legFrontRight;
    public ModelRenderer legFrontLeft;
    public ModelRenderer tail;
    public ModelRenderer mane;

    public ModelDog(float scaleFactor) {
        float f1 = 13.5F;

        //Head
        this.head = new ModelRenderer(this, 0, 0);
        this.head.func_228301_a_(-2.0F, -3.0F, -2.0F, 6, 6, 4, scaleFactor);
        this.head.setRotationPoint(-1.0F, f1, -7.0F);

        //Body
        this.body = new ModelRenderer(this, 18, 14);
        this.body.func_228301_a_(-3.0F, -2.0F, -3.0F, 6, 9, 6, scaleFactor);
        this.body.setRotationPoint(0.0F, 14.0F, 2.0F);

        //Mane
        this.mane = new ModelRenderer(this, 21, 0);
        this.mane.func_228301_a_(-3.0F, -3.0F, -3.0F, 8, 6, 7, scaleFactor);
        this.mane.setRotationPoint(-1.0F, 14.0F, 2.0F);

        //Limbs
        this.legBackRight = new ModelRenderer(this, 0, 18);
        this.legBackRight.func_228301_a_(0.0F, 0.0F, -1.0F, 2, 8, 2, scaleFactor);
        this.legBackRight.setRotationPoint(-2.5F, 16.0F, 7.0F);
        this.legBackLeft = new ModelRenderer(this, 0, 18);
        this.legBackLeft.func_228301_a_(0.0F, 0.0F, -1.0F, 2, 8, 2, scaleFactor);
        this.legBackLeft.setRotationPoint(0.5F, 16.0F, 7.0F);
        this.legFrontRight = new ModelRenderer(this, 0, 18);
        this.legFrontRight.func_228301_a_(0.0F, 0.0F, -1.0F, 2, 8, 2, scaleFactor);
        this.legFrontRight.setRotationPoint(-2.5F, 16.0F, -4.0F);
        this.legFrontLeft = new ModelRenderer(this, 0, 18);
        this.legFrontLeft.func_228301_a_(0.0F, 0.0F, -1.0F, 2, 8, 2, scaleFactor);
        this.legFrontLeft.setRotationPoint(0.5F, 16.0F, -4.0F);

        //Tail1
        this.tail = new ModelRenderer(this, 9, 18);
        this.tail.func_228301_a_(-0.5F, 0.0F, -1.0F, 2, 8, 2, scaleFactor);
        this.tail.setRotationPoint(-0.5F, 12.0F, 8.0F);

        //Tail2
        this.tail.setTextureOffset(45, 0).func_228300_a_(0.0F, 0.0F, 0.0F, 2, 3, 1).setRotationPoint(90.0F, 0.0F, 0.0F);

        //HeadMain EarsNormal
        this.head.setTextureOffset(16, 14).func_228301_a_(-2.0F, -5.0F, 0.0F, 2, 2, 1, scaleFactor);
        this.head.setTextureOffset(16, 14).func_228301_a_(2.0F, -5.0F, 0.0F, 2, 2, 1, scaleFactor);

        //HeadMain EarsBoni
        this.head.setTextureOffset(42, 14).func_228301_a_(-3.0F, -3.0F, -1.5F, 1, 5, 3, scaleFactor);
        this.head.setTextureOffset(42, 14).func_228301_a_(4.0F, -3.0F, -1.5F, 1, 5, 3, scaleFactor);

        //HeadMain EarsSmall
        this.head.setTextureOffset(18, 0).func_228301_a_(-2.8F, -3.5F, -1.0F, 2, 1, 2, scaleFactor);
        this.head.setTextureOffset(18, 0).func_228301_a_(2.8F, -3.5F, -1.0F, 2, 1, 2, scaleFactor);

        //HeadMain Nose
        this.head.setTextureOffset(0, 10).func_228301_a_(-0.5F, 0.0F, -5.0F, 3, 3, 4, scaleFactor);
    }

    @Override
    protected Iterable<ModelRenderer> func_225602_a_() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelRenderer> func_225600_b_() {
        return ImmutableList.of(this.body, this.legBackRight, this.legBackLeft, this.legFrontRight, this.legFrontLeft, this.tail, this.mane);
    }

    @Override
    public void setLivingAnimations(EntityDog dog, float limbSwing, float limbSwingAmount, float partialTickTime) {
        this.tail.rotateAngleY = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

        if(dog.isSitting()) {
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
        } else if(dog.isLyingDown()) {
            this.body.setRotationPoint(0.0F, 14.0F, 0.0F);
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
        } else if(dog.isLyingDown() && false) {
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

        if(dog.isSitting() || dog.getMotion().equals(Vec3d.ZERO)) {
            //DoggyTalentsMod.LOGGER.info("WAG");
            this.tail.rotateAngleY = dog.getWagAngle(partialTickTime, 0.0F);
        }
    }

    @Override
    public void func_225597_a_(EntityDog dogIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) { // 1.14 setRotationAngles
        this.head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        this.head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
        this.tail.rotateAngleX = ageInTicks;
    }
}
