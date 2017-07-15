package com.hepolite.racialtraits.ability;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.hepolite.coreutility.apis.damage.Damage;
import com.hepolite.coreutility.apis.damage.DamageAPI;
import com.hepolite.coreutility.apis.damage.DamageType;
import com.hepolite.coreutility.apis.entites.EntityLocater.ConeLocater;
import com.hepolite.racialtraits.ability.components.ComponentResourceCost;
import com.hepolite.racialtraits.race.Race;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityKick extends Ability
{
	private final ComponentResourceCost resourceCost;

	public AbilityKick(Race race)
	{
		super(race, "Kick");
		resourceCost = new ComponentResourceCost(race.getResource());
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		int level = skill.getLevel();
		float angle = getSettings().getFloat("Level " + level + ".angle");
		float radius = getSettings().getFloat("Level " + level + ".radius");
		float damage = getSettings().getFloat("Level " + level + ".damage");
		float knockback = getSettings().getFloat("Level " + level + ".knockback");
		float lift = getSettings().getFloat("Level " + level + ".lift");
		float cost = getSettings().getFloat("Level " + level + ".cost");

		if (!resourceCost.has(player, cost))
		{
			player.sendMessage(ChatColor.RED + "You are too tired to do this!");
			return false;
		}

		ConeLocater cone = new ConeLocater(player.getLocation(), player.getEyeLocation()
				.getDirection().multiply(-radius), angle);

		boolean hitSomething = false;
		for (LivingEntity entity : cone.getUnobstructed(player.getEyeLocation(), false,
				LivingEntity.class))
		{
			if (entity == player)
				continue;
			if (DamageAPI.damage(entity, player, new Damage(DamageType.BLUNT, damage)))
			{
				DamageAPI.applyKnockback(entity, player.getLocation(), knockback, lift);
				hitSomething = true;
			}
		}

		if (hitSomething)
			resourceCost.consume(player, cost);
		return hitSomething;
	}
}
