package com.hepolite.racialtraits.ability;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.hepolite.coreutility.apis.damage.Damage;
import com.hepolite.coreutility.apis.damage.DamageAPI;
import com.hepolite.coreutility.apis.damage.DamageType;
import com.hepolite.coreutility.apis.entites.EntityLocater.SphereLocater;
import com.hepolite.racialtraits.ability.components.ComponentResourceCost;
import com.hepolite.racialtraits.race.Race;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityGroundPound extends Ability
{
	private final ComponentResourceCost resourceCost;

	public AbilityGroundPound(Race race)
	{
		super(race, "Ground Pound");
		resourceCost = new ComponentResourceCost(race.getResource());
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		int level = skill.getLevel();
		float radius = getSettings().getFloat("Level " + level + ".radius");
		float damage = getSettings().getFloat("Level " + level + ".damage");
		float lift = getSettings().getFloat("Level " + level + ".lift");
		float cost = getSettings().getFloat("Level " + level + ".cost");

		if (!resourceCost.has(player, cost))
		{
			player.sendMessage(ChatColor.RED + "You are too tired to do this!");
			return false;
		}

		SphereLocater cone = new SphereLocater(player.getLocation(), radius);

		boolean hitSomething = false;
		for (LivingEntity entity : cone.getUnobstructed(player.getEyeLocation(), true,
				LivingEntity.class))
		{
			if (entity == player)
				continue;
			if (DamageAPI.damage(entity, player, new Damage(DamageType.BLUNT, damage)))
			{
				DamageAPI.applyKnockback(entity, player.getLocation(), 0.0f, lift);
				hitSomething = true;
			}
		}

		if (hitSomething)
			resourceCost.consume(player, cost);
		return hitSomething;
	}
}
