package doggytalents.client.model.entity;

import doggytalents.entity.EntityDog;
import net.minecraft.client.renderer.entity.model.ModelBase;
import net.minecraft.client.renderer.entity.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelChest extends ModelBase {
	
    public ModelRenderer chest1;
    public ModelRenderer chest2;
    
	public ModelChest(float scaleFactor) {   
		this.chest1 = new ModelRenderer(this, 52, 0);
		this.chest1.addBox(2.0F, -1F, 0F, 2, 7, 4, scaleFactor);
		this.chest1.setRotationPoint(0.0F, 14.0F, 2.0F);
		this.chest2 = new ModelRenderer(this, 52, 0);
		this.chest2.addBox(-4.0F, -1F, 0F, 2, 7, 4, scaleFactor);
		this.chest2.setRotationPoint(0.0F, 14.0F, 2.0F);   
	}
  
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		
		this.chest1.render(scale);         
		this.chest2.render(scale);
	}
  
	@Override
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
		EntityDog dog = (EntityDog)entitylivingbaseIn;

		if (dog.isSitting()) {
            this.chest1.setRotationPoint(0.0F, 18.0F, 0.0F);
            this.chest1.rotateAngleX = ((float)Math.PI / 4F);
            this.chest2.setRotationPoint(0.0F, 18.0F, 0.0F);
            this.chest2.rotateAngleX = ((float)Math.PI / 4F);
        }
        else {
            this.chest1.setRotationPoint(0.0F, 14.0F, 2.0F);
            this.chest1.rotateAngleX = ((float)Math.PI / 2F);
            this.chest2.setRotationPoint(0.0F, 14.0F, 2.0F);
            this.chest2.rotateAngleX = ((float)Math.PI / 2F);
           
        }
		
        this.chest1.rotateAngleZ = dog.getShakeAngle(partialTickTime, -0.16F);
        this.chest2.rotateAngleZ = this.chest1.rotateAngleZ;
	}
  
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
	}  
}