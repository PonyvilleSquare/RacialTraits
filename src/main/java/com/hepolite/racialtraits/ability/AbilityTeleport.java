package com.hepolite.racialtraits.ability;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.hepolite.coreutility.CoreUtility;
import com.hepolite.coreutility.apis.entites.EntityLocater.SphereLocater;
import com.hepolite.coreutility.util.effects.EffectHelper;
import com.hepolite.racialtraits.ability.components.ComponentTimer;
import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.skillapi.SkillAPIHelper;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityTeleport extends Ability
{
	private final ComponentTimer timer = new ComponentTimer();

	public AbilityTeleport(Race race, String name)
	{
		super(race, name);
	}

	@Override
	public final void onTick(Player player, PlayerSkill skill, int tick)
	{
		if (!timer.isLastTick(player))
			return;

		if (performTeleport(player, skill))
			cast(player);
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		if (timer.isActive(player))
		{
			player.sendMessage(ChatColor.RED + "You have already started channeling the spell...");
			return false;
		}

		float duration = getSettings().getFloat("Level " + skill.getLevel() + ".delay");
		if (duration == 0.0f)
			return performTeleport(player, skill);
		else
		{
			player.getWorld().playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 1.0f, 0.0f);
			timer.start(player, duration);
		}
		return false;
	}

	/** Performs the teleporting; returns true if the player did teleport */
	private final boolean performTeleport(Player player, PlayerSkill skill)
	{
		Location target = findLocation(player, skill);
		if (target == null)
		{
			player.sendMessage(ChatColor.RED + "No destination was found");
			return false;
		}
		target.setDirection(player.getEyeLocation().getDirection());

		float radius = getSettings().getFloat("Level " + skill.getLevel() + ".radius");
		Collection<LivingEntity> entities = new ArrayList<LivingEntity>();
		if (radius == 0.0f)
			entities.add(player);
		else
		{
			SphereLocater sphere = new SphereLocater(player.getEyeLocation(), radius);
			entities.addAll(sphere.getUnobstructed(player.getEyeLocation(), false, LivingEntity.class));
		}

		if (entities.size() == 0)
		{
			player.sendMessage(ChatColor.RED + "Found nobody to teleport. How did you manage to fail this badly...?");
			return false;
		}

		Vector velocity = player.getVelocity();
		float fallDistance = CoreUtility.getMovementHandler().getFallDistance(player);
		if (SkillAPIHelper.getSkill(player, getSettings().getString("skill.upgrade")) != null)
		{
			velocity.setX(0.0).setY(0.0).setZ(0.0);
			fallDistance = 0.0f;
		}

		EffectHelper.teleportEffect(player.getEyeLocation(), 1.0f);
		for (LivingEntity entity : entities)
		{
			entity.teleport(target);
			entity.setVelocity(velocity);
			CoreUtility.getMovementHandler().setFallDistance(entity, fallDistance);
		}
		EffectHelper.teleportEffect(player.getEyeLocation(), 1.0f);
		return true;
	}

	/** Finds a destination for the teleporting */
	private final Location findLocation(Player player, PlayerSkill skill)
	{
		int range = getSettings().getInt("Level " + skill.getLevel() + ".range");
		Location start = player.getEyeLocation();
		Location open = null;
		Location target = null;

		Block previous = null;
		BlockIterator it = new BlockIterator(start, 0.0, range);
		while (it.hasNext())
		{
			Block block = it.next();
			if (block.getType().isSolid())
			{
				if (previous == null)
					continue;
				if (canTeleportThrough(previous.getRelative(0, 1, 0)) && !block.getRelative(0, 1, 0).getType().isSolid() && !block.getRelative(0, 2, 0).getType().isSolid())
					open = block.getRelative(0, 1, 0).getLocation();
				target = open;
			}
			else
			{
				if (!block.getRelative(0, 1, 0).getType().isSolid())
					open = block.getLocation();
			}

			if (block.isLiquid() || !canTeleportThrough(block))
				break;
			previous = block;
		}
		return target == null ? null : target.add(0.5, 0.0, 0.5);
	}

	/** Returns true if the player can teleport through the given block */
	private final boolean canTeleportThrough(Block block)
	{
		if (!block.getType().isSolid())
			return true;
		switch (block.getType())
		{
		case ACACIA_DOOR:
		case ACACIA_FENCE:
		case ACACIA_FENCE_GATE:
		case BIRCH_DOOR:
		case BIRCH_FENCE:
		case BIRCH_FENCE_GATE:
		case DARK_OAK_DOOR:
		case DARK_OAK_FENCE:
		case DARK_OAK_FENCE_GATE:
		case FENCE:
		case FENCE_GATE:
		case FLOWER_POT:
		case GLASS:
		case ICE:
		case IRON_DOOR:
		case IRON_FENCE:
		case IRON_TRAPDOOR:
		case JUNGLE_DOOR:
		case JUNGLE_FENCE:
		case LADDER:
		case LEAVES:
		case LEAVES_2:
		case MOB_SPAWNER:
		case NETHER_FENCE:
		case SPRUCE_DOOR:
		case SPRUCE_FENCE:
		case SPRUCE_FENCE_GATE:
		case STAINED_GLASS:
		case STAINED_GLASS_PANE:
		case THIN_GLASS:
		case TRAP_DOOR:
		case WATER_LILY:
		case WOODEN_DOOR:
			return true;
		default:
			return false;
		}
	}
}
