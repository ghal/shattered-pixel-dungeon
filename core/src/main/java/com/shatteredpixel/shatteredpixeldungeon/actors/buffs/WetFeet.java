/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

/**
 * Minor movement slowdown that lingers briefly after stepping into water.
 * Purely mechanical and self-contained to avoid unintended side effects.
 */
public class WetFeet extends FlavourBuff {

	public static final float DURATION = 3f;

	{
		//Neutral visual, small negative impact
		type = buffType.NEUTRAL;
	}

	@Override
	public int icon() {
		//Reuse frost icon, tinted to a lighter blue
		return BuffIndicator.FROST;
	}

	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(0.4f, 0.8f, 1f);
	}

	@Override
	public String name() {
		return "Damp";
	}

	@Override
	public String desc() {
		return "You are damp from water and move a bit more slowly. Fades in " + dispTurns() + " turns.";
	}
}


