package doggytalents.client.render;

import doggytalents.client.model.ModelDTDoggy;
import doggytalents.entity.EntityDTDoggy;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

public class RenderDTDoggy extends RenderLiving
{
    public RenderDTDoggy()
    {
        super(new ModelDTDoggy(), 0.5F);
    }

    public void renderWolf(EntityDTDoggy entitywolf, double d, double d1, double d2, float f, float f1)
    {
        super.doRenderLiving(entitywolf, d, d1, d2, f, f1);
    }

    protected float func_25004_a(EntityDTDoggy entitywolf, float f)
    {
        return entitywolf.setTailRotation();
    }

    protected void func_25006_b(EntityDTDoggy entitywolf, float f)
    {
    }

    protected void preRenderCallback(EntityLiving entityliving, float f)
    {
        func_25006_b((EntityDTDoggy)entityliving, f);
    }

    protected float handleRotationFloat(EntityLiving entityliving, float f)
    {
        return func_25004_a((EntityDTDoggy)entityliving, f);
    }

    public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2,
            float f, float f1)
    {
        renderWolf((EntityDTDoggy)entityliving, d, d1, d2, f, f1);
    }

    public void doRender(Entity entity, double d, double d1, double d2,
            float f, float f1)
    {
        renderWolf((EntityDTDoggy)entity, d, d1, d2, f, f1);
    }
}
