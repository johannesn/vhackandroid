package com.devjam.tamagotchi.comm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.devjam.tamagotchi.game.Monster;

public class MonsterFactory {

	public static void writeMonster(DataOutputStream out, Monster monster)
			throws IOException {
		out.writeInt(monster.getHead());
		out.writeInt(monster.getTorso());
		out.writeInt(monster.getLegs());
		out.writeInt(monster.getSkinColor());
		out.writeInt(monster.getHunger());
		out.writeInt(monster.getSadness());
		out.writeInt(monster.getTiredness());
		out.writeInt(monster.getAgeRoundUnits());
		out.writeInt(monster.getName().length());
		out.write(monster.getName().getBytes());
	}

	public static Monster readMonster(DataInputStream in) throws IOException {
		int head = in.readInt();
		int torso = in.readInt();
		int legs = in.readInt();
		int skinColor = in.readInt();
		int hunger = in.readInt();
		int sadness = in.readInt();
		int tiredness = in.readInt();
		int ageRoundUnits = in.readInt();
		int name_length = in.readInt();
		byte[] name_bytes = new byte[name_length];
		in.read(name_bytes, 0, name_length);
		String name = new String(name_bytes);
		return new Monster(head, torso, legs, skinColor, hunger, sadness,
				tiredness, ageRoundUnits, name);
	}
}
