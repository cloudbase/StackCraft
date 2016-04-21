package com.cloudbase.openstackmod.block;

import java.util.Random;

import com.cloudbase.openstackmod.OpenStackMod;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class OpenStackness extends Block{
	
	public static final String NAME = "OpenStackness";
	public static OpenStackness instance = null;
	
	public static void init(){
		GameRegistry.registerBlock(instance, instance.NAME);
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(instance), 0, new ModelResourceLocation(OpenStackMod.MODID+":"+instance.NAME,"inventory"));
		
		GameRegistry.addRecipe(new ItemStack(instance), new Object[]{
		    	"RRR",
		    	"R R",
		    	"RRR",
		    	'R', Blocks.redstone_block
		    	});
		
		instance.setUnlocalizedName(OpenStackMod.MODID + "_" + instance.NAME);
		instance.setCreativeTab(CreativeTabs.tabBlock);
		instance.setHardness(3);
	}

	public OpenStackness() {
		super(Material.ground);
	}
	
	public Item getItemDropped(int metadata, Random random, int fortune){
		return Item.getItemFromBlock(OpenStackness.instance);
	}
	
	@Override
	public boolean isFullCube(){
		return false;
	}
	
	@Override
	public boolean isFullBlock(){
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(){
		return false;
	}
}