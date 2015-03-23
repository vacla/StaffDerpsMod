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
		
		int[] trash = {261, 268, 269, 270, 271, 290, 295, 375, 394, 358}; // add new before 358
		int[] junk = {78, 69, 70, 72, 77, 96, 107, 65, 143, 147, 148, 281,
				53, 67, 109, 126, 128, 134, 135, 136, 139, 163, 164, 102, 160, 171, 44, 44}; // add new before 44
		int[] nostackjunk = {355, 324, 333};
		int[] tooljunk = {398, 273, 274, 275, 291, 284, 285, 286, 294, 346, 293, 272};
		int[] ores = {152, 331, 14, 15, 16, 21, 22, 173, 41, 42, 56, 57, 73, 129, 
				133, 264, 263, 265, 266, 388, 371, 351}; // add new before 351
		int[] food = {260, 282, 297, 319, 320, 322, 349, 350, 354, 357, 360, 363, 364, 365, 366, 391,
				392, 393, 396, 400, 296, 361, 362, 86, 103};
		int[] plants = {6, 18, 31, 32, 37, 38, 39, 40, 81, 106, 111, 161, 175, 338};
		int[] discs = {2256, 2257, 2258, 2259, 2260, 2261, 2262, 2263, 2264, 2265, 2266, 2267};
		int[] armor = {298, 299, 300, 301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311, 312, 313, 314, 315, 316, 317};
		int[] tools = {256, 257, 258, 259, 277, 278, 279, 292, 359, 267, 276, 283};
		int[] wood = {5, 17, 162};
		
		this.presets.put("trash", trash);
		this.presets.put("junk", junk);
		this.presets.put("nostackjunk", nostackjunk);
		this.presets.put("tooljunk", tooljunk);
		this.presets.put("ores", ores);
		this.presets.put("food", food);
		this.presets.put("plants", plants);
		this.presets.put("discs", discs);
		this.presets.put("armor", armor);
		this.presets.put("tools", tools);
		this.presets.put("wood", wood);
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
			else if (tokens[2].equalsIgnoreCase("ores"))
				this.meta.addFirst(4);
			
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
