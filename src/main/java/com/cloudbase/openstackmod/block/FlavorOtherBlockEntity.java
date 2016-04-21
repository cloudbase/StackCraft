package com.cloudbase.openstackmod.block;

import com.cloudbase.openstackmod.OpenStackConnector;
import com.cloudbase.openstackmod.OpenStackMod;
import com.cloudbase.openstackmod.item.BlankImageItem;
import com.cloudbase.openstackmod.item.BoardItem;
import com.cloudbase.openstackmod.item.ChipItem;
import com.cloudbase.openstackmod.item.GlanceImageItem;
import com.cloudbase.openstackmod.item.NICItem;
import com.cloudbase.openstackmod.block.InstanceBlockEntity;

import net.minecraft.block.Block;
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

public class FlavorOtherBlockEntity extends TileEntity implements IInventory, IUpdatePlayerListBox{
	public static String NAME;
	private ItemStack[] inventory;
	private String customName;
	private boolean canTransform;
	private int creationKey;
	private String instanceData;
	
	public static void init(){
		NAME = OpenStackMod.MODID+"_container.FlavorOtherBlockEntity";
		GameRegistry.registerTileEntity(FlavorOtherBlockEntity.class, NAME);
	}
	public FlavorOtherBlockEntity(){
		this.inventory = new ItemStack[10];
		canTransform = false;
	}
	@Override
	public String getName() {
		return FlavorOtherBlockEntity.NAME;
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
		if(index == 0 && stack != null && stack.getItem() == Item.getItemFromBlock(FlavorOtherBlock.instance))
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

	    nbt.setString("flavorID", this.getTileData().getString("flavorID"));
	    NBTTagCompound stack;
	    for(int i=0;i<getSizeInventory();i++)
	    if(this.inventory[i] != null){
		    stack = new NBTTagCompound();
		    this.inventory[i].writeToNBT(stack);
		    nbt.setTag("InvObj" + i, stack);
	    }
	}


	@Override
	public void readFromNBT(NBTTagCompound nbt) {
	    super.readFromNBT(nbt);
	    
	    this.getTileData().setString("flavorID", nbt.getString("flavorID"));
	    for(int i=0;i<getSizeInventory();i++){
	    	this.inventory[i]=ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("InvObj" + i));
	    }
	}
	
	@Override
	public void update() {
		if(!this.worldObj.isRemote){
			//see if instance creation succeded
			if(this.canTransform){
				String response = OpenStackConnector.getRequestResponse(creationKey);
				if(response.startsWith("DONE:")){
					response = response.substring(5);
					if(response.startsWith("error:")){
						this.getWorld().setBlockState(new BlockPos(pos.getX(), pos.getY()+1, pos.getZ()), Blocks.lever.getStateFromMeta(6));
						this.canTransform = false;
						
						EntityPlayer p = this.getWorld().getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 1000);
						if(p != null)
							p.addChatMessage(new ChatComponentText(response.substring(6)));
					}
					else{
						String ID = response.split(";")[1];
						System.out.println(response);
						for(int i=0;i<this.inventory.length;i++)
							this.setInventorySlotContents(i, null);
						this.getWorld().destroyBlock(new BlockPos(pos.getX(), pos.getY()+1, pos.getZ()), false);
						this.getWorld().destroyBlock(pos, false);
						this.getWorld().setBlockState(pos, InstanceBlock.instance.getStateFromMeta(1));
						((InstanceBlockEntity)this.getWorld().getTileEntity(pos)).setInstanceID(ID);
						
					}
				}
			}
			else
			{
				//schedule creation
				if(this.worldObj.isBlockPowered(pos)){
					String imageData = "";
					String nicData = "";
					String flavorData = "";
					boolean images = false;
					int nics = 0;
					boolean goodSetup = true;
					instanceData = "";
					
					if(this.inventory[9] == null){
						goodSetup = false;
						EntityPlayer p = this.getWorld().getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 1000);
						if(p != null)
							p.addChatMessage(new ChatComponentText("No image inside the flavor block."));
					}
					
					for(int i=0;i<9;i++)
					for(int j=0;j<9;j++)
						if(goodSetup)
						if(i!=j){
							if(this.inventory[i] != null && this.inventory[j] != null)
							if(this.inventory[i].getTagCompound().getString("nicID") ==
							   this.inventory[j].getTagCompound().getString("nicID")){
								goodSetup = false;
								EntityPlayer p = this.getWorld().getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 1000);
								if(p != null)
									p.addChatMessage(new ChatComponentText("Duplicate NICs inside the flavor block."));
							}
						}
					if(!goodSetup){
						this.getWorld().setBlockState(new BlockPos(pos.getX(), pos.getY()+1, pos.getZ()), Blocks.lever.getStateFromMeta(6));
					}
					else{
						for(int i=0;i<10;i++){
							ItemStack stack = this.inventory[i];
							if(stack != null){
								if(i==9){
									images = true;
									imageData = stack.getTagCompound().getString("imageID").split(";")[1];
								}
								else{
									if(nics != 0) nicData += "+";
									nics++;
									nicData += stack.getTagCompound().getString("nicID").split(";")[1];
								}
							}
						}
						instanceData = imageData + "/" + this.getTileData().getString("flavorID").split(";")[1] + "/" + nicData;
					}
					if(images && nics > 0){
						this.canTransform = true;
						this.creationKey = OpenStackConnector.createInstance(instanceData);
					}
				}
			}
		}
	}
	
	public boolean insertImage(ItemStack heldItem) {
		if(this.getStackInSlot(9) != null && this.getStackInSlot(9).stackSize != 0 && this.getStackInSlot(9).getItem() != null)return false;
		ItemStack stack = new ItemStack(GlanceImageItem.instance);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("imageID", heldItem.getTagCompound().getString("imageID"));
		stack.setTagCompound(nbt);
		this.setInventorySlotContents(9, stack);
		return true;
	}
	
	public boolean insertNIC(ItemStack heldItem) {
		int emptyPosition = -1;
		for(int i=0;i<9;i++)
		if(this.getStackInSlot(i) != null){
			String id = this.getStackInSlot(i).getTagCompound().getString("nicID");
			if(id.equals(heldItem.getTagCompound().getString("nicID")))
				return false;
		}
		for(int i=0;i<9;i++){
			if(emptyPosition == -1 && !(this.getStackInSlot(i) != null && this.getStackInSlot(i).stackSize != 0 && this.getStackInSlot(i).getItem() != null)){
				emptyPosition = i;
				break;
			}
		}
		if(emptyPosition == -1)return false;
		ItemStack stack = new ItemStack(NICItem.instance);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("nicID", heldItem.getTagCompound().getString("nicID"));
		stack.setTagCompound(nbt);
		this.setInventorySlotContents(emptyPosition, stack);
		return true;
	}
}
