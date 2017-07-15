package com.hepolite.racialtraits.ability;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.hepolite.coreutility.apis.damage.Damage;
import com.hepolite.coreutility.apis.damage.DamageAPI;
import com.hepolite.coreutility.apis.damage.DamageType;
import com.hepolite.coreutility.apis.entites.EntityLocater.LineLocater;
import com.hepolite.racialtraits.ability.components.ComponentResourceCost;
import com.hepolite.racialtraits.race.Race;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityHoofStrike extends Ability
{
	private final ComponentResourceCost resourceCost;

	public AbilityHoofStrike(Race race)
	{
		super(race, "Hoof Strike");
		resourceCost = new ComponentResourceCost(race.getResource());
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		int level = skill.getLevel();
		float range = getSettings().getFloat("Level " + level + ".range");
		float damage = getSettings().getFloat("Level " + level + ".damage");
		float cost = getSettings().getFloat("Level " + level + ".cost");

		if (!resourceCost.has(player, cost))
		{
			player.sendMessage(ChatColor.RED + "You are too tired to do this!");
			return false;
		}

		Location start = player.getEyeLocation();
		LineLocater line = new LineLocater(start, start.getDirection().normalize().multiply(range));

		boolean hitSomething = false;
		LivingEntity target = line.getFirstUnobstructed(player.getEyeLocation(), player, true,
				LivingEntity.class);
		if (target != null)
		{
			if (DamageAPI.damage(target, player, new Damage(DamageType.BLUNT, damage)))
				hitSomething = true;
		}

		if (hitSomething)
			resourceCost.consume(player, cost);
		return hitSomething;
	}
}
