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

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

// Triggers on low health to provide brief sustain and mitigation.
public class SecondWind extends FlavourBuff {

	public static final float DURATION = 10f;

	{
		type = buffType.POSITIVE;
		announced = true;
	}

	@Override
	public boolean attachTo(Char target) {
		boolean ok = super.attachTo(target);
		if (ok) {
			// Apply a small burst of regeneration and temporary barkskin.
			// Healing: modest flat-over-time heal that is safe and reuses existing logic.
			Healing healing = Buff.affect(target, Healing.class);
			// Heal roughly 20% max HP over the duration, biased to be safe for early game too.
			int totalHeal = Math.max(6, Math.round(target.HT * 0.2f));
			healing.setHeal(totalHeal, 0f, Math.max(1, Math.round(totalHeal / DURATION)));
			healing.applyVialEffect();

			// Barkskin: temporary DR that decays naturally via the Barkskin buff.
			// Scale gently with hero level when applicable; safe default otherwise.
			int barkLevel = 1;
			if (target instanceof com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero) {
				barkLevel = Math.max(1, ((com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero) target).lvl);
			}
			Barkskin.conditionallyAppend(target, barkLevel, 1);
		}
		return ok;
	}

	@Override
	public int icon() {
		return BuffIndicator.HEART;
	}

	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(1f, 0.5f, 0.5f);
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (DURATION - visualcooldown()) / DURATION);
	}

	@Override
	public String name() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns());
	}
}


