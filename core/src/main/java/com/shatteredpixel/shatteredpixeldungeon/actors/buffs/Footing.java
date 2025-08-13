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
 * Brief evasion boost from steady footing when standing on natural ground.
 * Implemented as a simple flavour buff for safety and easy persistence.
 */
public class Footing extends FlavourBuff {

	public static final float DURATION = 3f;

	{
		type = buffType.POSITIVE;
	}

	@Override
	public int icon() {
		// Reuse barkskin icon with a lighter green tint
		return BuffIndicator.BARKSKIN;
	}

	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(0.65f, 1.0f, 0.55f);
	}

	@Override
	public String name() {
		return "Sure Footing";
	}

	@Override
	public String desc() {
		return "Natural ground improves your footing, slightly increasing evasion for a short time. Fades in "
				+ dispTurns() + " turns.";
	}
}


