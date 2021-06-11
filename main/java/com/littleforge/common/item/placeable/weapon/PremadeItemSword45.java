package com.littleforge.common.item.placeable.weapon;

import java.util.List;

import javax.annotation.Nullable;

import com.creativemd.littletiles.LittleTiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PremadeItemSword45 extends PremadePlaceableItemWeapon {
	
	public PremadeItemSword45(ToolMaterial material, String unlocalizedName, String registryName, String premadeToRender, String premadeToPlace) {
		super(material, unlocalizedName, registryName, premadeToRender, premadeToPlace);
		setCreativeTab(LittleTiles.littleTab);
	}
	
	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		return 0F;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void applyCustomOpenGLHackery(ItemStack stack, TransformType cameraTransformType) {
		
		if (cameraTransformType == TransformType.FIRST_PERSON_RIGHT_HAND) {
			GlStateManager.scale(2D, 2D, 2D);
			GlStateManager.translate(-0.04D, .15D, -0D);
			
			GlStateManager.rotate(-140.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(15.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
			
			//GlStateManager.scale(1.0D, 1.0D, 1.0D);
		}
		
		if (cameraTransformType == TransformType.GUI)
			GlStateManager.scale(1.6D, 1.6D, 1.6D);
		
	}
}
