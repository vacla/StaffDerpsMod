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
//					System.out.println("Current: " + Item.getIdFromItem(slots.get(i).getStack().getItem()));
					if (currID == id)
					{
						Minecraft.getMinecraft().playerController.windowClick(container.windowId, i, 0, 1, player);
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
		String result = "§8[§2StaffDerps§8] §aNow grabbing items:";
		for (String item: parsedList)
		{
			if (item.matches("[0-9]+") && Item.itemRegistry.containsID(Integer.parseInt(item)))
				this.items.addFirst(Integer.parseInt(item));
			else if (Item.itemRegistry.containsKey(item))
				this.items.addFirst(Item.itemRegistry.getIDForObject(Item.itemRegistry.getObject(item)));
			else
			{
				LiteModStaffDerps.logError("No such item ID/name as \"" + item + "\".");
				return;
			}
			result += " " + Item.getItemById(this.items.get(0)).getUnlocalizedName() + ",";
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
				result += " " + Item.getItemById(id).getUnlocalizedName() + ",";
			LiteModStaffDerps.logMessage(result.substring(0, result.length() - 1) + ".");
		}
		else if (tokens.length == 3 && tokens[2].equalsIgnoreCase("clear"))
		{
			this.items.clear();
			LiteModStaffDerps.logMessage("§8[§2StaffDerps§8] §aCleared list of items to grab");
		}
		else if (tokens.length == 3)
			this.setItems(tokens[2]);
	}
}
