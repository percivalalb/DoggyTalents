package doggytalents.client.model.entity;

import org.lwjgl.input.Keyboard;

import doggytalents.entity.EntityDog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class ModelWings extends ModelBase {
	
	public ModelRenderer wingA;
	public ModelRenderer wingB;
  
	public ModelWings() {   
		wingA = new ModelRenderer(this, 50, 14);
		wingA.addBox(-3F, -3F, 0F, 6, 17, 1);
		wingA.setRotationPoint(2F, 10F, -2F);
		this.setRotation(wingA, 1.570796F, 0F, 0F);     
      
		wingB = new ModelRenderer(this, 50, 14);
		wingB.mirror = true;
		wingB.addBox(-3F, -3F, 0F, 6, 17, 1);
		wingB.setRotationPoint(-2F, 10F, -2F);
		this.setRotation(wingB, 1.570796F, 0F, 0F);     
	}
  
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

		wingA.render(scale);         
		wingB.render(scale);
	}
  
	@Override
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
		EntityDog dog = (EntityDog)entitylivingbaseIn;

		if(dog.isSitting()) {
			this.wingA.setRotationPoint(2F, 12.0F, -2.0F);
			setRotation(wingA, ((float)Math.PI * 2F / 5F), 0F, 0F);          
			this.wingB.setRotationPoint(-2F, 12.0F, -2.0F);
			setRotation(wingB, ((float)Math.PI * 2F / 5F), 0F, 0F);
		} 	
		else {
			wingA.setRotationPoint(2F, 10F, -2F);
			setRotation(wingA, 1.570796F, 0F, 0F);
			wingB.setRotationPoint(-2F, 10F, -2F);
			setRotation(wingB, 1.570796F, 0F, 0F); 
    	  
			if(!dog.onGround) {
				if(!Minecraft.getMinecraft().gameSettings.keyBindBack.getIsKeyPressed()) {
					float c = 4.0F;
					wingA.rotateAngleY = 1.570796F - (float) ((Math.atan(Math.abs(dog.motionX*c)+Math.abs(dog.motionZ*c))));
					wingB.rotateAngleY = -1.570796F - (float) -((Math.atan(Math.abs(dog.motionX*c)+Math.abs(dog.motionZ*c))));
				} 
				else {
					float c2 = 0.5F;
					wingA.rotateAngleY = 1.570796F - (float) ((Math.atan(Math.abs(dog.motionX*c2)+Math.abs(dog.motionZ*c2))));
					wingB.rotateAngleY = -1.570796F - (float) -((Math.atan(Math.abs(dog.motionX*c2)+Math.abs(dog.motionZ*c2))));
				}
			}
		}
	}
  
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
    
		/*Wing1.rotateAngleY = -1.48353F;
		Wing2.rotateAngleY = 1.48353F; */   
	}  
  
	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}