package doggytalents.client.entity.model;

import com.google.common.collect.ImmutableList;
import doggytalents.common.entity.Dog;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;

public class BowTieModel extends ListModel<Dog> {

    public ModelPart rightChest;
    public ModelPart middleChest;
    public ModelPart leftChest;

    public BowTieModel(float scaleFactor) {
        // TODO
//        this.texHeight = 16;
//        this.texWidth = 16;
//        this.rightChest = new ModelPart(this, 52, 0);
//        this.rightChest.addBox(-2.0F, 1.5F, -9.0F, 1, 2, 1, scaleFactor);
//        this.rightChest.setPos(0.0F, 14.0F, 2.0F);
//        this.middleChest = new ModelPart(this, 52, 0);
//        this.middleChest.addBox(0.0F, 14.0F, -0.0F, 2, 1, 1, scaleFactor);
//        this.middleChest.setPos(-1.0F, 2.0F, -9.0F);//0.0F, 14.0F, 2.0F);
//        this.leftChest = new ModelPart(this, 52, 0);
//        this.leftChest.addBox(1.0F, 1.5F, -9.0F, 1, 2, 1, scaleFactor);
//        this.leftChest.setPos(0.0F, 14.0F, 2.0F);
    }

    @Override
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(this.rightChest, this.middleChest, this.leftChest);
    }

    @Override
    public void prepareMobModel(Dog dogIn, float limbSwing, float limbSwingAmount, float partialTickTime) {

        this.rightChest.xRot += 0.1;
        this.middleChest.xRot += 0.1;
        this.leftChest.xRot += 0.1;
        this.rightChest.zRot = dogIn.getShakeAngle(partialTickTime, -0.16F);
        this.leftChest.zRot = this.rightChest.zRot;
    }

    @Override
    public void setupAnim(Dog dogIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }
}
