package doggytalents.client.model.entity;

import org.lwjgl.input.Keyboard;

import doggytalents.entity.EntityDog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.SearchTreeManager.Key;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.settings.KeyBindingMap;

public class ModelWings extends ModelBase {
	
	public ModelRenderer WingA;
	public ModelRenderer WingB;
  
	public ModelWings() {   
		WingA = new ModelRenderer(this, 50, 14);
		WingA.addBox(-3F, -3F, 0F, 6, 17, 1);
		WingA.setRotationPoint(2F, 10F, -2F);
		this.setRotation(WingA, 1.570796F, 0F, 0F);     
      
		WingB = new ModelRenderer(this, 50, 14);
		WingB.mirror = true;
		WingB.addBox(-3F, -3F, 0F, 6, 17, 1);
		WingB.setRotationPoint(-2F, 10F, -2F);
		this.setRotation(WingB, 1.570796F, 0F, 0F);     
	}
  
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

		WingA.render(scale);         
		WingB.render(scale);
	}
  
	@Override
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
		EntityDog dog = (EntityDog)entitylivingbaseIn;

		if(dog.isSitting()) {
			this.WingA.setRotationPoint(2F, 12.0F, -2.0F);
			setRotation(WingA, ((float)Math.PI * 2F / 5F), 0F, 0F);          
			this.WingB.setRotationPoint(-2F, 12.0F, -2.0F);
			setRotation(WingB, ((float)Math.PI * 2F / 5F), 0F, 0F);
		} 	
		else {
			WingA.setRotationPoint(2F, 10F, -2F);
			setRotation(WingA, 1.570796F, 0F, 0F);
			WingB.setRotationPoint(-2F, 10F, -2F);
			setRotation(WingB, 1.570796F, 0F, 0F); 
    	  
			if(!dog.onGround) {
				if(!Minecraft.getMinecraft().gameSettings.keyBindBack.isKeyDown()) {
					float c = 4.0F;
					WingA.rotateAngleY = 1.570796F - (float) ((Math.atan(Math.abs(dog.motionX*c)+Math.abs(dog.motionZ*c))));
					WingB.rotateAngleY = -1.570796F - (float) -((Math.atan(Math.abs(dog.motionX*c)+Math.abs(dog.motionZ*c))));
				} 
				else {
					float c2 = 0.5F;
					WingA.rotateAngleY = 1.570796F - (float) ((Math.atan(Math.abs(dog.motionX*c2)+Math.abs(dog.motionZ*c2))));
					WingB.rotateAngleY = -1.570796F - (float) -((Math.atan(Math.abs(dog.motionX*c2)+Math.abs(dog.motionZ*c2))));
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