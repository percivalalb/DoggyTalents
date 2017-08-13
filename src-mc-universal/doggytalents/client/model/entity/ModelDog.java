package doggytalents.client.model.entity;

import doggytalents.base.ObjectLib;
import doggytalents.entity.EntityDog;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
@SideOnly(Side.CLIENT)
public class ModelDog extends ModelBase {
	
	public ModelRenderer wolfHeadMain;
    public ModelRenderer wolfHeadMainBone;
    public ModelRenderer wolfBody;
    public ModelRenderer wolfBodyChest;
    public ModelRenderer wolfBodySaddle;
    public ModelRenderer wolfBodySaddleChest;
    public ModelRenderer wolfLeg1;
    public ModelRenderer wolfLeg2;
    public ModelRenderer wolfLeg3;
    public ModelRenderer wolfLeg4;
    public ModelRenderer wolfTail;
    public ModelRenderer wolfMane;
	
	public ModelDog(float scaleFactor) {
        float f1 = 13.5F;
		
		//Head
        this.wolfHeadMain = new ModelRenderer(this, 0, 0);
        this.wolfHeadMain.addBox(-2.0F, -3.0F, -2.0F, 6, 6, 4, scaleFactor);
        this.wolfHeadMain.setRotationPoint(-1.0F, f1, -7.0F);
		
		//Bone
        this.wolfHeadMainBone = new ModelRenderer(this, 0, 0);
        this.wolfHeadMainBone.addBox(-2.0F, -3.0F, -2.0F, 6, 6, 4, scaleFactor);
        this.wolfHeadMainBone.setRotationPoint(-1.0F, f1, -7.0F);
	
		//Body
        this.wolfBody = new ModelRenderer(this, 18, 14);
        this.wolfBody.addBox(-3.0F, -2.0F, -3.0F, 6, 9, 6, scaleFactor);
        this.wolfBody.setRotationPoint(0.0F, 14.0F, 2.0F);
      
		//BodyChest
        this.wolfBodyChest = new ModelRenderer(this, 18, 14);
        this.wolfBodyChest.addBox(-3.0F, -2.0F, -3.0F, 6, 9, 6, scaleFactor);
        this.wolfBodyChest.setRotationPoint(0.0F, 14.0F, 2.0F);
        
        //BodySaddle
        this.wolfBodySaddle = new ModelRenderer(this, 18, 14);
        this.wolfBodySaddle.addBox(-3.0F, -2.0F, -3.0F, 6, 9, 6, scaleFactor);
        this.wolfBodySaddle.setRotationPoint(0.0F, 14.0F, 2.0F);
        
        //BodySaddleChest
        this.wolfBodySaddleChest = new ModelRenderer(this, 18, 14);
        this.wolfBodySaddleChest.addBox(-3.0F, -2.0F, -3.0F, 6, 9, 6, scaleFactor);
        this.wolfBodySaddleChest.setRotationPoint(0.0F, 14.0F, 2.0F);        
        
		//Mane
        this.wolfMane = new ModelRenderer(this, 21, 0);
        this.wolfMane.addBox(-3.0F, -3.0F, -3.0F, 8, 6, 7, scaleFactor);
        this.wolfMane.setRotationPoint(-1.0F, 14.0F, 2.0F);
        
		//Limbs
        this.wolfLeg1 = new ModelRenderer(this, 0, 18);
        this.wolfLeg1.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, scaleFactor);
        this.wolfLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
        this.wolfLeg2 = new ModelRenderer(this, 0, 18);
        this.wolfLeg2.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, scaleFactor);
        this.wolfLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
        this.wolfLeg3 = new ModelRenderer(this, 0, 18);
        this.wolfLeg3.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, scaleFactor);
        this.wolfLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
        this.wolfLeg4 = new ModelRenderer(this, 0, 18);
        this.wolfLeg4.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, scaleFactor);
        this.wolfLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
        
		//Tail1
        this.wolfTail = new ModelRenderer(this, 9, 18);
        this.wolfTail.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, scaleFactor);
        this.wolfTail.setRotationPoint(-1.0F, 12.0F, 8.0F);              

		//Tail2
        this.wolfTail.setTextureOffset(45, 0).addBox(0.0F, 0.0F, 0.0F, 2, 3, 1).setRotationPoint(90.0F, 0.0F, 0.0F);
        
		//Chests
        this.wolfBodyChest.setTextureOffset(52, 0).addBox(2.0F, -1F, 0F, 2, 7, 4);//Backpack1
        this.wolfBodyChest.setTextureOffset(52, 0).addBox(-4.0F, -1F, 0F, 2, 7, 4);//Backpack1                
    	
        //Saddles
        this.wolfBodySaddle.setTextureOffset(52, 11).addBox(-2.5F, 0F, 3F, 5, 6, 1);//Saddle1
        this.wolfBodySaddle.setTextureOffset(52, 18).addBox(-2.0F, 0F, 3.5F, 4, 1, 1);//Saddle2
        this.wolfBodySaddle.setTextureOffset(52, 18).addBox(-2.0F, 5F, 3.5F, 4, 1, 1);//Saddle3

        //Chest+Saddles
        this.wolfBodySaddleChest.setTextureOffset(52, 0).addBox(3.0F, -1F, -2F, 2, 7, 4);//Backpack1
        this.wolfBodySaddleChest.setTextureOffset(52, 0).addBox(-5.0F, -1F, -2F, 2, 7, 4);//Backpack1                
        this.wolfBodySaddleChest.setTextureOffset(52, 11).addBox(-2.5F, 0F, 3F, 5, 6, 1);//Saddle1
        this.wolfBodySaddleChest.setTextureOffset(52, 18).addBox(-2.0F, 0F, 3.5F, 4, 1, 1);//Saddle2
        this.wolfBodySaddleChest.setTextureOffset(52, 18).addBox(-2.0F, 5F, 3.5F, 4, 1, 1);//Saddle3

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
        
		//HeadBone EarsNormal
        this.wolfHeadMainBone.setTextureOffset(16, 14).addBox(-2.0F, -5.0F, 0.0F, 2, 2, 1, scaleFactor); //EarNormal1
        this.wolfHeadMainBone.setTextureOffset(16, 14).addBox(2.0F, -5.0F, 0.0F, 2, 2, 1, scaleFactor); //EarNormal2
        
		//HeadBone EarsBoni
        this.wolfHeadMainBone.setTextureOffset(42, 14).addBox(-3.0F, -3.0F, -1.5F, 1, 5, 3, scaleFactor);//EarBoni
        this.wolfHeadMainBone.setTextureOffset(42, 14).addBox(4.0F, -3.0F, -1.5F, 1, 5, 3, scaleFactor);//EarBoni
        
		//HeadBone EarsSmall
        this.wolfHeadMainBone.setTextureOffset(18, 0).addBox(-4.8F, -3.5F, -1.0F, 2, 1, 2, scaleFactor);//SmallEar1
        this.wolfHeadMainBone.setTextureOffset(18, 0).addBox(2.8F, -3.5F, -1.0F, 2, 1, 2, scaleFactor);//SmallEar2
        
		//HeadBone Nose
        this.wolfHeadMainBone.setTextureOffset(0, 10).addBox(-0.5F, 0.0F, -5.0F, 3, 3, 4, scaleFactor);//Nose moved
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        EntityDog dog = (EntityDog)entityIn;
        
        if(this.isChild) {
             GlStateManager.pushMatrix();
             GlStateManager.translate(0.0F, 5.0F * scale, 2.0F * scale);
             (dog.hasBone() ? this.wolfHeadMainBone : this.wolfHeadMain).renderWithRotation(scale);
             GlStateManager.popMatrix();
             GlStateManager.pushMatrix();
             GlStateManager.scale(0.5F, 0.5F, 0.5F);
             GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
             ModelRenderer bodyRender = this.wolfBody;
             
             if(dog.talents.getLevel("packpuppy") > 0) {
            	 if(dog.talents.getLevel("wolfmount") > 0)
            		 bodyRender = this.wolfBodySaddleChest;
            	 else
            		 bodyRender = this.wolfBodyChest;
             }
             else if(dog.talents.getLevel("wolfmount") > 0)
            	 bodyRender = this.wolfBodySaddle;
             
             
             bodyRender.render(scale);
             this.wolfBody.render(scale);
             this.wolfLeg1.render(scale);
             this.wolfLeg2.render(scale);
             this.wolfLeg3.render(scale);
             this.wolfLeg4.render(scale);
             this.wolfTail.renderWithRotation(scale);
             this.wolfMane.render(scale);
             GlStateManager.popMatrix();
        }
        else {
        	(dog.hasBone() ? this.wolfHeadMainBone : this.wolfHeadMain).renderWithRotation(scale);
        	ModelRenderer bodyRender = this.wolfBody;
            
            if(dog.talents.getLevel("packpuppy") > 0) {
           	 if(dog.talents.getLevel("wolfmount") > 0)
           		 bodyRender = this.wolfBodySaddleChest;
           	 else
           		 bodyRender = this.wolfBodyChest;
            }
            else if(dog.talents.getLevel("wolfmount") > 0)
           	 bodyRender = this.wolfBodySaddle;
            
            
            bodyRender.render(scale);
            this.wolfLeg1.render(scale);
            this.wolfLeg2.render(scale);
            this.wolfLeg3.render(scale);
            this.wolfLeg4.render(scale);
            this.wolfTail.renderWithRotation(scale);
            this.wolfMane.render(scale);
        }
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
    	EntityDog dog = (EntityDog)entitylivingbaseIn;

    	this.wolfTail.rotateAngleY = ObjectLib.BRIDGE.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

        if (dog.isSitting()) {
        	this.wolfMane.setRotationPoint(-1.0F, 16.0F, -3.0F);
            this.wolfMane.rotateAngleX = ((float)Math.PI * 2F / 5F);
            this.wolfMane.rotateAngleY = 0.0F;
            this.wolfBody.setRotationPoint(0.0F, 18.0F, 0.0F);
            this.wolfBody.rotateAngleX = ((float)Math.PI / 4F);
            this.wolfBodyChest.setRotationPoint(0.0F, 18.0F, 0.0F);
            this.wolfBodyChest.rotateAngleX = ((float)Math.PI / 4F);
            this.wolfBodySaddle.setRotationPoint(0.0F, 18.0F, 0.0F);
            this.wolfBodySaddle.rotateAngleX = ((float)Math.PI / 4F);
            this.wolfBodySaddleChest.setRotationPoint(0.0F, 18.0F, 0.0F);
            this.wolfBodySaddleChest.rotateAngleX = ((float)Math.PI / 4F);
            this.wolfTail.setRotationPoint(-1.0F, 21.0F, 6.0F);
            this.wolfLeg1.setRotationPoint(-2.5F, 22.0F, 2.0F);
            this.wolfLeg1.rotateAngleX = ((float)Math.PI * 3F / 2F);
            this.wolfLeg2.setRotationPoint(0.5F, 22.0F, 2.0F);
            this.wolfLeg2.rotateAngleX = ((float)Math.PI * 3F / 2F);
            this.wolfLeg3.rotateAngleX = 5.811947F;
            this.wolfLeg3.setRotationPoint(-2.49F, 17.0F, -4.0F);
            this.wolfLeg4.rotateAngleX = 5.811947F;
            this.wolfLeg4.setRotationPoint(0.51F, 17.0F, -4.0F);
        }
        else {
        	this.wolfBody.setRotationPoint(0.0F, 14.0F, 2.0F);
            this.wolfBody.rotateAngleX = ((float)Math.PI / 2F);
            this.wolfBodyChest.setRotationPoint(0.0F, 14.0F, 2.0F);
            this.wolfBodyChest.rotateAngleX = ((float)Math.PI / 2F);
            this.wolfBodySaddle.setRotationPoint(0.0F, 14.0F, 2.0F);
            this.wolfBodySaddle.rotateAngleX = ((float)Math.PI / 2F);
            this.wolfBodySaddleChest.setRotationPoint(0.0F, 14.0F, 2.0F);
            this.wolfBodySaddleChest.rotateAngleX = ((float)Math.PI / 2F);
            this.wolfMane.setRotationPoint(-1.0F, 14.0F, -3.0F);
            this.wolfMane.rotateAngleX = this.wolfBody.rotateAngleX;
            this.wolfTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
            this.wolfLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
            this.wolfLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
            this.wolfLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
            this.wolfLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
            this.wolfLeg1.rotateAngleX = ObjectLib.BRIDGE.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.wolfLeg2.rotateAngleX = ObjectLib.BRIDGE.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
            this.wolfLeg3.rotateAngleX = ObjectLib.BRIDGE.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
            this.wolfLeg4.rotateAngleX = ObjectLib.BRIDGE.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }

        this.wolfHeadMain.rotateAngleZ = dog.getInterestedAngle(partialTickTime) + dog.getShakeAngle(partialTickTime, 0.0F);
        this.wolfHeadMainBone.rotateAngleZ = this.wolfHeadMain.rotateAngleZ;
        this.wolfMane.rotateAngleZ = dog.getShakeAngle(partialTickTime, -0.08F);
        this.wolfBody.rotateAngleZ = dog.getShakeAngle(partialTickTime, -0.16F);
        this.wolfBodyChest.rotateAngleZ = this.wolfBody.rotateAngleZ;
        this.wolfBodySaddle.rotateAngleZ = this.wolfBody.rotateAngleZ;
        this.wolfBodySaddleChest.rotateAngleZ = this.wolfBody.rotateAngleZ;
        this.wolfTail.rotateAngleZ = dog.getShakeAngle(partialTickTime, -0.2F);
        
        if((dog.isSitting() || (dog.motionX == 0.0F && dog.motionZ == 0.0F)) && dog.getHealth() > 1) {
        	float wagAngleY = dog.getWagAngle(partialTickTime, 0.0F);
        	if(wagAngleY == 0.0F)
        		wagAngleY = ObjectLib.BRIDGE.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        	this.wolfTail.rotateAngleY = wagAngleY;
        }
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        this.wolfHeadMain.rotateAngleX = headPitch * 0.017453292F;
        this.wolfHeadMain.rotateAngleY = netHeadYaw * 0.017453292F;
        this.wolfHeadMainBone.rotateAngleX = this.wolfHeadMain.rotateAngleX;
        this.wolfHeadMainBone.rotateAngleY = this.wolfHeadMain.rotateAngleY;
        this.wolfTail.rotateAngleX = ageInTicks;
    }
}
