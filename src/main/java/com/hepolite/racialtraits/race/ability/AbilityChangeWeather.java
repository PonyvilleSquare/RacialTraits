package com.hepolite.racialtraits.race.ability;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;

import com.hepolite.coreutility.chat.Message;
import com.hepolite.racialtraits.cmd.InstructionAbilityChangeWeather;
import com.hepolite.racialtraits.race.Race;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityChangeWeather extends Ability
{
	public AbilityChangeWeather(Race race)
	{
		super(race, "Change Weather");
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		InstructionAbilityChangeWeather.addInvoker(player, this);

		// "Click here to select weather: [Sun] [Rain] [Storm]"
		// "Alternatively, run /changeweather sun|rain|storm"

		Message message = new Message(ChatColor.AQUA + "Click here to select weather: ");
		message.addCommand(ChatColor.WHITE + "[Sun]", ChatColor.AQUA
				+ "Stops all and any rain and storms", "/race Ability ChangeWeather sun");
		message.addText(" ");
		message.addCommand(ChatColor.WHITE + "[Rain]", ChatColor.AQUA
				+ "Moves clouds into position\n" + ChatColor.AQUA + "to drop water everywhere",
				"/race Ability ChangeWeather rain");
		message.addText(" ");
		message.addCommand(ChatColor.WHITE + "[Storm]", ChatColor.AQUA
				+ "Forces everybody in the\n" + ChatColor.AQUA + "world to deal with lightning",
				"/race Ability ChangeWeather storm");
		message.send(player);
		player.sendMessage(ChatColor.AQUA + "Alternatively, run /changeweather sun|rain|storm");
		return false;
	}
}
