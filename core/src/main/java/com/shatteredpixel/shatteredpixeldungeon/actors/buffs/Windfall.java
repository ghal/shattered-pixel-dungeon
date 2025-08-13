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
 * Brief burst of speed after picking up gold. Self-contained and safe.
 */
public class Windfall extends FlavourBuff {

    public static final float DURATION = 4f;

    {
        type = buffType.POSITIVE;
        announced = false;
    }

    @Override
    public int icon() {
        // Reuse haste icon, tint to gold
        return BuffIndicator.HASTE;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(1f, 0.9f, 0.3f);
    }

    @Override
    public String name() {
        return "Windfall";
    }

    @Override
    public String desc() {
        return "Finding gold puts a spring in your step, slightly hastening your actions for a short time. Fades in " + dispTurns() + " turns.";
    }
}


