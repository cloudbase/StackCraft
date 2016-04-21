package com.cloudbase.openstackmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RDPItem extends Item{
	
	public static RDPItem instance = null;
	public static final String NAME = "RDPItem";
	
	public static void init(){
		GameRegistry.registerItem(RDPItem.instance, RDPItem.NAME);
		
		//instance.setTextureName(OpenStackMod.MODID+":"+RDPItem.NAME);
		//((Item)instance).set.func_111206_d("generic:genericItem");
		
		//Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(instance, 0, new ModelResourceLocation(OpenStackMod.MODID+":"+NAME, "inventory"));
		//Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(instance, 0, new ModelResourceLocation(OpenStackMod.MODID+":"+NAME, "inventory"));
		
		instance.setUnlocalizedName(OpenStackMod.MODID+"_"+RDPItem.NAME);
		instance.setMaxStackSize(1);
		instance.setCreativeTab(CreativeTabs.tabMisc);
		
		ModelLoader.setCustomModelResourceLocation(instance, 0, new ModelResourceLocation(OpenStackMod.MODID+":"+NAME,"inventory"));
	}
	
	public RDPItem(){
		
	}
	
	
}
