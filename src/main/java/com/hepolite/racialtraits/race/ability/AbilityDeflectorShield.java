package com.hepolite.racialtraits.race.ability;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.hepolite.coreutility.apis.damage.DamageClass;
import com.hepolite.coreutility.event.events.DamageEvent;
import com.hepolite.coreutility.util.effects.EffectHelper;
import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.race.components.ComponentTimer;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityDeflectorShield extends Ability
{
	private final ComponentTimer timer = new ComponentTimer();
	private final Map<UUID, Integer> charges = new HashMap<UUID, Integer>();

	public AbilityDeflectorShield(Race race)
	{
		super(race, "Deflector Shield");
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		int level = skill.getLevel();
		float duration = getSettings().getFloat("Level " + level + ".duration");
		int charge = getSettings().getInt("Level " + level + ".charges");

		timer.start(player, duration);
		charges.put(player.getUniqueId(), charge);
		return true;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public final void onDamage(DamageEvent event)
	{
		DamageClass damageClass = event.getDamage().getDamageClass();
		if (damageClass == DamageClass.PERSONAL || damageClass == DamageClass.TRUE)
			return;
		Player player = event.getTargetAsPlayer();
		if (player == null || !validate(player) || !timer.isActive(player))
			return;
		UUID uuid = player.getUniqueId();
		if (!charges.containsKey(uuid))
			return;

		Location location = player.getLocation().add(0.0, 0.5 * player.getEyeHeight(), 0.0);
		EffectHelper.magicalDischarge(location);
		int charge = charges.get(uuid) - 1;
		if (charge > 0)
			charges.put(uuid, charge);
		else
			charges.remove(uuid);
		event.setCancelled(true);
	}
}
