package com.hepolite.racialtraits.race.ability;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.hepolite.coreutility.util.InventoryHelper;
import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.race.components.ComponentMovementModifier;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityPreen extends Ability
{
	private final ComponentMovementModifier modifier;

	public AbilityPreen(Race race)
	{
		super(race, "Preen");
		modifier = new ComponentMovementModifier(getName());
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		float speed = getSettings().getFloat("boost");
		List<ItemStack> items = getSettings().getItems("items");
		int duration = getSettings().getInt("duration");

		modifier.setFly(player, 0.0f, speed, 0.0f, duration);
		InventoryHelper.add(player, items);
		return true;
	}

	@Override
	public final void onDeinitialize(Player player)
	{
		modifier.wipe(player);
	}
}
