package com.cloudbase.openstackmod.item;

import java.util.ArrayList;
import java.util.List;

import com.cloudbase.openstackmod.OpenStackMod;
import com.google.common.collect.Lists;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GlanceImageItem extends Item{
	
	public static GlanceImageItem instance = null;
	public static final String NAME = "GlanceImage";
	
	public static void init(){
		GameRegistry.registerItem(instance, NAME);
		
		//instance.setTextureName(OpenStackMod.MODID+":"+RDPItem.NAME);
		//((Item)instance).set.func_111206_d("generic:genericItem");
		
		//Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(instance, 0, new ModelResourceLocation(OpenStackMod.MODID+":"+NAME, "inventory"));
		//Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(instance, 0, new ModelResourceLocation(OpenStackMod.MODID+":"+NAME, "inventory"));
		
		instance.setUnlocalizedName(OpenStackMod.MODID+"_"+NAME);
		instance.setMaxStackSize(1);
		
		ModelLoader.setCustomModelResourceLocation(instance, 0, new ModelResourceLocation(OpenStackMod.MODID+":"+NAME,"inventory"));
	}
	
	public GlanceImageItem(){
		
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced){
		NBTTagCompound nbt = stack.getTagCompound();

        if (nbt != null)
        {	
            tooltip.add(nbt.getString("imageID").split(";")[0]);
        }
	}
}