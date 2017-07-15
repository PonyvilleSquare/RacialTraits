package com.hepolite.racialtraits.ability;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.hepolite.coreutility.apis.entites.EntityLocater.LineLocater;
import com.hepolite.coreutility.chat.Message;
import com.hepolite.racialtraits.RacialTraits;
import com.hepolite.racialtraits.ability.components.ComponentHungerModifier;
import com.hepolite.racialtraits.ability.components.ComponentMovementModifier;
import com.hepolite.racialtraits.race.Race;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityCarry extends Ability
{
	private final ComponentMovementModifier speedModifier;
	private final ComponentHungerModifier hungerModifier;

	public AbilityCarry(Race race, String name)
	{
		super(race, name);
		speedModifier = new ComponentMovementModifier(getName());
		hungerModifier = new ComponentHungerModifier(getName());
	}

	@Override
	public final void onTick(Player player, PlayerSkill skill, int tick)
	{
		if (player.getPassenger() == null || tick % 20 != 0)
			return;

		speedModifier.setFly(player, 0.0f, getSettings().getFloat("speed"), 0.0f, 3);
		hungerModifier.setDecrease(player, 0.0f, getSettings().getFloat("hunger"), 0.0f, 3);
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		Location eye = player.getEyeLocation();
		LineLocater line = new LineLocater(eye, eye.getDirection().multiply(5.0));
		Player target = line.getFirstUnobstructed(eye, player, true, Player.class);

		if (target == null)
			player.sendMessage(ChatColor.RED + "Found no players in front of you to carry");
		else
		{
			if (hasRequest(target))
				player.sendMessage(ChatColor.WHITE + target.getDisplayName() + ChatColor.RED + " has already received a request");
			else
			{
				Message message = new Message(ChatColor.WHITE + player.getDisplayName() + ChatColor.AQUA + " wants to carry you.\n");
				message.addText(ChatColor.AQUA + "Use /carry accept or ");
				message.addCommand("[click here]", ChatColor.AQUA + "Request from " + ChatColor.WHITE + player.getDisplayName(), "/race Ability Carry");
				message.addText(ChatColor.AQUA + " to accept");
				message.send(target);

				player.sendMessage(ChatColor.AQUA + "Sent a request to " + ChatColor.WHITE + target.getDisplayName());
				sendRequest(player, target);
			}
		}
		return false;
	}

	// /////////////////////////////////////////////////////////////////

	public class Request
	{
		public final UUID uuid;
		public final int timeout;

		public Request(Player player, int timeout)
		{
			this.uuid = player.getUniqueId();
			this.timeout = timeout;
		}
	}

	private final static Map<UUID, Request> requests = new HashMap<UUID, Request>();

	/** Returns true if the given player has a request sent to them already */
	private final static boolean hasRequest(Player player)
	{
		Request request = requests.get(player.getUniqueId());
		return (request != null && request.timeout > RacialTraits.getCurrentTick());
	}

	/** Sends a request to the given target from the given player */
	private final void sendRequest(Player player, Player target)
	{
		requests.put(target.getUniqueId(), new Request(player, RacialTraits.getCurrentTick() + 200));
	}

	/** Returns the request for the given player */
	public final static Request getRequest(Player player)
	{
		return hasRequest(player) ? requests.get(player.getUniqueId()) : null;
	}
}
