package doggytalents.client.model.entity;

import doggytalents.ModBlocks;
import doggytalents.entity.EntityDog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;

public class ModelTalentAccesories extends ModelBase {
	
	public ModelRenderer saddle;
	public ModelRenderer chestOnly;
	public ModelRenderer chestSaddle;
	public ModelRenderer wingA;
	public ModelRenderer wingB;
	public ModelRenderer Fish;
	public ModelRenderer Rescue;
  
	public ModelTalentAccesories() {  
		//Saddle
        this.saddle = new ModelRenderer(this, 18, 14);
        this.saddle.setTextureOffset(0, 11).addBox(-2.5F, 0F, 3F, 5, 6, 1);
        this.saddle.setTextureOffset(0, 18).addBox(-2.0F, 0F, 3.5F, 4, 1, 1);
        this.saddle.setTextureOffset(0, 18).addBox(-2.0F, 5F, 3.5F, 4, 1, 1);
        this.saddle.setRotationPoint(0.0F, 14.0F, 2.0F);
        //Chest only
        this.chestOnly = new ModelRenderer(this, 18, 14);
        this.chestOnly.setTextureOffset(0, 0).addBox(2.0F, -1F, 0F, 2, 7, 4);
        this.chestOnly.setTextureOffset(0, 0).addBox(-4.0F, -1F, 0F, 2, 7, 4);
        this.chestOnly.setRotationPoint(0.0F, 14.0F, 2.0F);
        //Chest when have Saddle too
        this.chestSaddle = new ModelRenderer(this, 18, 14);
        this.chestSaddle.setTextureOffset(0, 0).addBox(3.0F, -1F, -2F, 2, 7, 4);
        this.chestSaddle.setTextureOffset(0, 0).addBox(-5.0F, -1F, -2F, 2, 7, 4);
        this.chestSaddle.setRotationPoint(0.0F, 14.0F, 2.0F);  
        //Wings
        this.wingA = new ModelRenderer(this, 50, 14);
		this.wingA.addBox(-3F, -3F, 0F, 6, 17, 1);
		this.wingA.setRotationPoint(2F, 10F, -2F);
		this.setRotation(this.wingA, 1.570796F, 0F, 0F);
		this.wingB = new ModelRenderer(this, 50, 14);
		this.wingB.mirror = true;
		this.wingB.addBox(-3F, -3F, 0F, 6, 17, 1);
		this.wingB.setRotationPoint(-2F, 10F, -2F);
		this.setRotation(this.wingB, 1.570796F, 0F, 0F);   
		//
		Fish = new ModelRenderer(this, 12, 0);
	    Fish.addBox(1F, -3F, 4F, 0, 6, 6);
	    Fish.setRotationPoint(-1F, 14F, -3F);
	    setRotation(Fish, 1.570796F, 0F, 0F);
	    
		Rescue = new ModelRenderer(this, 0, 20);
		Rescue.addBox(-1F, -4F, -4.5F, 4, 2, 2);
		Rescue.setRotationPoint(-1F, 14F, -3F);
		setRotation(Rescue, 1.570796F, 0F, 0F);
	}
  
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        EntityDog dog = (EntityDog)entityIn;
        
