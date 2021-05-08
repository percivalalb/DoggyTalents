package doggytalents.client.entity.render.layer.accessory;

import com.mojang.blaze3d.matrix.MatrixStack;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import doggytalents.DoggyAccessories;
import doggytalents.DoggyAccessoryTypes;
import doggytalents.api.client.render.IAccessoryRenderer;
import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.inferface.IColoredObject;
import doggytalents.api.registry.AccessoryInstance;
import doggytalents.client.entity.model.DogModel;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.entity.accessory.ArmourAccessory;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

public class ArmorAccessoryRenderer implements IAccessoryRenderer<DogEntity> {

    private final DogModel<DogEntity> model;
    private ResourceLocation texture;

    public ArmorAccessoryRenderer(ResourceLocation textureIn) {
        this.model = new DogModel<>(0.4F);
        this.texture = textureIn;
    }

    @Override
    public void render(LayerRenderer<DogEntity, EntityModel<DogEntity>> layer, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, DogEntity dog, AccessoryInstance data, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (dog.isTamed() && !dog.isInvisible()) {
            ArmourAccessory.Instance armorInstance = data.cast(ArmourAccessory.Instance.class);
            layer.getEntityModel().copyModelAttributesTo(this.model);
            this.model.setLivingAnimations(dog, limbSwing, limbSwingAmount, partialTicks);
            this.model.setRotationAngles(dog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            this.model.setVisible(false);

            if (armorInstance.ofType(DoggyAccessoryTypes.FEET)) {
                this.model.legBackLeft.showModel = true;
                this.model.legBackRight.showModel = true;
                this.model.legFrontLeft.showModel = true;
                this.model.legFrontRight.showModel = true;
            } else if (armorInstance.ofType(DoggyAccessoryTypes.HEAD)) {
                this.model.head.showModel = true;
            } else if (armorInstance.ofType(DoggyAccessoryTypes.CLOTHING)) {
                this.model.body.showModel = true;
                this.model.mane.showModel = true;
            } else if (armorInstance.ofType(DoggyAccessoryTypes.TAIL)) {
                this.model.tail.showModel = true;
            }

            if (armorInstance instanceof IColoredObject) {
                float[] color = ((IColoredObject) armorInstance).getColor();
                this.renderArmorCutout(this.model, this.getTexture(dog, data), matrixStackIn, bufferIn, packedLightIn, dog, color[0], color[1], color[2], armorInstance.hasEffect());
            } else {

                this.renderArmorCutout(this.model, this.getTexture(dog, data), matrixStackIn, bufferIn, packedLightIn, dog, 1.0F, 1.0F, 1.0F, armorInstance.hasEffect());
            }
        }
    }

    public static <T extends LivingEntity> void renderArmorCutout(EntityModel<T> modelIn, ResourceLocation textureLocationIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entityIn, float red, float green, float blue, boolean enchanted) {
        IVertexBuilder ivertexbuilder = ItemRenderer.getArmorVertexBuilder(bufferIn, RenderType.getArmorCutoutNoCull(textureLocationIn), false, enchanted);
        modelIn.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
    }

    public <T extends AbstractDogEntity> ResourceLocation getTexture(T dog, AccessoryInstance data) {
        return this.texture;
    }
}
