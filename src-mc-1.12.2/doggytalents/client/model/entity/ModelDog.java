package doggytalents.client.model.entity;

import doggytalents.entity.EntityDog;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
@SideOnly(Side.CLIENT)
public class ModelDog extends ModelBase {
    
    public ModelRenderer wolfHeadMain;
    public ModelRenderer wolfBody;
    public ModelRenderer legBackRight;
    public ModelRenderer legBackLeft;
    public ModelRenderer legFrontRight;
    public ModelRenderer legFrontLeft;
    public ModelRenderer wolfTail;
    public ModelRenderer wolfMane;
    
    public ModelDog(float scaleFactor) {
        float f1 = 13.5F;
        
        //Head
        this.wolfHeadMain = new ModelRenderer(this, 0, 0);
        this.wolfHeadMain.addBox(-2.0F, -3.0F, -2.0F, 6, 6, 4, scaleFactor);
        this.wolfHeadMain.setRotationPoint(-1.0F, f1, -7.0F);
        
        //Body
        this.wolfBody = new ModelRenderer(this, 18, 14);
        this.wolfBody.addBox(-3.0F, -2.0F, -3.0F, 6, 9, 6, scaleFactor);
        this.wolfBody.setRotationPoint(0.0F, 14.0F, 2.0F);

        //Mane
        this.wolfMane = new ModelRenderer(this, 21, 0);
        this.wolfMane.addBox(-3.0F, -3.0F, -3.0F, 8, 6, 7, scaleFactor);
        this.wolfMane.setRotationPoint(-1.0F, 14.0F, 2.0F);
        
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
        this.wolfTail = new ModelRenderer(this, 9, 18);
        this.wolfTail.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, scaleFactor);
        this.wolfTail.setRotationPoint(-1.0F, 12.0F, 8.0F);              

        //Tail2
        this.wolfTail.setTextureOffset(45, 0).addBox(0.0F, 0.0F, 0.0F, 2, 3, 1).setRotationPoint(90.0F, 0.0F, 0.0F);

        //HeadMain EarsNormal
        this.wolfHeadMain.setTextureOffset(16, 14).addBox(-2.0F, -5.0F, 0.0F, 2, 2, 1, scaleFactor); 
        this.wolfHeadMain.setTextureOffset(16, 14).addBox(2.0F, -5.0F, 0.0F, 2, 2, 1, scaleFactor); 
        
        //HeadMain EarsBoni
        this.wolfHeadMain.setTextureOffset(42, 14).addBox(-3.0F, -3.0F, -1.5F, 1, 5, 3, scaleFactor);
        this.wolfHeadMain.setTextureOffset(42, 14).addBox(4.0F, -3.0F, -1.5F, 1, 5, 3, scaleFactor);
        
        //HeadMain EarsSmall
        this.wolfHeadMain.setTextureOffset(18, 0).addBox(-2.8F, -3.5F, -1.0F, 2, 1, 2, scaleFactor);
        this.wolfHeadMain.setTextureOffset(18, 0).addBox(2.8F, -3.5F, -1.0F, 2, 1, 2, scaleFactor);
        
        //HeadMain Nose
        this.wolfHeadMain.setTextureOffset(0, 10).addBox(-0.5F, 0.0F, -5.0F, 3, 3, 4, scaleFactor);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        EntityDog dog = (EntityDog)entityIn;
        
        if(this.isChild) {
             GlStateManager.pushMatrix();
             GlStateManager.translate(0.0F, 5.0F * scale, 2.0F * scale);
             this.wolfHeadMain.renderWithRotation(scale);
             GlStateManager.popMatrix();
             GlStateManager.pushMatrix();
             GlStateManager.scale(0.5F, 0.5F, 0.5F);
             GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
             this.wolfBody.render(scale);
             this.wolfBody.render(scale);
             this.legBackRight.render(scale);
             this.legBackLeft.render(scale);
             this.legFrontRight.render(scale);
             this.legFrontLeft.render(scale);
             this.wolfTail.renderWithRotation(scale);
             this.wolfMane.render(scale);
             GlStateManager.popMatrix();
        }
        else {
            this.wolfHeadMain.renderWithRotation(scale);
            this.wolfBody.render(scale);
            this.legBackRight.render(scale);
            this.legBackLeft.render(scale);
            this.legFrontRight.render(scale);
            this.legFrontLeft.render(scale);
            this.wolfTail.renderWithRotation(scale);
            this.wolfMane.render(scale);
        }
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        EntityDog dog = (EntityDog)entitylivingbaseIn;

        this.wolfTail.rotateAngleY = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

