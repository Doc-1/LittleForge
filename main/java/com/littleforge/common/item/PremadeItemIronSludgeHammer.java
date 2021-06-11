package com.littleforge.common.item;

import com.creativemd.creativecore.client.rendering.model.ICreativeRendered;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.common.structure.LittleStructure;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PremadeItemIronSludgeHammer extends PremadeItem implements ICreativeRendered {
	
	public PremadeItemIronSludgeHammer(String unlocalizedName, String registryNm) {
		super(unlocalizedName, registryNm);
		setCreativeTab(LittleTiles.littleTab);
	}
	
	public static void onLeftClickStructure(World world, EntityPlayer player, LittleStructure structure) {
		System.out.println("sa");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void applyCustomOpenGLHackery(ItemStack stack, TransformType cameraTransformType) {
		Minecraft mc = Minecraft.getMinecraft();
		
		if (cameraTransformType == TransformType.FIRST_PERSON_RIGHT_HAND) {
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			GlStateManager.translate(-0.07D, -0.3D, -0.07D);
			
			GlStateManager.rotate(-140.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(15.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
			
			//GlStateManager.scale(1.0D, 1.0D, 1.0D);
		}
		
		if (cameraTransformType == TransformType.GUI) {
			GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(6.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(5.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.translate(-1.3D, -14.0D, -0.02D);
			GlStateManager.scale(1.4D, 1.4D, 1.4D);
		}
		
	}
	
}
