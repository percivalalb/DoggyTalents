package doggytalents.client.entity.render.layer.accessory;

import com.mojang.blaze3d.vertex.PoseStack;

import com.mojang.blaze3d.vertex.VertexConsumer;
import doggytalents.DoggyAccessories;
import doggytalents.DoggyAccessoryTypes;
import doggytalents.api.client.render.IAccessoryRenderer;
import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.inferface.IColoredObject;
import doggytalents.api.registry.AccessoryInstance;
import doggytalents.client.entity.model.DogModel;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.entity.accessory.ArmourAccessory;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;

public class ArmorAccessoryRenderer implements IAccessoryRenderer<DogEntity> {

    private final DogModel<DogEntity> model;
    private ResourceLocation texture;

    public ArmorAccessoryRenderer(ResourceLocation textureIn) {
        this.model = new DogModel<>(0.4F);
        this.texture = textureIn;
    }

    @Override
    public void render(RenderLayer<DogEntity, EntityModel<DogEntity>> layer, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, DogEntity dog, AccessoryInstance data, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (dog.isTame() && !dog.isInvisible()) {
            ArmourAccessory.Instance armorInstance = data.cast(ArmourAccessory.Instance.class);
            layer.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(dog, limbSwing, limbSwingAmount, partialTicks);
            this.model.setupAnim(dog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            this.model.setVisible(false);

            if (armorInstance.ofType(DoggyAccessoryTypes.FEET)) {
                this.model.legBackLeft.visible = true;
                this.model.legBackRight.visible = true;
                this.model.legFrontLeft.visible = true;
                this.model.legFrontRight.visible = true;
            } else if (armorInstance.ofType(DoggyAccessoryTypes.HEAD)) {
                this.model.head.visible = true;
            } else if (armorInstance.ofType(DoggyAccessoryTypes.CLOTHING)) {
                this.model.body.visible = true;
                this.model.mane.visible = true;
            } else if (armorInstance.ofType(DoggyAccessoryTypes.TAIL)) {
                this.model.tail.visible = true;
            }

            if (armorInstance instanceof IColoredObject) {
                float[] color = ((IColoredObject) armorInstance).getColor();
                this.renderArmorCutout(this.model, this.getTexture(dog, data), matrixStackIn, bufferIn, packedLightIn, dog, color[0], color[1], color[2], armorInstance.hasEffect());
            } else {

                this.renderArmorCutout(this.model, this.getTexture(dog, data), matrixStackIn, bufferIn, packedLightIn, dog, 1.0F, 1.0F, 1.0F, armorInstance.hasEffect());
            }
        }
    }

    public static <T extends LivingEntity> void renderArmorCutout(EntityModel<T> modelIn, ResourceLocation textureLocationIn, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entityIn, float red, float green, float blue, boolean enchanted) {
        VertexConsumer ivertexbuilder = ItemRenderer.getArmorFoilBuffer(bufferIn, RenderType.armorCutoutNoCull(textureLocationIn), false, enchanted);
        modelIn.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
    }

    public <T extends AbstractDogEntity> ResourceLocation getTexture(T dog, AccessoryInstance data) {
        return this.texture;
    }
}
