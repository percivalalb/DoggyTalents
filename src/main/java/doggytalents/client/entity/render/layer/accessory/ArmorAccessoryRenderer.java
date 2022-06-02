package doggytalents.client.entity.render.layer.accessory;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import doggytalents.DoggyAccessoryTypes;
import doggytalents.api.inferface.IColoredObject;
import doggytalents.api.registry.AccessoryInstance;
import doggytalents.client.ClientSetup;
import doggytalents.client.entity.model.DogModel;
import doggytalents.common.entity.Dog;
import doggytalents.common.entity.accessory.ArmourAccessory;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class ArmorAccessoryRenderer extends RenderLayer<Dog, DogModel<Dog>> {

    private DogModel model;

    public ArmorAccessoryRenderer(RenderLayerParent parentRenderer, EntityRendererProvider.Context ctx) {
        super(parentRenderer);
        this.model = new DogModel(ctx.bakeLayer(ClientSetup.DOG_ARMOR));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Dog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        // Only show armour if dog is tamed or visible
        if (!dog.isTame() || dog.isInvisible()) {
            return;
        }

        this.getParentModel().copyPropertiesTo(this.model);
        this.model.prepareMobModel(dog, limbSwing, limbSwingAmount, partialTicks);
        this.model.setupAnim(dog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        for (AccessoryInstance accessoryInst : dog.getAccessories()) {
            if (accessoryInst instanceof ArmourAccessory.Instance armorInst) {

                this.model.setVisible(false);

                if (accessoryInst.ofType(DoggyAccessoryTypes.FEET)) {
                    this.model.legBackLeft.visible = true;
                    this.model.legBackRight.visible = true;
                    this.model.legFrontLeft.visible = true;
                    this.model.legFrontRight.visible = true;
                } else if (accessoryInst.ofType(DoggyAccessoryTypes.HEAD)) {
                    this.model.head.visible = true;
                } else if (accessoryInst.ofType(DoggyAccessoryTypes.CLOTHING)) {
                    this.model.body.visible = true;
                    this.model.mane.visible = true;
                } else if (accessoryInst.ofType(DoggyAccessoryTypes.TAIL)) {
                    this.model.tail.visible = true;
                }

                if (accessoryInst instanceof IColoredObject) {
                    float[] color = ((IColoredObject) armorInst).getColor();
                    this.renderArmorCutout(this.model, armorInst.getModelTexture(dog), poseStack, buffer, packedLight, dog, color[0], color[1], color[2], armorInst.hasEffect());
                } else {
                    this.renderArmorCutout(this.model, armorInst.getModelTexture(dog), poseStack, buffer, packedLight, dog, 1.0F, 1.0F, 1.0F, armorInst.hasEffect());
                }
            }
        }
    }

    public static <T extends LivingEntity> void renderArmorCutout(EntityModel<T> modelIn, ResourceLocation textureLocationIn, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entityIn, float red, float green, float blue, boolean enchanted) {
        VertexConsumer ivertexbuilder = ItemRenderer.getArmorFoilBuffer(bufferIn, RenderType.armorCutoutNoCull(textureLocationIn), false, enchanted);
        modelIn.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
    }
}
