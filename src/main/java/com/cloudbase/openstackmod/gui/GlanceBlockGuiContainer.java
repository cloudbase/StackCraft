package com.cloudbase.openstackmod.gui;

import com.cloudbase.openstackmod.OpenStackConnector;
import com.cloudbase.openstackmod.SingleObjectSlot;
import com.cloudbase.openstackmod.block.GlanceBlockEntity;
import com.cloudbase.openstackmod.item.BlankImageItem;
import com.cloudbase.openstackmod.item.GlanceImageItem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GlanceBlockGuiContainer extends Container {
	private GlanceBlockEntity te;
	
	public GlanceBlockGuiContainer(IInventory playerInv, GlanceBlockEntity entity){
		this.te = entity;
		
		this.addSlotToContainer(new SingleObjectSlot(te, 0, 53, 15, BlankImageItem.instance, 1));
		this.addSlotToContainer(new SingleObjectSlot(te, 1, 107, 15, GlanceImageItem.instance, 1));
		//player inventory
		// Player Inventory, Slot 9-35, Slot IDs 9-35
	    for (int y = 0; y < 3; ++y) {
	        for (int x = 0; x < 9; ++x) {
	            this.addSlotToContainer(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
	        }
	    }

	    // Player Inventory, Slot 0-8, Slot IDs 36-44
	    for (int x = 0; x < 9; ++x) {
	        this.addSlotToContainer(new Slot(playerInv, x, 8 + x * 18, 142));
	    }
	}
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
		//return this.te.isUseableByPlayer(playerIn);
	}
	
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot) {
	    ItemStack previous = null;
	    Slot slot = (Slot) this.inventorySlots.get(fromSlot);

	    if (slot != null && slot.getHasStack()) {
	        ItemStack current = slot.getStack();
	        previous = current.copy();

	        if (current.stackSize == 0)
	            slot.putStack((ItemStack) null);
	        else
	            slot.onSlotChanged();

	        if (current.stackSize == previous.stackSize)
	            return null;
	        slot.onPickupFromSlot(playerIn, current);
	    }
	    return previous;
	}
	
	@Override
	public boolean enchantItem(EntityPlayer playerIn, int id)
    {
    	te.convertImage(OpenStackConnector.getGlanceImages().split("&")[id]);
    	return true;
    }
}
