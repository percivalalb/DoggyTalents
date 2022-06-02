package doggytalents.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import doggytalents.DoggyTalents;
import doggytalents.api.registry.TalentInstance;
import doggytalents.client.ClientSetup;
import doggytalents.client.entity.model.DogModel;
import doggytalents.client.entity.model.DogRescueModel;
import doggytalents.common.entity.Dog;
import doggytalents.common.lib.Resources;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

import java.util.Optional;

public class RescueDogRenderer extends RenderLayer<Dog, DogModel<Dog>> {

    private DogRescueModel model;

    public RescueDogRenderer(RenderLayerParent parentRenderer, EntityRendererProvider.Context ctx) {
        super(parentRenderer);
        this.model = new DogRescueModel(ctx.bakeLayer(ClientSetup.DOG_RESCUE_BOX));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Dog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (dog.isInvisible()) {
            return;
        }

        Optional<TalentInstance> inst = dog.getTalent(DoggyTalents.RESCUE_DOG);
        if (inst.isPresent() && inst.get().level() >= 5) {
            this.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(dog, limbSwing, limbSwingAmount, partialTicks);
            this.model.setupAnim(dog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            RenderLayer.renderColoredCutoutModel(this.model, Resources.TALENT_RESCUE, poseStack, buffer, packedLight, dog, 1.0F, 1.0F, 1.0F);
        }

    }
}
