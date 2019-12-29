package doggytalents.client.model.entity;

import com.google.common.collect.ImmutableList;

import doggytalents.entity.EntityDog;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelChest extends SegmentedModel<EntityDog> {

    public ModelRenderer chest1;
    public ModelRenderer chest2;

    public ModelChest(float scaleFactor) {
        this.chest1 = new ModelRenderer(this, 52, 0);
        this.chest1.func_228301_a_(2.0F, -1F, 0F, 2, 7, 4, scaleFactor); // 1.14 addBox
        this.chest1.setRotationPoint(0.0F, 14.0F, 2.0F);
        this.chest2 = new ModelRenderer(this, 52, 0);
        this.chest2.func_228301_a_(-4.0F, -1F, 0F, 2, 7, 4, scaleFactor); // 1.14 addBox
        this.chest2.setRotationPoint(0.0F, 14.0F, 2.0F);
    }

    @Override
    public Iterable<ModelRenderer> func_225601_a_() {
        return ImmutableList.of(this.chest1, this.chest2);
    }

    @Override
    public void setLivingAnimations(EntityDog dogIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        if(dogIn.isSitting()) {
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

        this.chest1.rotateAngleZ = dogIn.getShakeAngle(partialTickTime, -0.16F);
        this.chest2.rotateAngleZ = this.chest1.rotateAngleZ;
    }

    @Override
    public void func_225597_a_(EntityDog dogIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }
}