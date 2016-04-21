package com.cloudbase.openstackmod.gui;

import com.cloudbase.openstackmod.block.NeutronBlockEntity;
import com.cloudbase.openstackmod.block.GlanceBlockEntity;
import com.cloudbase.openstackmod.block.NovaBlockEntity;
import com.cloudbase.openstackmod.block.FlavorOtherBlockEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler{
	public static final int MOD_GLANCE_GUI = 0;
	public static final int MOD_NEUTRON_GUI = 1;
	public static final int MOD_NOVA_GUI = 2;
	public static final int MOD_FLAVOR_GUI = 3;
	public static GuiHandler instance;
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == MOD_GLANCE_GUI){
			return new GlanceBlockGuiContainer(player.inventory, (GlanceBlockEntity)world.getTileEntity(new BlockPos(x, y, z)));
		}
		if(ID == MOD_NEUTRON_GUI){
			return new NeutronBlockGuiContainer(player.inventory, (NeutronBlockEntity)world.getTileEntity(new BlockPos(x, y, z)));
		}
		if(ID == MOD_NOVA_GUI){
			return new NovaBlockGuiContainer(player.inventory, (NovaBlockEntity)world.getTileEntity(new BlockPos(x, y, z)));
		}
		if(ID == MOD_FLAVOR_GUI){
			return new FlavorBlockGuiContainer(player.inventory, (FlavorOtherBlockEntity)world.getTileEntity(new BlockPos(x, y, z)));
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {	
		if(ID == MOD_GLANCE_GUI){
			return new GlanceBlockGui(player.inventory, (GlanceBlockEntity)world.getTileEntity(new BlockPos(x, y, z)));
		}
		if(ID == MOD_NEUTRON_GUI){
			return new NeutronBlockGui(player.inventory, (NeutronBlockEntity)world.getTileEntity(new BlockPos(x, y, z)));
		}
		if(ID == MOD_NOVA_GUI){
			return new NovaBlockGui(player.inventory, (NovaBlockEntity)world.getTileEntity(new BlockPos(x, y, z)));
		}
		if(ID == MOD_FLAVOR_GUI){
			return new FlavorBlockGui(player.inventory, (FlavorOtherBlockEntity)world.getTileEntity(new BlockPos(x, y, z)));
		}
		return null;
	}
	
}
