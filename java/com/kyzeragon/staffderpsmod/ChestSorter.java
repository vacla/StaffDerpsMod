package com.kyzeragon.staffderpsmod;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class ChestSorter 
{
	private Container container;
	private LinkedList<Integer> items;
	private LinkedList<Integer> meta;
	private HashMap<String, int[]> presets;
	
	public ChestSorter()
	{
		this.items = new LinkedList<Integer>();
		this.meta = new LinkedList<Integer>();
		this.presets = new HashMap<String, int[]>();
		
		int[] trash = {261, 268, 269, 270, 271, 290, 295, 375, 358};
		int[] junk = {78, 69, 70, 72, 77, 96, 107, 65, 143, 147, 148, 281,
				53, 67, 109, 126, 128, 134, 135, 136, 139, 163, 164, 102, 160, 171, 44, 44}; // add new before 44
		int[] nostackjunk = {355, 324, 333};
		int[] tooljunk = {398, 273, 274, 275, 291, 284, 285, 286, 294, 346, 293, 272};
		this.presets.put("trash", trash);
		this.presets.put("junk", junk);
		this.presets.put("nostackjunk", nostackjunk);
		this.presets.put("tooljunk", tooljunk);
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
				int currID = Item.getIdFromItem(slots.get(i).getStack().getItem());
				int currMeta = slots.get(i).getStack().getItemDamage();
				Item item = slots.get(i).getStack().getItem();
				for (int j = 0; j < this.items.size(); j++)
				{
					int id = this.items.get(j);
					int findMeta = this.meta.get(j);
					if (currID == id)
					{
						if (findMeta == -1 || findMeta == currMeta)
						{
							Minecraft.getMinecraft().playerController.windowClick(container.windowId, i, 0, 1, player);
							break;
						}
					}
				}
			}
		}
	}

	public void setItems(String list)
	{
		String[] parsedList = list.split(",");
		String result = "§8[§2StaffDerps§8] §aNow grabbing items:";
		for (String item: parsedList)
		{
			String[] parts = item.split(":");
			if (parts[0].matches("[0-9]+") && Item.itemRegistry.containsID(Integer.parseInt(parts[0])))
				this.items.addFirst(Integer.parseInt(parts[0]));
			else if (Item.itemRegistry.containsKey(parts[0]))
				this.items.addFirst(Item.itemRegistry.getIDForObject(Item.itemRegistry.getObject(parts[0])));
			else
			{
				LiteModStaffDerps.logError("No such item ID/name as \"" + item + "\".");
				return;
			}
			///// metadata /////
			if (parts.length > 1 && parts[1].matches("[0-9]+"))
				this.meta.addFirst(Integer.parseInt(parts[1]));
			else
				this.meta.addFirst(-1);
			result += " " + (new ItemStack(Item.getItemById(this.items.get(0)))).getDisplayName() + ",";
		}
		LiteModStaffDerps.logMessage(result.substring(0, result.length() - 1) + ".");
	}

	public void handleCommand(String message)
	{
		String[] tokens = message.split(" ");
		if (tokens.length < 3)
		{
			if (this.items.size() < 1)
			{
				LiteModStaffDerps.logError("No items specified yet. Usage: /sd grab <item[,item2]>");
				return;
			}
			String result = "§8[§2StaffDerps§8] §aCurrently grabbing:";
			for (Integer id: this.items)
				result += " " + (new ItemStack(Item.getItemById(id))).getDisplayName() + ",";
			LiteModStaffDerps.logMessage(result.substring(0, result.length() - 1) + ".");
		}
		else if (tokens.length == 3 && tokens[2].equalsIgnoreCase("clear"))
		{
			this.items.clear();
			this.meta.clear();
			LiteModStaffDerps.logMessage("§8[§2StaffDerps§8] §aCleared list of items to grab");
		}
		else if (tokens.length == 3 && this.presets.containsKey(tokens[2].toLowerCase()))
		{
			this.items.clear();
			this.meta.clear();
			int[] preset = this.presets.get(tokens[2]);
			for (int i = 0; i < preset.length; i++)
			{
				this.items.addFirst(preset[i]);
				this.meta.addFirst(-1);
			}
			if (tokens[2].equalsIgnoreCase("junk"))
			{
				this.meta.addFirst(3);
				this.meta.addFirst(5);
			}
			else if (tokens[2].equalsIgnoreCase("trash"))
				this.meta.addFirst(0);
			
			LiteModStaffDerps.logMessage("§8[§2StaffDerps§8] §aNow grabbing all items in the preset: " + tokens[2]);
		}
		else if (tokens.length == 3 && tokens[2].matches("presets?"))
		{
			String result = "§8[§2StaffDerps§8] §aAvailable presets:";
			for (String preset: this.presets.keySet())
				result += " " + preset + ",";
			LiteModStaffDerps.logMessage(result.substring(0, result.length() - 1) + ".");
		}
		else if (tokens.length == 3)
		{
			this.items.clear();
			this.meta.clear();
			this.setItems(tokens[2]);
		}
		else
		{
			LiteModStaffDerps.logError("Invalid parameters! Usage: /sd grab <item[,item2]>");
			String result = "§8[§2StaffDerps§8] §aAvailable presets:";
			for (String preset: this.presets.keySet())
				result += " " + preset + ",";
			LiteModStaffDerps.logMessage(result.substring(0, result.length() - 1) + ".");
		}
	}
}
