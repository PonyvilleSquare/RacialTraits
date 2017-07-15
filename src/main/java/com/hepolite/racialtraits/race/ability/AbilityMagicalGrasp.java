package com.hepolite.racialtraits.race.ability;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.hepolite.coreutility.apis.damage.DamageAPI;
import com.hepolite.coreutility.apis.entites.EntityLocater.LineLocater;
import com.hepolite.coreutility.util.InventoryHelper;
import com.hepolite.racialtraits.race.Race;

public class AbilityMagicalGrasp extends Ability
{
	public AbilityMagicalGrasp(Race race)
	{
		super(race, "Magical Grasp");
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public final void onPlayerAttack(PlayerInteractEvent event)
	{
		if (event.getHand() == EquipmentSlot.OFF_HAND || event.getAction() != Action.LEFT_CLICK_AIR)
			return;
		ItemStack item = event.getItem();
		if (!InventoryHelper.isSword(item))
			return;
		Player player = event.getPlayer();
		if (!validate(player))
			return;

		float range = getSettings().getFloat("range");

		Location start = player.getEyeLocation();
		LineLocater line = new LineLocater(start, start.getDirection().multiply(range));
		LivingEntity target = line.getFirstUnobstructed(start, player, false, LivingEntity.class);
		if (target != null)
		{
			if (DamageAPI.damage(target, player, item))
			{
				item.setDurability((short) (item.getDurability() + 1));
				if (item.getDurability() >= item.getType().getMaxDurability())
					player.getEquipment().setItemInMainHand(null);
			}
		}
	}
}
