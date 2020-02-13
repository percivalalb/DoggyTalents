package doggytalents.client.model.entity;

import com.google.common.collect.ImmutableList;

import doggytalents.entity.EntityDog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelWings extends SegmentedModel<EntityDog> {

    public ModelRenderer wingA;
    public ModelRenderer wingB;

    public ModelWings() {
        this.wingA = new ModelRenderer(this, 50, 14);
        this.wingA.addBox(-3F, -3F, 0F, 6, 17, 1);
        this.wingA.setRotationPoint(2F, 10F, -2F);
        this.setRotation(this.wingA, 1.570796F, 0F, 0F);

        this.wingB = new ModelRenderer(this, 50, 14);
        this.wingB.mirror = true;
        this.wingB.addBox(-3F, -3F, 0F, 6, 17, 1);
        this.wingB.setRotationPoint(-2F, 10F, -2F);
        this.setRotation(this.wingB, 1.570796F, 0F, 0F);
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.wingA, this.wingB);
    }

    @Override
    public void setLivingAnimations(EntityDog dogIn, float limbSwing, float limbSwingAmount, float partialTick) {
        super.setLivingAnimations(dogIn, limbSwing, limbSwingAmount, partialTick);
        if(dogIn.isSitting()) {
            this.wingA.setRotationPoint(2F, 12.0F, -2.0F);
            this.setRotation(this.wingA, ((float)Math.PI * 2F / 5F), 0F, 0F);
            this.wingB.setRotationPoint(-2F, 12.0F, -2.0F);
            this.setRotation(this.wingB, ((float)Math.PI * 2F / 5F), 0F, 0F);
        }
        else {
            this.wingA.setRotationPoint(2F, 10F, -2F);
            this.setRotation(this.wingA, 1.570796F, 0F, 0F);
            this.wingB.setRotationPoint(-2F, 10F, -2F);
            this.setRotation(this.wingB, 1.570796F, 0F, 0F);

            if(!dogIn.onGround) {
                if(!Minecraft.getInstance().gameSettings.keyBindBack.isKeyDown()) {
                    float c = 4.0F;
                    this.wingA.rotateAngleY = 1.570796F - (float) ((Math.atan(Math.abs(dogIn.getMotion().getX()*c)+Math.abs(dogIn.getMotion().getZ()*c))));
                    this.wingB.rotateAngleY = -1.570796F - (float) -((Math.atan(Math.abs(dogIn.getMotion().getX()*c)+Math.abs(dogIn.getMotion().getZ()*c))));
                }
                else {
                    float c2 = 0.5F;
                    this.wingA.rotateAngleY = 1.570796F - (float) ((Math.atan(Math.abs(dogIn.getMotion().getX()*c2)+Math.abs(dogIn.getMotion().getZ()*c2))));
                    this.wingB.rotateAngleY = -1.570796F - (float) -((Math.atan(Math.abs(dogIn.getMotion().getX()*c2)+Math.abs(dogIn.getMotion().getZ()*c2))));
                }
            }
        }
    }

    @Override
    public void setRotationAngles(EntityDog dogIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

        /*Wing1.rotateAngleY = -1.48353F;
        Wing2.rotateAngleY = 1.48353F; */
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}