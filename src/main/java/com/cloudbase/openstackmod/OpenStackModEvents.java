package com.cloudbase.openstackmod;

import com.cloudbase.openstackmod.block.NovaBlock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OpenStackModEvents{
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(BlockEvent.PlaceEvent event){
		if(event.state.getBlock() instanceof NovaBlock){
			System.out.println("placed");  
		}
	}
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(BlockEvent.BreakEvent event){
		if(event.state.getBlock() instanceof NovaBlock){
			System.out.println("removed");
		}
	}
}
