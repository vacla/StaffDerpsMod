package com.kyzeragon.staffderpsmod;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class ChestSorter 
{
	private Container container;
	private LinkedList<Integer> items;

	public ChestSorter()
	{
		this.items = new LinkedList<Integer>();
	}

	public void grab(Container container)
	{
		if (this.items.size() < 1)
			return;
		this.container = container;
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		List<Slot> slots = this.container.inventorySlots;
		for (int i = 0; i < slots.size() - 36; i++)
		{
			if (slots.get(i) != null && slots.get(i).getStack() != null)
			{
				for (int id: items)
				{
					int currID = Item.getIdFromItem(slots.get(i).getStack().getItem());
					System.out.println("Current: " + Item.getIdFromItem(slots.get(i).getStack().getItem()));
					if (currID == id)
//					if (slots.get(i).getStack().isItemEqual(stack))
					{
						Minecraft.getMinecraft().playerController.windowClick(container.windowId, i, 0, 1, player);
//						System.out.println("Grabbing slot " + i);
						break;
						/* Action values:
						 * 0: Standard Click
						 * 1: Shift-Click
						 * 2: Move item to/from hotbar slot (Depends on current slot and hotbar slot being full or empty)
						 * 3: Duplicate item (only while in creative)
						 * 4: Drop item
						 * 5: Spread items (Drag behavior)
						 * 6: Merge all valid items with held item
						 * 
						 * Data values:
						 * 0 - left click
						 * 1 - right click
						 */
					}
				}
			}
		}
	}

	public void setItems(String list)
	{
		String[] parsedList = list.split(",");
		String result = "Now grabbing items:";
		for (String item: parsedList)
		{
			if (item.matches("[0-9]+") && Item.itemRegistry.containsID(Integer.parseInt(item)))
				this.items.addFirst(Integer.parseInt(item));
			else if (Item.itemRegistry.containsKey(item))
				this.items.addFirst(Item.itemRegistry.getIDForObject(Item.itemRegistry.getObject(item)));
			else
			{
				this.logError("No such item ID/name as " + item + ".");
				return;
			}
			result += " " + Item.getItemById(this.items.get(0)).getUnlocalizedName() + ",";
		}
		this.logMessage(result.substring(0, result.length() - 1) + ".");
	}

	public void handleCommand(String message)
	{
		String[] tokens = message.split(" ");
		if (tokens.length < 3)
			this.logError("Usage: /sd sort <item>");
		else if (tokens.length == 3 && tokens[2].equalsIgnoreCase("clear"))
		{
			this.items.clear();
			this.logMessage("Cleared list of items to grab");
		}
		else if (tokens.length == 3)
			this.setItems(tokens[2]);
	}

	/**
	 * Logs the message to the user
	 * @param message The message to log
	 */
	private void logMessage(String message)
	{
		ChatComponentText displayMessage = new ChatComponentText(message);
		displayMessage.setChatStyle((new ChatStyle()).setColor(EnumChatFormatting.GREEN));
		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(displayMessage);
	}

	/**
	 * Logs the error message to the user
	 * @param message The error message to log
	 */
	private void logError(String message)
	{
		ChatComponentText displayMessage = new ChatComponentText(message);
		displayMessage.setChatStyle((new ChatStyle()).setColor(EnumChatFormatting.RED));
		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(displayMessage);
	}
}
