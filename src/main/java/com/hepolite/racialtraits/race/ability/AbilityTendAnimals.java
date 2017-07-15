package com.hepolite.racialtraits.race.ability;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;

import com.hepolite.coreutility.apis.entites.EntityLocater.SphereLocater;
import com.hepolite.racialtraits.race.Race;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityTendAnimals extends Ability
{
	public AbilityTendAnimals(Race race)
	{
		super(race, "Tend Animals");
	}

	@Override
	public void onTick(Player player, PlayerSkill skill, int tick)
	{
		if (tick % 600 != 0)
			return;

		float chance = getSettings().getFloat("chance");
		float range = getSettings().getFloat("range");
		int max = getSettings().getInt("max");
		drop(player.getLocation(), chance, range, max);
	}

	/** Drops loot from animals around the given location; returns true if something was dropped */
	@SuppressWarnings("deprecation")
	private final boolean drop(Location location, float chance, float range, int max)
	{
		int count = 0;
		SphereLocater locater = new SphereLocater(location, range);
		for (LivingEntity entity : locater.get(Animals.class))
		{
			if (chance < random.nextFloat())
				continue;
			count++;
			if (entity instanceof Chicken)
				entity.getWorld().dropItemNaturally(entity.getLocation(),
						new ItemStack(Material.FEATHER, 1));
			else if (entity instanceof Cow)
				entity.getWorld().dropItemNaturally(entity.getLocation(),
						new ItemStack(Material.LEATHER, 1));
			else if (entity instanceof Sheep)
				entity.getWorld().dropItemNaturally(entity.getLocation(),
						new ItemStack(Material.WOOL, 1, ((Sheep) entity).getColor().getWoolData()));
			else
				count--;
			if (count >= max)
				break;
		}
		return count > 0;
	}
}
