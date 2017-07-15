package com.hepolite.racialtraits.ability;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.hepolite.coreutility.CoreUtility;
import com.hepolite.coreutility.util.InventoryHelper;
import com.hepolite.racialtraits.race.Race;

public class AbilityCloudSeed extends Ability
{
	public AbilityCloudSeed(Race race)
	{
		super(race, "Cloud Seed");
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public final void onPlayerInteract(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		if (!validate(player) || event.getAction() != Action.RIGHT_CLICK_AIR)
			return;
		ItemStack cloud = getSettings().getItem("seed");
		ItemStack item = player.getInventory().getItemInMainHand();
		if (!InventoryHelper.isItemSubType(cloud, item))
			return;

		Location location = player.getEyeLocation();
		Block block = location.add(location.getDirection().multiply(3.0f)).getBlock();
		if (CoreUtility.getBlockHandler().playerPlaceBlock(player, block.getLocation(), cloud))
			InventoryHelper.remove(player, cloud);
	}
}
