package com.hepolite.racialtraits.ability;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.util.Vector;

import com.hepolite.coreutility.CoreUtility;
import com.hepolite.coreutility.apis.raytrace.RaytraceAPI;
import com.hepolite.racialtraits.ability.components.ComponentToggle;
import com.hepolite.racialtraits.race.Race;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityGlide extends Ability
{
	private final ComponentToggle toggle = new ComponentToggle(true);

	public AbilityGlide(Race race)
	{
		super(race, "Glide");
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		toggle.toggle(player);
		if (toggle.get(player))
			player.sendMessage(ChatColor.AQUA + "You will now automatically start gliding when falling");
		else
			player.sendMessage(ChatColor.RED + "You will no longer automatically glide when falling");
		return false;
	}

	@Override
	public final void onTick(Player player, PlayerSkill skill, int tick)
	{
		float fallDistance = CoreUtility.getMovementHandler().getFallDistance(player);
		float triggerDistance = getSettings().getFloat("distance");

		if (!toggle.get(player) || fallDistance < triggerDistance)
			return;
		player.setGliding(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public final void onPlayerToggleGlide(EntityToggleGlideEvent event)
	{
		if (event.getEntityType() != EntityType.PLAYER)
			return;
		Location start = event.getEntity().getLocation();
		if (!event.isGliding() && RaytraceAPI.rayTrace(start, new Vector(0, -1, 0), true, true, false) == null)
			event.setCancelled(true);
	}
}
