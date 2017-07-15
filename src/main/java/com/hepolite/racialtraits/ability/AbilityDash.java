package com.hepolite.racialtraits.ability;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.hepolite.coreutility.apis.damage.Damage;
import com.hepolite.coreutility.apis.damage.DamageAPI;
import com.hepolite.coreutility.apis.damage.DamageType;
import com.hepolite.coreutility.apis.entites.EntityLocater.ConeLocater;
import com.hepolite.coreutility.apis.raytrace.RaytraceAPI;
import com.hepolite.coreutility.movement.MovementConstants;
import com.hepolite.racialtraits.ability.components.ComponentTimer;
import com.hepolite.racialtraits.race.Race;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityDash extends Ability
{
	private final ComponentTimer timer = new ComponentTimer();

	public AbilityDash(Race race)
	{
		super(race, "Dash");
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		if (!player.isFlying() && !player.isGliding())
		{
			player.sendMessage(ChatColor.RED + "You may only use this while flying");
			return false;
		}
		if (player.getPassenger() != null)
		{
			player.sendMessage(ChatColor.RED + "You cannot do this while carrying someone!");
			return false;
		}

		timer.start(player, getSettings().getFloat("Level " + skill.getLevel() + ".duration"));
		player.setVelocity(player.getEyeLocation().getDirection());
		return true;
	}

	@Override
	public final void onTick(Player player, PlayerSkill skill, int tick)
	{
		if (!timer.isActive(player))
			return;

		int level = skill.getLevel();
		float speed = getSettings().getFloat("Level " + level + ".speed");
		float rate = getSettings().getFloat("Level " + level + ".rate");

		Vector vel = player.getVelocity();
		if (vel.lengthSquared() > 0.0001)
			vel.normalize();
		vel.multiply(1.0f - rate);
		vel.add(player.getEyeLocation().getDirection().multiply(rate));
		vel.multiply(speed);

		boolean lastTick = timer.isLastTick(player);
		player.setFlying(lastTick);
		player.setFallDistance(0.0f);
		player.setGliding(false);
		player.setVelocity(vel.setY(vel.getY() + MovementConstants.GRAVITY));

		handleEntityCollision(player, skill);
		handleWorldCollision(player);
	}

	/** Perform collision detection */
	private final void handleWorldCollision(Player player)
	{
		Vector dir = player.getVelocity().multiply(5.0);
		Location eyeTarget = RaytraceAPI.rayTrace(player.getEyeLocation(), dir, true, true, false);
		Location footTarget = RaytraceAPI.rayTrace(player.getLocation(), dir, true, true, false);
		if (eyeTarget != null || footTarget != null)
		{
			player.setFlying(false);
			timer.stop(player);
		}
	}

	/** Perform entity collision detection */
	private final void handleEntityCollision(Player player, PlayerSkill skill)
	{
		int level = skill.getLevel();
		float damage = getSettings().getFloat("Level " + level + ".damage");
		float angle = getSettings().getFloat("cone.angle");
		float range = getSettings().getFloat("cone.range");

		boolean hitSomething = false;
		Damage dmg = new Damage(DamageType.NORMAL, damage);

		Location start = (player.getEyeLocation().add(player.getLocation())).multiply(0.5);
		ConeLocater cone = new ConeLocater(start, player.getVelocity().multiply(range), angle);
		for (LivingEntity entity : cone.getUnobstructed(start, true, LivingEntity.class))
		{
			if (entity == player)
				continue;
			hitSomething |= DamageAPI.damage(entity, player, dmg);
			if (hitSomething)
				break;
		}

		if (hitSomething)
		{
			player.setFlying(true);
			timer.stop(player);
		}
	}
}
