package com.hepolite.racialtraits.race.ability;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.hepolite.coreutility.CoreUtility;
import com.hepolite.coreutility.apis.raytrace.RaytraceAPI;
import com.hepolite.coreutility.event.events.PlayerFlightAllowEvent;
import com.hepolite.coreutility.hunger.Node;
import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.resource.Resource;
import com.hepolite.racialtraits.resource.ResourceExhaustion;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityFlyer extends Ability
{
	private final Set<UUID> landlocked = new HashSet<UUID>();
	private final Resource resource = new ResourceExhaustion();

	public AbilityFlyer(Race race)
	{
		super(race, "Flyer");
	}

	@Override
	public final void onInitialize(Player player, int level)
	{
		resource.addPlayer(player);
	}

	@Override
	public final void onDeinitialize(Player player)
	{
		resource.removePlayer(player);
	}

	@Override
	public final void onTick(Player player, PlayerSkill skill, int tick)
	{
		float max = getSettings().getFloat("max");
		float tired = getSettings().getFloat("tired");
		float untired = getSettings().getFloat("untired");

		Node node = CoreUtility.getHungerHandler().getHungerNode(player);
		UUID uuid = player.getUniqueId();

		if (!player.isFlying() && !player.isGliding() && !landlocked.contains(uuid))
			resource.setResource(player, 0.0f);
		else
			resource.setResource(player, node.getExhaustion() / max);

		if (node.getExhaustion() >= tired && !landlocked.contains(uuid))
		{
			player.sendMessage(ChatColor.RED + "You are now too tired to take off if you land!");
			resource.getBossBar(player).setColor(BarColor.RED);
			landlocked.add(uuid);
		}
		if (node.getExhaustion() <= untired && landlocked.contains(uuid))
		{
			player.sendMessage(ChatColor.AQUA + "You are no longer too tired to take off if you land");
			resource.getBossBar(player).setColor(BarColor.BLUE);
			landlocked.remove(uuid);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public final void onPlayerFlightCheck(PlayerFlightAllowEvent event)
	{
		Player player = event.getPlayer();
		if (!validate(player))
			return;

		UUID uuid = player.getUniqueId();
		Location start = player.getLocation();
		Node node = CoreUtility.getHungerHandler().getHungerNode(player);

		if (landlocked.contains(uuid) && RaytraceAPI.rayTrace(start, start.clone().add(0.0, -4.0, 0.0), true) != null)
			event.setFligthAllowed(false);
		else if (node.getExhaustion() >= getSettings().getFloat("fall"))
			event.setFligthAllowed(false);
	}
}
