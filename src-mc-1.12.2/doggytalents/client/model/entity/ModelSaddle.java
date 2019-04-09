package doggytalents.client.model.entity;

import doggytalents.entity.EntityDog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class ModelSaddle extends ModelBase {
	
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
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		
		this.chest1.render(scale);         
		this.chest2.render(scale);
		this.chest3.render(scale);
	}
  
	@Override
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
		EntityDog dog = (EntityDog)entitylivingbaseIn;

		if (dog.isSitting()) {
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
		
		this.chest1.rotateAngleZ = dog.getShakeAngle(partialTickTime, -0.16F);
        this.chest2.rotateAngleZ = this.chest1.rotateAngleZ;
        this.chest3.rotateAngleZ = this.chest1.rotateAngleZ;
	}
  
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
	}  
}