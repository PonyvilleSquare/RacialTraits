package com.hepolite.racialtraits.ability;

import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.hepolite.coreutility.apis.damage.Damage;
import com.hepolite.coreutility.apis.damage.DamageAPI;
import com.hepolite.coreutility.apis.damage.DamageType;
import com.hepolite.coreutility.apis.entites.EntityLocater.LineLocater;
import com.hepolite.coreutility.apis.entites.EntityLocater.SphereLocater;
import com.hepolite.coreutility.apis.raytrace.RaytraceAPI;
import com.hepolite.coreutility.settings.Settings;
import com.hepolite.coreutility.util.effects.EffectHelper;
import com.hepolite.racialtraits.ability.components.ComponentItemCost;
import com.hepolite.racialtraits.race.Race;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityLightning extends Ability
{
	private final ComponentItemCost itemCost = new ComponentItemCost();

	public AbilityLightning(Race race)
	{
		super(race, "Lightning");
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		Settings settings = getSettings();
		int level = skill.getLevel();
		Collection<ItemStack> items = settings.getItems("cost");
		float damage = settings.getFloat("Level " + level + ".damage");
		float radius = settings.getFloat("Level " + level + ".radius");
		float segmentSize = settings.getFloat("visual.segmentSize");
		float displacementRatio = settings.getFloat("visual.displacementRatio");
		float branchChance = settings.getFloat("visual.branchChance");

		if (!itemCost.has(player, items))
		{
			player.sendMessage(ChatColor.RED + "You still need the following resources:");
			player.sendMessage(itemCost.findMissing(player, items).toArray(new String[0]));
			return false;
		}

		Location start = player.getEyeLocation().add(0.0, -0.5, 0.0);
		Vector direction = start.getDirection().multiply(100.0);
		Location end;

		LineLocater line = new LineLocater(start, direction);
		LivingEntity target = line.getFirstUnobstructed(start, player, true, LivingEntity.class);
		if (target == null)
		{
			end = RaytraceAPI.rayTrace(start, direction, true, true, false);
			if (end != null)
				end.subtract(direction.normalize().multiply(2.0));
		}
		else
			end = target.getLocation().add(0.0, 0.5 * target.getEyeHeight(), 0.0);
		if (end == null)
		{
			player.sendMessage(ChatColor.RED + "No target was found");
			return false;
		}

		SphereLocater sphere = new SphereLocater(end, radius);
		for (LivingEntity entity : sphere.get(LivingEntity.class))
		{
			if (entity == player)
				continue;
			DamageAPI.damage(entity, player, new Damage(DamageType.ELECTRICITY, damage));
		}
		EffectHelper.electricalDischarge(start);
		EffectHelper.electricalDischarge(end);
		EffectHelper.electricalBolt(start, end, segmentSize, displacementRatio, branchChance);
		itemCost.consume(player, items);
		return true;
	}
}
