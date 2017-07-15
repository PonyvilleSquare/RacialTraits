package com.hepolite.racialtraits.race.ability;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.hepolite.coreutility.apis.attributes.AttributeAPI;
import com.hepolite.coreutility.apis.attributes.AttributeType;
import com.hepolite.coreutility.apis.entites.EntityLocater.LineLocater;
import com.hepolite.coreutility.event.events.PlayerFlightAllowEvent;
import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.race.components.ComponentTimer;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityHold extends Ability
{
	private final ComponentTimer timer = new ComponentTimer();

	public AbilityHold(Race race)
	{
		super(race, "Hold");
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		float range = getSettings().getFloat("range");
		int duration = getSettings().getInt("duration");

		Location start = player.getEyeLocation();
		LineLocater line = new LineLocater(start, start.getDirection().multiply(range));
		LivingEntity target = line.getFirstUnobstructed(start, player, false, LivingEntity.class);
		if (target == null)
		{
			player.sendMessage(ChatColor.RED + "There was no valid target in front of you!");
			return false;
		}

		if (target instanceof Player)
		{
			timer.start((Player) target, duration);
			AttributeAPI.get(target, AttributeType.SPEED_WALK).getModifier(getName())
					.setLifetime(duration).setScale(-1.0f);
			AttributeAPI.get(target, AttributeType.SPEED_FLY).getModifier(getName())
					.setLifetime(duration).setScale(-1.0f);
		}
		else
			target.addPotionEffect(
					new PotionEffect(PotionEffectType.SLOW, 20 * duration, 10, true), true);
		return true;
	}

	@Override
	public final void onTick(int tick)
	{
		for (Player player : Bukkit.getOnlinePlayers())
		{
			if (timer.isActive(player))
			{
				player.setAllowFlight(true);
				player.setFlying(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public final void onFlightCheck(PlayerFlightAllowEvent event)
	{
		if (timer.equals(event.getPlayer()))
			event.setFligthAllowed(true);
	}
}
