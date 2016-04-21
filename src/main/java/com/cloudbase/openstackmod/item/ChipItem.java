package com.cloudbase.openstackmod.item;

import com.cloudbase.openstackmod.OpenStackMod;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ChipItem extends Item{
	
	public static ChipItem instance = null;
	public static final String NAME = "Chip";
	
	public static void init(){
		GameRegistry.registerItem(instance, NAME);
		
		//instance.setTextureName(OpenStackMod.MODID+":"+RDPItem.NAME);
		//((Item)instance).set.func_111206_d("generic:genericItem");
		
		//Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(instance, 0, new ModelResourceLocation(OpenStackMod.MODID+":"+NAME, "inventory"));
		//Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(instance, 0, new ModelResourceLocation(OpenStackMod.MODID+":"+NAME, "inventory"));
		
		GameRegistry.addRecipe(new ItemStack(instance), new Object[]{
		    	"CNC",
		    	"NRN",
		    	"CNC",
		    	'C', Items.coal,
		    	'R', Items.redstone,
		    	'N', Items.gold_nugget,
		    	});
		
		instance.setUnlocalizedName(OpenStackMod.MODID+"_"+NAME);
		instance.setMaxStackSize(32);
		instance.setCreativeTab(CreativeTabs.tabMisc);
		
		ModelLoader.setCustomModelResourceLocation(instance, 0, new ModelResourceLocation(OpenStackMod.MODID+":"+NAME,"inventory"));
	}
	
	public ChipItem(){
		
	}
	
	
}