        if(dog.isSitting()) {
            this.wolfMane.setRotationPoint(-1.0F, 16.0F, -3.0F);
            this.wolfMane.rotateAngleX = ((float)Math.PI * 2F / 5F);
            this.wolfMane.rotateAngleY = 0.0F;
            this.wolfBody.setRotationPoint(0.0F, 18.0F, 0.0F);
            this.wolfBody.rotateAngleX = ((float)Math.PI / 4F);
            this.wolfTail.setRotationPoint(-1.0F, 21.0F, 6.0F);
            this.legBackRight.setRotationPoint(-2.5F, 22.0F, 2.0F);
            this.legBackRight.rotateAngleX = ((float)Math.PI * 3F / 2F);
            this.legBackLeft.setRotationPoint(0.5F, 22.0F, 2.0F);
            this.legBackLeft.rotateAngleX = ((float)Math.PI * 3F / 2F);
            this.legFrontRight.rotateAngleX = 5.811947F;
            this.legFrontRight.setRotationPoint(-2.49F, 17.0F, -4.0F);
            this.legFrontLeft.rotateAngleX = 5.811947F;
            this.legFrontLeft.setRotationPoint(0.51F, 17.0F, -4.0F);
            
            
            this.wolfHeadMain.setRotationPoint(-1.0F, 13.5F, -7.0F);
            this.legFrontRight.rotateAngleY = 0;
            this.legFrontLeft.rotateAngleY = 0;
        } else if(dog.isLyingDown()) {
            this.wolfBody.setRotationPoint(0.0F, 14.0F, 0.0F);
            this.wolfBody.rotateAngleX = ((float)Math.PI / 2F);
            this.wolfMane.setRotationPoint(-1.0F, 19.0F, -3.0F);
            this.wolfMane.rotateAngleX = this.wolfBody.rotateAngleX;
            this.wolfHeadMain.setRotationPoint(-1.0F, 17.0F, -7.0F);
            
            this.wolfTail.setRotationPoint(-1.0F, 17.0F, 8.0F); // +4.0D
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
            this.wolfBody.setRotationPoint(0.0F, 19.0F, 2.0F);
            this.wolfBody.rotateAngleX = ((float)Math.PI / 2F);
            this.wolfMane.setRotationPoint(-1.0F, 19.0F, -3.0F);
            this.wolfMane.rotateAngleX = this.wolfBody.rotateAngleX;
            this.wolfHeadMain.setRotationPoint(-1.0F, 17.0F, -7.0F);
            
            this.wolfTail.setRotationPoint(-1.0F, 17.0F, 8.0F); // +4.0D
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
            this.wolfBody.setRotationPoint(0.0F, 14.0F, 2.0F);
            this.wolfBody.rotateAngleX = ((float)Math.PI / 2F);
            this.wolfMane.setRotationPoint(-1.0F, 14.0F, -3.0F);
            this.wolfMane.rotateAngleX = this.wolfBody.rotateAngleX;
            this.wolfTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
            this.legBackRight.setRotationPoint(-2.5F, 16.0F, 7.0F);
            this.legBackLeft.setRotationPoint(0.5F, 16.0F, 7.0F);
            this.legFrontRight.setRotationPoint(-2.5F, 16.0F, -4.0F);
            this.legFrontLeft.setRotationPoint(0.5F, 16.0F, -4.0F);
            this.legBackRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.legBackLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
            this.legFrontRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
            this.legFrontLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            
            this.wolfHeadMain.setRotationPoint(-1.0F, 13.5F, -7.0F);
            this.legFrontRight.rotateAngleY = 0;
            this.legFrontLeft.rotateAngleY = 0;
        }

        this.wolfHeadMain.rotateAngleZ = dog.getInterestedAngle(partialTickTime) + dog.getShakeAngle(partialTickTime, 0.0F);
        this.wolfMane.rotateAngleZ = dog.getShakeAngle(partialTickTime, -0.08F);
        this.wolfBody.rotateAngleZ = dog.getShakeAngle(partialTickTime, -0.16F);
        this.wolfTail.rotateAngleZ = dog.getShakeAngle(partialTickTime, -0.2F);
        
        if((dog.isSitting() || (dog.motionX == 0.0F && dog.motionZ == 0.0F)) && dog.getHealth() > 1) {
            float wagAngleY = dog.getWagAngle(partialTickTime, 0.0F);
            if(wagAngleY == 0.0F)
                wagAngleY = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.wolfTail.rotateAngleY = wagAngleY;
        }
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        this.wolfHeadMain.rotateAngleX = headPitch * 0.017453292F;
        this.wolfHeadMain.rotateAngleY = netHeadYaw * 0.017453292F;
        this.wolfTail.rotateAngleX = ageInTicks;
    }
}
