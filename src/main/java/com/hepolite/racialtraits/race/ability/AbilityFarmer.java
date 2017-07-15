package com.hepolite.racialtraits.race.ability;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import com.hepolite.racialtraits.race.Race;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityFarmer extends Ability
{
	public AbilityFarmer(Race race)
	{
		super(race, "Farmer");
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerBreakBlock(BlockBreakEvent event)
	{
		Player player = event.getPlayer();
		PlayerSkill skill = getSkill(player);
		if (skill == null || player.isSneaking())
			return;

		Block block = event.getBlock();
		Material material = block.getType();
		if (material != Material.POTATO && material != Material.CARROT
				&& material != Material.CROPS && material != Material.BEETROOT_BLOCK)
			return;

		int size = skill.getLevel();
		for (int x = -size; x <= size; x++)
			for (int z = -size; z <= size; z++)
			{
				if (x == 0 && z == 0)
					continue;
				Block newBlock = block.getRelative(x, 0, z);
				Material current = newBlock.getType();
				if (current == Material.POTATO || current == Material.CARROT
						|| current == Material.CROPS || current == Material.BEETROOT_BLOCK)
				{
					@SuppressWarnings("deprecation")
					Byte meta = newBlock.getData();
					if (meta == 7 || (meta == 3 && material == Material.BEETROOT_BLOCK)) // The meta of fully grown crops is 7 for normal crops, 3 for beets
					{
						newBlock.breakNaturally(player.getInventory().getItemInMainHand());
						newBlock.setType(Material.AIR);
					}
				}
			}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerPlaceBlock(BlockPlaceEvent event)
	{
		Player player = event.getPlayer();
		PlayerSkill skill = getSkill(player);
		if (skill == null || player.isSneaking())
			return;

		Block block = event.getBlock();
		Material material = block.getType();

		Material seeds;
		switch (material)
		{
		case CROPS:
			seeds = Material.SEEDS;
			break;
		case POTATO:
			seeds = Material.POTATO_ITEM;
			break;
		case CARROT:
			seeds = Material.CARROT_ITEM;
			break;
		case BEETROOT_BLOCK:
			seeds = Material.BEETROOT_SEEDS;
			break;
		default:
			return;
		}

		int size = skill.getLevel();
		for (int x = -size; x <= size; x++)
			for (int z = -size; z <= size; z++)
			{
				if (x == 0 && z == 0)
					continue;
				if (block.getRelative(x, -1, z).getType() != Material.SOIL
						|| block.getRelative(x, 0, z).getType() != Material.AIR)
					continue;
				if (player.getInventory().contains(seeds))
				{
					block.getRelative(x, 0, z).setType(material);
					player.getInventory().removeItem(new ItemStack(seeds, 1));
				}
				else
					break;
			}
	}
}
