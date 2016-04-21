package com.cloudbase.openstackmod;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SingleObjectSlot extends Slot{
	private Item item;
	private int qty;
	public SingleObjectSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, Item item, int qty) {
		super(inventoryIn, index, xPosition, yPosition);

		this.item = item;
		this.qty = qty;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
    {
        if(stack.getItem() != null && this.item != null && stack.getItem() == item)
        	return true;
        return false;
    }

	@Override
	public int getSlotStackLimit(){
		return this.qty;
	}
}
