package com.hepolite.racialtraits.race.ability;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.hepolite.coreutility.apis.damage.Damage;
import com.hepolite.coreutility.apis.damage.DamageAPI;
import com.hepolite.coreutility.apis.damage.DamageType;
import com.hepolite.coreutility.apis.entites.EntityLocater.SphereLocater;
import com.hepolite.racialtraits.race.Race;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityRepel extends Ability
{
	public AbilityRepel(Race race)
	{
		super(race, "Repel");
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		float radius = getSettings().getFloat("radius");
		float force = getSettings().getFloat("force");
		float lift = getSettings().getFloat("lift");
		float damage = getSettings().getFloat("damage");

		Damage dmg = new Damage(DamageType.BLUNT, damage);
		boolean didSomething = false;

		SphereLocater sphere = new SphereLocater(player.getEyeLocation(), radius);
		for (LivingEntity entity : sphere.getUnobstructed(player.getEyeLocation(), true,
				LivingEntity.class))
		{
			if (entity == player)
				continue;
			if (DamageAPI.damage(entity, player, dmg))
			{
				DamageAPI.applyKnockback(entity, player.getLocation(), force, lift);
				didSomething = true;
			}
		}
		return didSomething;
	}
}