        if(dog.talents.getLevel("swimmerdog") > 4) {
			this.Fish.render(scale);
        }
        if(dog.talents.getLevel("rescuedog") > 0) {
			this.Rescue.render(scale);
        }
        if(dog.talents.getLevel("wolfmount") > 0) {
			this.saddle.render(scale);
        }
        if(dog.talents.getLevel("pillowpaw") > 4) {
        	this.wingA.render(scale);         
    		this.wingB.render(scale);
        }
		if(dog.talents.getLevel("packpuppy") > 0) {
			if(dog.talents.getLevel("wolfmount") > 0) {
				this.chestSaddle.render(scale);
			}
			else {
				this.chestOnly.render(scale);
			}
		}		
	}
  
	@Override
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
		EntityDog dog = (EntityDog)entitylivingbaseIn;
		
        if (dog.isSitting()) {
            boolean flag2 = dog.getOwner() != null && dog.getOwner().isPlayerSleeping();
            boolean flag3 = dog.world.getBlockState(dog.getPosition().down()).getBlock() == ModBlocks.DOG_BED || dog.world.getBlockState(dog.getPosition().down()).getBlock() == Blocks.BED;
            
        	if(flag2 || flag3) {
        		this.saddle.setRotationPoint(0.0F, 20F, 2.0F);
    			this.setRotation(this.saddle, 1.570796F, 0F, 0F);			
    			this.chestOnly.setRotationPoint(0.0F, 20F, 2.0F);
    			this.setRotation(this.chestOnly, 1.570796F, 0.0F, 0.0F);
    			this.chestSaddle.setRotationPoint(0.0F, 20F, 2.0F);
    			this.setRotation(this.chestSaddle, 1.570796F, 0.0F, 0.0F);
    			
    			Rescue.setRotationPoint(-1F, 20F, -2F);
    			this.setRotation(this.Rescue, 1.570796F, 0.0F, 0.0F);
    			Fish.setRotationPoint(-1F, 20F, -2F);
    			this.setRotation(this.Fish, 1.570796F, 0.0F, 0.0F);
        	}
        	else {
        		this.saddle.setRotationPoint(0F, 18.0F, 0.0F);
    			this.setRotation(this.saddle, ((float)Math.PI/ 4F), 0F, 0F); 			
    			this.chestOnly.setRotationPoint(0.0F, 18F, 0.0F);
    			this.setRotation(this.chestOnly, ((float)Math.PI/ 4F), 0.0F, 0.0F);
    			this.chestSaddle.setRotationPoint(0.0F, 18F, 0.0F);
    			this.setRotation(this.chestSaddle, ((float)Math.PI/ 4F), 0.0F, 0.0F);
    			
    			this.Rescue.setRotationPoint(-1, 16, -3);
    			this.setRotation(this.Rescue, ((float)Math.PI * 2F / 5F), 0.0F, 0.0F);
    			this.Fish.setRotationPoint(-1, 16, -3);
    			this.setRotation(this.Fish, ((float)Math.PI * 2F / 5F), 0.0F, 0.0F);    			
    			this.wingA.setRotationPoint(2F, 12.0F, -2.0F);
    			this.setRotation(this.wingA, ((float)Math.PI * 2F / 5F), 0F, 0F);          
    			this.wingB.setRotationPoint(-2F, 12.0F, -2.0F);
    			this.setRotation(this.wingB, ((float)Math.PI * 2F / 5F), 0F, 0F);		
        	}        	
        }
        else {
        	this.saddle.setRotationPoint(0.0F, 14F, 2.0F);
			this.setRotation(this.saddle, 1.570796F, 0F, 0F);			
			this.chestOnly.setRotationPoint(0.0F, 14F, 2.0F);
			this.setRotation(this.chestOnly, 1.570796F, 0.0F, 0.0F);
			this.chestSaddle.setRotationPoint(0.0F, 14F, 2.0F);
			this.setRotation(this.chestSaddle, 1.570796F, 0.0F, 0.0F);
			
			Rescue.setRotationPoint(-1F, 14F, -3F);
			this.setRotation(this.Rescue, 1.570796F, 0.0F, 0.0F);
			Fish.setRotationPoint(-1F, 14F, -3F);
			this.setRotation(this.Fish, 1.570796F, 0.0F, 0.0F);
			
			//Wing movement when flying
			this.wingA.setRotationPoint(2F, 10F, -2F);
			this.setRotation(this.wingA, 1.570796F, 0F, 0F);
			this.wingB.setRotationPoint(-2F, 10F, -2F);
			this.setRotation(this.wingB, 1.570796F, 0F, 0F);
			if(!dog.onGround) {
				if(!Minecraft.getMinecraft().gameSettings.keyBindBack.isKeyDown()) {
					float c = 4.0F;
					this.wingA.rotateAngleY = 1.570796F - (float) ((Math.atan(Math.abs(dog.motionX*c)+Math.abs(dog.motionZ*c))));
					this.wingB.rotateAngleY = -1.570796F - (float) -((Math.atan(Math.abs(dog.motionX*c)+Math.abs(dog.motionZ*c))));
				} 
				else {
					float c2 = 0.5F;
					this.wingA.rotateAngleY = 1.570796F - (float) ((Math.atan(Math.abs(dog.motionX*c2)+Math.abs(dog.motionZ*c2))));
					this.wingB.rotateAngleY = -1.570796F - (float) -((Math.atan(Math.abs(dog.motionX*c2)+Math.abs(dog.motionZ*c2))));
				}
			}
        }
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}