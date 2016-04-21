package com.cloudbase.openstackmod.block;

import java.util.List;
import java.util.Random;

import com.cloudbase.openstackmod.OpenStackMod;
import com.cloudbase.openstackmod.gui.GuiHandler;
import com.cloudbase.openstackmod.item.BlankImageItem;
import com.cloudbase.openstackmod.item.GlanceImageItem;
import com.cloudbase.openstackmod.item.NICItem;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.main.Main;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FlavorOtherBlock extends BlockContainer{
	public static final String NAME = "FlavorOther";
	public static FlavorOtherBlock instance = null;
	
	public static void init(){	
		GameRegistry.registerBlock(instance, instance.NAME);
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(instance), 0, new ModelResourceLocation(OpenStackMod.MODID+":"+instance.NAME,"inventory"));
		
		instance.setUnlocalizedName(OpenStackMod.MODID + "_" + instance.NAME);
		instance.setHardness(3);
	}

	public FlavorOtherBlock() {
		super(Material.ground);
	}
	
	@Override
	public int getRenderType(){
        return 3;
    }
	
	public boolean renderAsNormalBlock(){
        return true;
    }
	
	@Override
	public Item getItemDropped(IBlockState state, Random rnd, int fortune){
		return null;
	}
	
	public Item getItemDropped(int metadata, Random random, int fortune){
		return null;
	}
	
	@Override
	public boolean isFullCube(){
		return true;
	}
	
	@Override
	public boolean isFullBlock(){
		return true;
	}
	
	@Override
	public boolean isOpaqueCube(){
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new FlavorOtherBlockEntity();
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
	    FlavorOtherBlockEntity te = (FlavorOtherBlockEntity) world.getTileEntity(pos);
	    InventoryHelper.dropInventoryItems(world, pos, te);
	    super.breakBlock(world, pos, blockstate);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		FlavorOtherBlockEntity te = (FlavorOtherBlockEntity)worldIn.getTileEntity(pos);
		te.getTileData().setString("flavorID", stack.getTagCompound().getString("flavorID"));
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(!world.isRemote){
			if(player.getHeldItem() != null && player.getHeldItem().getItem() == GlanceImageItem.instance){
				if(((FlavorOtherBlockEntity)world.getTileEntity(pos)).insertImage(player.getHeldItem()))
					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
				else
					player.openGui(OpenStackMod.instance, GuiHandler.MOD_FLAVOR_GUI, world, pos.getX(), pos.getY(), pos.getZ());
			}
			else if(player.getHeldItem() != null && player.getHeldItem().getItem() == NICItem.instance){
				if(((FlavorOtherBlockEntity)world.getTileEntity(pos)).insertNIC(player.getHeldItem()))
					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
				else
					player.openGui(OpenStackMod.instance, GuiHandler.MOD_FLAVOR_GUI, world, pos.getX(), pos.getY(), pos.getZ());
			}
			else if(player.getHeldItem() != null && player.getHeldItem().getItem() == Item.getItemFromBlock(Blocks.lever) && hitY == 1.0){
				return false;
			}
			else{
				player.openGui(OpenStackMod.instance, GuiHandler.MOD_FLAVOR_GUI, world, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return true;
	}
	
	public String getUnlocalizedName(ItemStack itemstack) {
		return getUnlocalizedName() + itemstack.getTagCompound().getString("flavorID").split(";")[0];
	}
}

