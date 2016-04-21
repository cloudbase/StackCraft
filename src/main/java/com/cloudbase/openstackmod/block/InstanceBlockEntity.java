package com.cloudbase.openstackmod.block;

import com.cloudbase.openstackmod.OpenStackConnector;
import com.cloudbase.openstackmod.OpenStackMod;
import com.cloudbase.openstackmod.item.BlankImageItem;
import com.cloudbase.openstackmod.item.BoardItem;
import com.cloudbase.openstackmod.item.ChipItem;
import com.cloudbase.openstackmod.item.GlanceImageItem;
import com.cloudbase.openstackmod.item.NICItem;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import scala.reflect.internal.Trees.This;

public class InstanceBlockEntity extends TileEntity implements IUpdatePlayerListBox{
	public static String NAME;
	private ItemStack[] inventory;
	private String customName;
	private int ticker;
	private String instanceID;
	private boolean flick;
	private boolean sentFlick;
	private int flickCount;
	private int status;
	
	public static void init(){
		NAME = OpenStackMod.MODID+"_container.InstanceBlockEntity";
		GameRegistry.registerTileEntity(InstanceBlockEntity.class, NAME);
	}
	public InstanceBlockEntity(){
		ticker = 0;
		instanceID = "null";
		flick = false;
		this.inventory = new ItemStack[1];
		status = 0;
		flickCount = 0;
	}
	
	public String getInstanceID(){
		return instanceID;
	}
	
	public void setInstanceID(String id){
		this.instanceID = id;
	}
	
	public String getID() {
		return this.instanceID;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		
		nbt.setString("instanceID", this.instanceID);
	}


	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		
		this.instanceID = nbt.getString("instanceID");
	}
	
	@Override
	public void update() {
		if(this.getWorld().isRemote){
			this.status = this.getBlockMetadata();
		}
		if(!this.getWorld().isRemote){
			if(this.flick && !this.sentFlick){
				this.sentFlick = true;
				if(status == 0 || status == 5)
					OpenStackConnector.startInstance(this.instanceID);
				if(status == 2 || status == 6)
					OpenStackConnector.stopInstance(this.instanceID);
			}
			if(flickCount > 0)flickCount --;
			if(flickCount == 0)flick = false;
			ticker++;
			if(ticker>10){
				ticker=0;
				String instanceRaw = OpenStackConnector.getNovaInstances();
				if(instanceRaw != null){
					String[] instances = instanceRaw.split("&");	
					boolean instanceStillExists = false;
					for(int i=0;i<instances.length;i++)
					if(instances[i].length()>1){
						String id = instances[i].split(";")[1];
						if(this.instanceID.equals(id)){
							instanceStillExists = true;
							String stringStatus = instances[i].split(";")[2];
							//System.out.println(stringStatus + ":" + this.flick + " state " + status);
							if(stringStatus.equals("active")){
								if(flick)
									status = 6;
								else
									status = 2;
							}
							else if(stringStatus.equals("shutoff")){
								if(flick)
									status = 5;
								else
									status = 0;
							}
							else if(stringStatus.equals("powering-on")){
								status = 1;
								flick = false;
							}
							else if(stringStatus.equals("powering-off")){
								status = 4;
								flick = false;
							}
							else if(stringStatus.equals("error")){
								status = 3;
								flick = false;
							}
						}
					}
					this.getWorld().setBlockState(pos, InstanceBlock.instance.getStateFromMeta(status));
				}
			}
		}
	}
	
	@Override
	public boolean shouldRefresh(World w, BlockPos pos, IBlockState oState, IBlockState nState){
		if(nState == Blocks.air.getDefaultState())
			return true;
		return false;
	}
	public void flickSwitch() {
		//flick switch only when active or shutoff
		//System.out.println("Flicked at state:" + status);
		if(status == 2 || status == 0 || status == 5 || status == 6){
			if(status == 2)status = 6;
			if(status == 0)status = 5;
			this.getWorld().setBlockState(pos, InstanceBlock.instance.getStateFromMeta(status));
			this.ticker = 0;
			this.flick = true;
			this.sentFlick = false;
			this.flickCount = 60;
		}
	}
	public int getState() {
		return this.status;
	}

}
