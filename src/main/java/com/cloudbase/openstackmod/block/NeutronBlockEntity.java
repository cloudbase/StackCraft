package com.cloudbase.openstackmod.block;

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

public class NeutronBlockEntity extends TileEntity implements IInventory, IUpdatePlayerListBox{
	public static String NAME;
	private ItemStack[] inventory;
	private String customName;
	private boolean mustCreate;
	private String requiredNIC;
	
	public static void init(){
		NAME = OpenStackMod.MODID+"_container.NeutronBlockEntity";
		GameRegistry.registerTileEntity(NeutronBlockEntity.class, NAME);
	}
	public NeutronBlockEntity(){
		this.inventory = new ItemStack[3];
	}
	@Override
	public String getName() {
		return NeutronBlockEntity.NAME;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName());
	}

	@Override
	public int getSizeInventory() {
		return this.inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		if(index >= 0 && index < this.getSizeInventory())
			return this.inventory[index];
		return null;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (this.getStackInSlot(index) != null) {
	        ItemStack itemstack;

	        if (this.getStackInSlot(index).stackSize <= count) {
	            itemstack = this.getStackInSlot(index);
	            this.setInventorySlotContents(index, null);
	            this.markDirty();
	            return itemstack;
	        } else {
	            itemstack = this.getStackInSlot(index).splitStack(count);

	            if (this.getStackInSlot(index).stackSize <= 0) {
	                this.setInventorySlotContents(index, null);
	            } else {
	                //Just to show that changes happened
	                this.setInventorySlotContents(index, this.getStackInSlot(index));
	            }

	            this.markDirty();
	            return itemstack;
	        }
	    } else {
	        return null;
	    }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index) {
		ItemStack stack = this.getStackInSlot(index);
	    this.setInventorySlotContents(index, null);
	    return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (index < 0 || index >= this.getSizeInventory())
	        return;

	    if (stack != null && stack.stackSize > this.getInventoryStackLimit())
	        stack.stackSize = this.getInventoryStackLimit();
	        
	    if (stack != null && stack.stackSize == 0)
	        stack = null;

	    this.inventory[index] = stack;
	    this.markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.getPos()) == this &&
			   player.getDistanceSq(this.pos.add(0.5, 0.5, 0.5)) < 2.0;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if((index == 0 || index == 1) && stack != null && (stack.getItem() == ChipItem.instance || stack.getItem() == BoardItem.instance))
			return true;
		return false;
	}

	@Override
	public int getField(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		for (int i = 0; i < this.getSizeInventory(); i++)
	        this.setInventorySlotContents(i, null);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
	    super.writeToNBT(nbt);

	    NBTTagCompound stack;
	    if(this.inventory[0] != null){
		    stack = new NBTTagCompound();
		    this.inventory[0].writeToNBT(stack);
		    nbt.setTag("LeftObj", stack);
	    }
	    if(this.inventory[1] != null){
		    stack = new NBTTagCompound();
		    this.inventory[1].writeToNBT(stack);
		    nbt.setTag("RightObj", stack);
	    }
	    if(this.inventory[2] != null){
		    stack = new NBTTagCompound();
		    this.inventory[2].writeToNBT(stack);
		    nbt.setTag("CenterObj", stack);
	    }
	}


	@Override
	public void readFromNBT(NBTTagCompound nbt) {
	    super.readFromNBT(nbt);

	    this.inventory[0]=ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("LeftObj"));
	    this.inventory[1]=ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("RightObj"));
	    this.inventory[2]=ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("CenterObj"));
	}
	
	@Override
	public void update() {
		if(this.mustCreate){
			this.mustCreate = false;
			ItemStack stack = new ItemStack(NICItem.instance);
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("nicID", this.requiredNIC);
			stack.setTagCompound(nbt);
			setInventorySlotContents(0, null);
			setInventorySlotContents(1, null);
			setInventorySlotContents(2, stack);
		}
	}
	
	public void createNIC(String nic){
		if(!this.mustCreate){
			if(this.inventory[2] == null || 
			   (this.inventory[2] != null && this.inventory[2].getItem() == null) || 
			   (this.inventory[2] != null && this.inventory[2].stackSize == 0))
			if(this.inventory[0] != null && this.inventory[0].getItem() == BoardItem.instance &&
		       this.inventory[1] != null && this.inventory[1].getItem() == ChipItem.instance){
					this.mustCreate = true;
					this.requiredNIC = nic;
			}
		}
	}
}
