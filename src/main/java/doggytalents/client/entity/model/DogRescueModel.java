package doggytalents.client.entity.model;

import com.google.common.collect.ImmutableList;
import doggytalents.common.entity.Dog;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class DogRescueModel extends ListModel<Dog>{

    public ModelPart rescueBox;

    public DogRescueModel(ModelPart box) {
        this.rescueBox = box.getChild("rescue_box");
//        this.rescueBox = new ModelPart(this, 0, 0);
//        this.rescueBox.addBox(-1F, -4F, -4.5F, 4, 2, 2);
//        this.rescueBox.setPos(-1F, 14F, -3F);
//        this.rescueBox.xRot = (float) (Math.PI / 2);
    }

    public static LayerDefinition createRescueBoxLayer() {
        MeshDefinition var0 = new MeshDefinition();
        PartDefinition var1 = var0.getRoot();

        var1.addOrReplaceChild("rescue_box", CubeListBuilder.create().texOffs(0, 0).addBox(-1F, -4F, -4.5F, 4, 2, 2), PartPose.offsetAndRotation(-1F, 14F, -3F, (float) (Math.PI / 2), 0F, 0F));

        return LayerDefinition.create(var0, 64, 32);
    }

    @Override
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(this.rescueBox);
    }

    @Override
    public void prepareMobModel(Dog dogIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        if (dogIn.isInSittingPose()) {
            if (dogIn.isLying()) {
                this.rescueBox.setPos(-1F, 20F, -2F);
                this.rescueBox.xRot = (float) (Math.PI / 2);
            }
            else  {
                this.rescueBox.setPos(-1, 16, -3);
                this.rescueBox.xRot = (float) (Math.PI * 2 / 5);
            }
        }
        else {
            this.rescueBox.setPos(-1F, 14F, -3F);
            this.rescueBox.xRot = (float) (Math.PI / 2);
        }
    }

    @Override
    public void setupAnim(Dog entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }
}
