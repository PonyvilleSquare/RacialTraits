package com.hepolite.racialtraits.race.ability;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.hepolite.racialtraits.race.Race;

public class AbilityGrowthExpertise extends Ability
{
	public AbilityGrowthExpertise(Race race)
	{
		super(race, "Growth Expertise");
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		if (!validate(player) || event.getHand() == EquipmentSlot.OFF_HAND)
			return;

		ItemStack item = player.getInventory().getItemInMainHand();
		if (item == null || event.getAction() != Action.RIGHT_CLICK_BLOCK || item.getType() != Material.INK_SACK || item.getDurability() != (short) 15)
			return;
		Material material = event.getClickedBlock().getType();
		if (material != Material.CACTUS && material != Material.SUGAR_CANE_BLOCK)
			return;

		if (grow(event.getClickedBlock()))
			player.getInventory().removeItem(new ItemStack(Material.INK_SACK, 1, (short) 15));
	}

	/** Grows a block of sugar cane or cactus from another block */
	private final boolean grow(Block block)
	{
		int count = 0;
		Block currentBlock = block;
		while (currentBlock.getType() == block.getType())
		{
			count++;
			currentBlock = currentBlock.getRelative(0, -1, 0);
		}
		currentBlock = block.getRelative(0, 1, 0);
		while (currentBlock.getType() == block.getType())
		{
			count++;
			currentBlock = currentBlock.getRelative(0, 1, 0);
		}

		// Perform the actual growth
		if (count <= 4 && currentBlock.getType() == Material.AIR)
		{
			currentBlock.setType(block.getType());
			return true;
		}
		return false;
	}
}
