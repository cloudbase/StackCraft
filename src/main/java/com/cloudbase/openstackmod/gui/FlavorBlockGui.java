package com.cloudbase.openstackmod.gui;

import java.io.IOException;

import com.cloudbase.openstackmod.OpenStackConnector;
import com.cloudbase.openstackmod.block.FlavorOtherBlockEntity;
import com.cloudbase.openstackmod.block.GlanceBlockEntity;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class FlavorBlockGui extends GuiContainer {
	private FlavorOtherBlockEntity te;
	public FlavorBlockGui(IInventory playerInv, FlavorOtherBlockEntity te) {
		super(new FlavorBlockGuiContainer(playerInv, te));

		this.xSize = 176;
		this.ySize = 166;
		
		this.te = te;
	}
	
	@Override
	public void initGui(){
		super.initGui();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
	    this.mc.getTextureManager().bindTexture(new ResourceLocation("openstackmod:textures/gui/container/flavorblockgui.png"));
	    this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException{

	}
}
