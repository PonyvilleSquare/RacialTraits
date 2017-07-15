package com.hepolite.racialtraits.ability;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;

import com.hepolite.racialtraits.ability.components.ComponentMovementModifier;
import com.hepolite.racialtraits.ability.components.ComponentStaminaModifier;
import com.hepolite.racialtraits.race.Race;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilitySwiftness extends Ability
{
	private final ComponentMovementModifier movementModifier;
	private final ComponentStaminaModifier staminaModifier;

	private final Set<UUID> activePlayers = new HashSet<UUID>();

	public AbilitySwiftness(Race race)
	{
		super(race, "Swiftness");
		movementModifier = new ComponentMovementModifier(getName());
		staminaModifier = new ComponentStaminaModifier(getName());
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		if (activePlayers.contains(player.getUniqueId()))
			deinit(player);
		else
			init(player);
		return false;
	}

	@Override
	public final void onTick(Player player, PlayerSkill skill, int tick)
	{
		if (!activePlayers.contains(player.getUniqueId()))
			return;
		if (getRace().getResource().getRemaining(player) <= 0.0f)
			deinit(player);
	}

	@Override
	public final void onDeinitialize(Player player)
	{
		movementModifier.wipe(player);
		staminaModifier.wipe(player);
	}

	/** Initializes the speed boost for the given player */
	private final void init(Player player)
	{
		player.sendMessage(ChatColor.WHITE + "You are now moving swiftly!");
		activePlayers.add(player.getUniqueId());

		float walk = getSettings().getFloat("walk");
		float stamina = getSettings().getFloat("stamina");
		movementModifier.setWalk(player, 0.0f, walk, 0.0f);
		staminaModifier.setRegen(player, 0.0f, 0.0f, stamina);
	}

	/** Deinitializes the speed bost for the player */
	private final void deinit(Player player)
	{
		player.sendMessage(ChatColor.WHITE + "You are no longer moving swiftly...");
		activePlayers.remove(player.getUniqueId());
		onDeinitialize(player);
	}
}
