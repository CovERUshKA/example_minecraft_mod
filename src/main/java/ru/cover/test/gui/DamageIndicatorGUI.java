package ru.cover.test.gui;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.ibm.icu.math.BigDecimal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Nullable
@SideOnly(Side.CLIENT)
public class DamageIndicatorGUI extends Gui
{
	public static Entity LastEntity;
	public static long LastTime;
	public static int timeout = 1500;
	
	private static float round(float number, int decimalPlace) {
		BigDecimal bd = new BigDecimal(number);
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}
	
	private RayTraceResult getEntity()
	{
		Entity pointedEntity;
		RayTraceResult result = null;
		Minecraft mc = Minecraft.getMinecraft();
		Entity entity = mc.getRenderViewEntity();

        if (entity != null)
        {
            if (mc.world != null)
            {
                double d0 = (double)256;
                result = entity.rayTrace(d0, 1.0f);
                Vec3d vec3d = entity.getPositionEyes(1.0f);
                boolean flag = false;
                int i = 3;
                double d1 = d0;

                if (result != null)
                {
                    d1 = result.hitVec.distanceTo(vec3d);
                }

                Vec3d vec3d1 = entity.getLook(1.0F);
                Vec3d vec3d2 = vec3d.addVector(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0);
                pointedEntity = null;
                Vec3d vec3d3 = null;
                float f = 1.0F;
                List<Entity> list = mc.world.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().expand(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0).grow(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>()
                {
                    public boolean apply(@Nullable Entity p_apply_1_)
                    {
                        return p_apply_1_ != null && p_apply_1_.canBeCollidedWith();
                    }
                }));
                double d2 = d1;

                for (int j = 0; j < list.size(); ++j)
                {
                    Entity entity1 = list.get(j);
                    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow((double)entity1.getCollisionBorderSize());
                    RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);

                    if (axisalignedbb.contains(vec3d))
                    {
                        if (d2 >= 0.0D)
                        {
                            pointedEntity = entity1;
                            vec3d3 = raytraceresult == null ? vec3d : raytraceresult.hitVec;
                            d2 = 0.0D;
                        }
                    }
                    else if (raytraceresult != null)
                    {
                        double d3 = vec3d.distanceTo(raytraceresult.hitVec);

                        if (d3 < d2 || d2 == 0.0D)
                        {
                            if (entity1.getLowestRidingEntity() == entity.getLowestRidingEntity() && !entity1.canRiderInteract())
                            {
                                if (d2 == 0.0D)
                                {
                                    pointedEntity = entity1;
                                    vec3d3 = raytraceresult.hitVec;
                                }
                            }
                            else
                            {
                                pointedEntity = entity1;
                                vec3d3 = raytraceresult.hitVec;
                                d2 = d3;
                            }
                        }
                    }
                }

                if (pointedEntity != null && (d2 < d1 || result == null))
                {
                	result = new RayTraceResult(pointedEntity, vec3d3);
                }
            }
        }
        
        return result;
	}
	
	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent event)
	{
		if (!event.isCancelable() && event.getType() == ElementType.EXPERIENCE)
		{
			Minecraft mc = Minecraft.getMinecraft();
	       
			RayTraceResult result = getEntity();
			if(result != null && result.entityHit != null && LastEntity != result.entityHit)
			{
				if(!result.entityHit.getClass().getName().contains("item"))
				{
					LastEntity = (EntityLivingBase)result.entityHit;
					LastTime = System.currentTimeMillis();
				}
			}
			else if (result != null && result.entityHit != null && LastEntity == result.entityHit)
			{
				LastTime = System.currentTimeMillis();
			}
			
			if(LastEntity != null && (System.currentTimeMillis() - LastTime) > timeout)
			{
				LastEntity = null;
			}

			if(LastEntity != null && LastEntity.isEntityAlive() && LastEntity.canBeAttackedWithItem())
			{
				EntityLivingBase ent = (EntityLivingBase)LastEntity;
				
				FontRenderer fontRenderer = mc.ingameGUI.getFontRenderer();
				String health = null;
				if(ent.getHealth() - (int)ent.getHealth() == 0.0f)
				{
					health = (int)ent.getHealth() + "/" + (int)ent.getMaxHealth();
				}
				else
				{
					health = round(ent.getHealth(), 1) + "/" + (int)ent.getMaxHealth();
				}
				
				mc.ingameGUI.drawRect(50, 46 - fontRenderer.FONT_HEIGHT, 120, 50, 0xFF444444);
				mc.ingameGUI.getFontRenderer().drawString(ent.getName(), 85 - fontRenderer.getStringWidth(ent.getName()) / 2, 48 - fontRenderer.FONT_HEIGHT, 0xFFFFFFFF);
				
				mc.ingameGUI.drawRect(50, 50, 120, 60, 0xFFAA0000);
				mc.ingameGUI.drawRect(50, 50, (int)( 50 + (70 / ent.getMaxHealth()) * ent.getHealth()), 60, 0xFF00AA00);
				mc.ingameGUI.getFontRenderer().drawString(health, 85 - fontRenderer.getStringWidth(health) / 2, 55 - fontRenderer.FONT_HEIGHT / 2, 0xFFFFFFFF);
			}
		}
	}
	
}
