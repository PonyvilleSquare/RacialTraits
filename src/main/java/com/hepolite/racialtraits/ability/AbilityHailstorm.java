package com.hepolite.racialtraits.ability;

import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.hepolite.coreutility.apis.damage.Damage;
import com.hepolite.coreutility.apis.damage.DamageAPI;
import com.hepolite.coreutility.apis.damage.DamageType;
import com.hepolite.coreutility.apis.entites.EntityLocater.SphereLocater;
import com.hepolite.coreutility.settings.Settings;
import com.hepolite.coreutility.util.effects.EffectHelper;
import com.hepolite.racialtraits.ability.components.ComponentItemCost;
import com.hepolite.racialtraits.ability.components.ComponentTimer;
import com.hepolite.racialtraits.race.Race;
import com.sucy.skill.api.player.PlayerSkill;

import de.slikey.effectlib.effect.SphereEffect;
import de.slikey.effectlib.util.ParticleEffect;

public class AbilityHailstorm extends Ability
{
	private final ComponentItemCost itemCost = new ComponentItemCost();
	private final ComponentTimer timer = new ComponentTimer();

	public AbilityHailstorm(Race race)
	{
		super(race, "Hailstorm");
	}

	@Override
	public final void onTick(Player player, PlayerSkill skill, int tick)
	{
		if (!timer.isActive(player))
			return;

		if (tick % 20 == 0)
			handleDamage(player, skill);
		if (tick % 11 == 0)
			handleSound(player);
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		int level = skill.getLevel();
		Settings settings = getSettings();
		Collection<ItemStack> items = settings.getItems("cost");
		float duration = settings.getInt("Level " + level + ".duration");
		float radius = settings.getFloat("Level " + level + ".radius");

		if (!itemCost.has(player, items))
		{
			player.sendMessage(ChatColor.RED + "You still need the following resources:");
			player.sendMessage(itemCost.findMissing(player, items).toArray(new String[0]));
			return false;
		}
		timer.start(player, duration);
		itemCost.consume(player, items);

		SphereEffect effect = new SphereEffect(EffectHelper.getInstance());
		effect.setEntity(player);
		effect.radius = radius;
		effect.particle = ParticleEffect.SNOW_SHOVEL;
		effect.duration = 50 * timer.getRemainingTimeAsTicks(player);
		effect.speed = 0.05f;
		effect.start();
		return true;
	}

	/** Processes the damage stuff */
	private final void handleDamage(Player player, PlayerSkill skill)
	{
		int level = skill.getLevel();
		Settings settings = getSettings();
		float radius = settings.getFloat("Level " + level + ".radius");
		float damage = settings.getFloat("Level " + level + ".damage");
		Damage dmg = new Damage(DamageType.FROST, damage);

		Location start = player.getEyeLocation();
		SphereLocater sphere = new SphereLocater(start, radius);
		for (LivingEntity entity : sphere.getUnobstructed(start, true, LivingEntity.class))
		{
			if (entity == player)
				continue;
			DamageAPI.damage(entity, player, dmg);
		}
	}

	/** Handles the sound of the hailstorm */
	private final void handleSound(Player player)
	{
		player.getWorld().playSound(player.getLocation(), Sound.WEATHER_RAIN, 0.35f, 2.0f);
	}
}
