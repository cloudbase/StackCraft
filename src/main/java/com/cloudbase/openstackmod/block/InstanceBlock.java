package com.cloudbase.openstackmod.block;

import java.util.Collection;
import java.util.Random;

import com.cloudbase.openstackmod.OpenStackConnector;
import com.cloudbase.openstackmod.OpenStackMod;
import com.cloudbase.openstackmod.gui.GuiHandler;
import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class InstanceBlock extends BlockContainer{
	public static final String NAME = "InstanceBlock";
	public static InstanceBlock instance = null;
	
	public enum EnumType implements IStringSerializable{
		ON      (2, "on"),
		OFF     (0, "off"),
		POWON   (1, "powon"),
		POWOFF  (4, "powoff"),
		ERR     (3, "err"),
		OFFTOON (5, "offtoon"),
		ONTOOFF (6, "ontooff");
		
		private int ID;
		private String name;
		
		private EnumType(int ID, String name){
			this.ID = ID;
			this.name = name;
		}
		
		@Override
		public String getName(){
			return name;
		}
		
		public int getID(){
			return ID;
		}
		
		@Override
		public String toString(){
			return getName();
		}
	}
	public static final PropertyEnum TYPE = PropertyEnum.create("type", EnumType.class);
	
	public static void init(){	
		GameRegistry.registerBlock(instance, instance.NAME);
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(instance), 0, new ModelResourceLocation(OpenStackMod.MODID+":"+instance.NAME,"inventory"));
		
		instance.setUnlocalizedName(OpenStackMod.MODID + "_" + instance.NAME);
		instance.setHardness(5);
		instance.setDefaultState(instance.blockState.getBaseState().withProperty(TYPE, EnumType.POWON));
	}

	public InstanceBlock() {
		super(Material.ground);
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
	public int getRenderType(){
        return 3;
    }

	public boolean renderAsNormalBlock(){
        return true;
    }
	
	@Override
	protected BlockState createBlockState() {
	    return new BlockState(this, new IProperty[] { TYPE });
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumType ret = EnumType.OFF;
		if(meta == 2)ret = EnumType.ON;
		if(meta == 0)ret = EnumType.OFF;
		if(meta == 1)ret = EnumType.POWON;
		if(meta == 4)ret = EnumType.POWOFF;
		if(meta == 3)ret = EnumType.ERR;
		if(meta == 5)ret = EnumType.OFFTOON;
		if(meta == 6)ret = EnumType.ONTOOFF;
	    return this.getDefaultState().withProperty(TYPE, ret);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
	    EnumType type = (EnumType) state.getValue(TYPE);
	    return type.getID();
	}
	
	@Override
	public int damageDropped(IBlockState state) {
	    return getMetaFromState(state);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new InstanceBlockEntity();
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
	    InstanceBlockEntity te = (InstanceBlockEntity) world.getTileEntity(pos);
	    OpenStackConnector.terminateInstance(te.getID());
	    System.out.println("Destroyed:"+te.getID());
	    super.breakBlock(world, pos, blockstate);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(!world.isRemote){
			((InstanceBlockEntity)world.getTileEntity(pos)).flickSwitch();
		}
		return true;
	}
	
	@Override
	public float getBlockHardness(World world, BlockPos pos){
		int sts = ((InstanceBlockEntity)world.getTileEntity(pos)).getState();
		if(sts == 0 || sts == 3) return 3;
		return -1;
	}
	
	/*@Override
	public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state){
		System.out.println(((InstanceBlockEntity)world.getTileEntity(pos)).getID());
		if(state != InstanceBlock.instance.getStateFromMeta(0)){
			
		}
	}*/
}
