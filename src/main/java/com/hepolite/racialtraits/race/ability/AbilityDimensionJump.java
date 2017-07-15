package com.hepolite.racialtraits.race.ability;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.hepolite.coreutility.CoreUtility;
import com.hepolite.coreutility.chat.Message;
import com.hepolite.coreutility.util.TeleportHelper;
import com.hepolite.coreutility.util.effects.EffectHelper;
import com.hepolite.racialtraits.cmd.InstructionAbilityDimensionJump;
import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.race.components.ComponentTimer;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityDimensionJump extends Ability
{
	private final ComponentTimer timer = new ComponentTimer();
	private final Map<UUID, String> targets = new HashMap<UUID, String>();

	public AbilityDimensionJump(Race race)
	{
		super(race, "Dimension Jump");
	}

	@Override
	public final void onTick(Player player, PlayerSkill skill, int tick)
	{
		if (!timer.isLastTick(player))
			return;

		if (performTeleport(player))
			cast(player);
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		if (!isInValidWorld(player))
		{
			player.sendMessage(ChatColor.RED + "Your magic fails to respond in this dimension");
			return false;
		}

		InstructionAbilityDimensionJump.addInvoker(player, this);

		// "Click here to select dimension: [DimA] [DimB] [DimC]"
		// "Alternatively, run /changeweather <dimension>"

		Message message = new Message(ChatColor.AQUA + "Click here to select dimension: ");
		for (String dimension : getSettings().getKeys("worlds"))
		{
			float scaleH = getSettings().getFloat("worlds." + dimension + ".scaleH");
			float scaleV = getSettings().getFloat("worlds." + dimension + ".scaleV");

			message.addCommand(String.format("%s[%s]", ChatColor.WHITE, dimension), String.format("%sDimension %s%s%s [scale %s%.3f/%.3f%s]", ChatColor.AQUA, ChatColor.WHITE, dimension, ChatColor.AQUA, ChatColor.WHITE, scaleH, scaleV, ChatColor.AQUA), "/race Ability DimensionJump " + dimension);
			message.addText(" ");
		}
		message.send(player);
		player.sendMessage(ChatColor.AQUA + "Alternatively, run /dimensionjump <dimension>");
		return false;
	}

	/** Returns true if the player is in a world where the ability can be used */
	private final boolean isInValidWorld(Player player)
	{
		Set<String> worlds = getSettings().getKeys("worlds");
		return worlds.contains(player.getWorld().getName().toLowerCase());
	}

	/** Returns true if the ability is valid for the given player */
	public final boolean performValidationTest(Player player)
	{
		return validate(player) && !timer.isActive(player) && isInValidWorld(player);
	}

	/** Performs the ability cast, invoked from the instruction */
	public final void performAbility(Player player, String target)
	{
		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 1.0f, 0.0f);
		timer.start(player, getSettings().getFloat("delay"));
		targets.put(player.getUniqueId(), target);
	}

	/** Performs the teleporting; returns true if the player did teleport */
	private final boolean performTeleport(Player player)
	{
		String targetWorldName = targets.get(player.getUniqueId());
		World targetWorld = targetWorldName == null ? null : Bukkit.getWorld(targetWorldName);
		if (targetWorld == null)
		{
			player.sendMessage(ChatColor.RED + "The target dimension " + ChatColor.WHITE + targetWorldName + ChatColor.RED + " is invalid!");
			return false;
		}

		String cwn = player.getWorld().getName().toLowerCase();
		String twn = targetWorldName.toLowerCase();
		float fromScaleH = getSettings().getFloat("worlds." + cwn + ".scaleH");
		float fromScaleV = getSettings().getFloat("worlds." + cwn + ".scaleV");
		float toScaleH = getSettings().getFloat("worlds." + twn + ".scaleH");
		float toScaleV = getSettings().getFloat("worlds." + twn + ".scaleV");
		Location current = player.getLocation();
		current.setWorld(targetWorld);
		Location scaled = TeleportHelper.scale(current, fromScaleH, fromScaleV, toScaleH, toScaleV);
		Location target = TeleportHelper.findSafe(scaled);
		if (target == null)
		{
			player.sendMessage(ChatColor.RED + "Was unable to find a safe target location");
			return false;
		}

		EffectHelper.teleportEffect(player.getEyeLocation(), 1.0f);
		player.teleport(target);
		CoreUtility.getMovementHandler().setFallDistance(player, 0.0f);
		EffectHelper.teleportEffect(player.getEyeLocation(), 1.0f);
		return true;
	}
}
