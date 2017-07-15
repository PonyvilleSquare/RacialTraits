package com.hepolite.racialtraits.race.ability;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import com.hepolite.coreutility.apis.entites.EntityLocater.SphereLocater;
import com.hepolite.racialtraits.race.Race;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityDispel extends Ability
{
	public AbilityDispel(Race race)
	{
		super(race, "Dispel");
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		float radius = getSettings().getFloat("radius");

		boolean didSomething = false;
		Location loc = player.getEyeLocation();
		SphereLocater sphere = new SphereLocater(loc, radius);
		for (LivingEntity entity : sphere.getUnobstructed(loc, false, LivingEntity.class))
		{
			for (PotionEffect effect : entity.getActivePotionEffects())
			{
				didSomething = true;
				entity.removePotionEffect(effect.getType());
			}
		}
		return didSomething;
	}
}
