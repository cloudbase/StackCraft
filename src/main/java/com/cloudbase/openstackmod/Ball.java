package com.cloudbase.openstackmod;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Ball extends Item {


	public Ball(){
		
		this.setUnlocalizedName(OpenStackMod.MODID + "." + "ball");
		this.setCreativeTab(CreativeTabs.tabMisc);
		this.setMaxStackSize(1);
		
		GameRegistry.registerItem(this, "ball");
		
	}
	
	public void RegisterRenderer(String modelName){
		
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(OpenStackMod.MODID + ":" + "ball", "inventory"));
		
	}
	
}