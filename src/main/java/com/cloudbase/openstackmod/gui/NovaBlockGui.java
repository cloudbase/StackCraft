package com.cloudbase.openstackmod.gui;

import java.io.IOException;

import com.cloudbase.openstackmod.OpenStackConnector;
import com.cloudbase.openstackmod.block.NovaBlockEntity;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class NovaBlockGui extends GuiContainer {
	private int option;
	private GuiButton buttonLeft;
	private GuiButton buttonRight;
	private GuiButton buttonChoose;
	private NovaBlockEntity te;
	public NovaBlockGui(IInventory playerInv, NovaBlockEntity te) {
		super(new NovaBlockGuiContainer(playerInv, te));

		this.xSize = 176;
		this.ySize = 166;
		this.option = 0;
		
		this.te = te;
	}
	
	@Override
	public void initGui(){
		super.initGui();
		
		int x = width/2;
		int y = (height - ySize)/2;
		
		this.buttonList.add(buttonLeft = new GuiButton(0, x-80, y+50, 20,20, "<"));
		this.buttonList.add(buttonChoose = new GuiButton(1, x-61, y+50, 122, 20, "Choose"));
		this.buttonList.add(buttonRight = new GuiButton(2, x+60,y+50, 20,20, ">"));
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
	    this.mc.getTextureManager().bindTexture(new ResourceLocation("openstackmod:textures/gui/container/novablockgui.png"));
	    this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String display = OpenStackConnector.getNovaFlavors().split("&")[this.option].split(";")[0];
		buttonChoose.displayString = display;
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException{
		if(button == this.buttonLeft){
			this.option--;
			if(this.option<0)
				this.option = OpenStackConnector.getNovaFlavors().split("&").length-1;
		}
		if(button == this.buttonRight){
			this.option++;
			if(this.option>=OpenStackConnector.getNovaFlavors().split("&").length)
				this.option = 0;
				
		}
		if(button == this.buttonChoose){
			te.createFlavor(OpenStackConnector.getNovaFlavors().split("&")[this.option]);
			this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, this.option);
		}
	}
}
