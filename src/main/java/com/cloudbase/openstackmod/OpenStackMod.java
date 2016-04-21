package com.cloudbase.openstackmod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import com.cloudbase.openstackmod.block.CloudBaseBlock;
import com.cloudbase.openstackmod.block.FlavorOtherBlock;
import com.cloudbase.openstackmod.block.FlavorOtherBlockEntity;
import com.cloudbase.openstackmod.block.GlanceBlock;
import com.cloudbase.openstackmod.block.GlanceBlockEntity;
import com.cloudbase.openstackmod.block.InstanceBlock;
import com.cloudbase.openstackmod.block.InstanceBlockEntity;
import com.cloudbase.openstackmod.block.NeutronBlock;
import com.cloudbase.openstackmod.block.NeutronBlockEntity;
import com.cloudbase.openstackmod.block.NovaBlock;
import com.cloudbase.openstackmod.block.NovaBlockEntity;
import com.cloudbase.openstackmod.block.OpenStackness;
import com.cloudbase.openstackmod.gui.GuiHandler;
import com.cloudbase.openstackmod.item.BlankImageItem;
import com.cloudbase.openstackmod.item.BoardItem;
import com.cloudbase.openstackmod.item.ChipItem;
import com.cloudbase.openstackmod.item.GlanceImageItem;
import com.cloudbase.openstackmod.item.NICItem;

import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.LanguageRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = OpenStackMod.MODID, version = OpenStackMod.VERSION)
public class OpenStackMod
{
	@Instance
	public static OpenStackMod instance;
    public static final String MODID = "openstackmod";
    public static final String VERSION = "1.1";
    
    public static final int GLANCE_MESSAGE_ID = 35;
    public static SimpleNetworkWrapper simpleNetworkWrapper;
    
    @EventHandler
	public void preInit(FMLPreInitializationEvent event) {
    	OBJLoader.instance.addDomain(OpenStackMod.MODID);
    	
    	OpenStackness.instance = new OpenStackness();
    	CloudBaseBlock.instance = new CloudBaseBlock();
    	NovaBlock.instance = new NovaBlock();
    	NeutronBlock.instance = new NeutronBlock();
    	GlanceBlock.instance = new GlanceBlock();
    	FlavorOtherBlock.instance = new FlavorOtherBlock();
    	InstanceBlock.instance = new InstanceBlock();
    	
    	RDPItem.instance = new RDPItem();
		BlankImageItem.instance = new BlankImageItem();
		GlanceImageItem.instance = new GlanceImageItem();
		NICItem.instance = new NICItem();
		BoardItem.instance = new BoardItem();
		ChipItem.instance = new ChipItem();
		
    	OpenStackness.init();
    	CloudBaseBlock.init();
    	NovaBlock.init();
    	NovaBlockEntity.init();
    	NeutronBlock.init();
    	NeutronBlockEntity.init();
    	GlanceBlock.init();
    	GlanceBlockEntity.init();
    	FlavorOtherBlock.init();
    	FlavorOtherBlockEntity.init();
    	InstanceBlock.init();
    	InstanceBlockEntity.init();
    	
    	RDPItem.init();
    	BlankImageItem.init();
    	GlanceImageItem.init();
    	NICItem.init();
    	BoardItem.init();
    	ChipItem.init();
    	
    	new Ball().RegisterRenderer("basicCannonball");
    	
    	ModelBakery.addVariantName(Item.getItemFromBlock(InstanceBlock.instance), "openstackmod:instanceblock_off", "openstackmod:instanceblock_inter", "openstackmod:instanceblock_on");
	}

    @EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new OpenStackModEvents());
		GuiHandler.instance = new GuiHandler();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, GuiHandler.instance);
	}

	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		String server = "127.0.0.1:6446";
		try {
			BufferedReader br = new BufferedReader(new FileReader("openstackmod_config.txt"));
			server = br.readLine();
			System.out.println(server);
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OpenStackConnector conn = new OpenStackConnector(server);
		new Thread(conn).start();
	}
}
