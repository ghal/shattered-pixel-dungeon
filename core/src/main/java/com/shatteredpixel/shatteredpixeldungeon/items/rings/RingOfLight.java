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

package com.shatteredpixel.shatteredpixeldungeon.items.rings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RingOfLight extends Ring {

	{
		// Reuse an existing ring icon to avoid asset changes
		icon = ItemSpriteSheet.Icons.RING_ELEMENTS;
		buffClass = Vision.class;
	}

	@Override
	protected RingBuff buff() {
		return new Vision();
	}

	public String statsInfo() {
		if (isIdentified()){
			String info = Messages.get(this, "stats", soloBuffedBonus());
			if (isEquipped(Dungeon.hero) && soloBuffedBonus() != combinedBuffedBonus(Dungeon.hero)){
				info += "\n\n" + Messages.get(this, "combined_stats", combinedBuffedBonus(Dungeon.hero));
			}
			return info;
		} else {
			return Messages.get(this, "typical_stats", 1);
		}
	}

	public String upgradeStat1(int level){
		if (cursed && cursedKnown) level = Math.min(-1, level-3);
		return Integer.toString(level+1);
	}

	public class Vision extends RingBuff {

		private int lastApplied = -1;

		@Override
		public boolean attachTo(Char target) {
			boolean ok = super.attachTo(target);
			if (ok) {
				applyViewDistance();
			}
			return ok;
		}

		@Override
		public boolean act() {
			// Keep effect consistent across dynamic changes (e.g. new floor)
			applyViewDistance();
			return super.act();
		}

		@Override
		public void detach() {
			// Restore to baseline or light buff if present
			if (target != null) {
				int base = Dungeon.level != null ? Dungeon.level.viewDistance : target.viewDistance;
				if (target.buff(Light.class) != null) {
					base = Math.max(base, Light.DISTANCE);
				}
				target.viewDistance = base;
				Dungeon.observe();
			}
			super.detach();
		}

		private void applyViewDistance(){
			if (target == null) return;
			int base = Dungeon.level != null ? Dungeon.level.viewDistance : target.viewDistance;
			if (target.buff(Light.class) != null) {
				base = Math.max(base, Light.DISTANCE);
			}
			int desired = Math.max(base, base + buffedLvl());
			if (desired != lastApplied && desired > target.viewDistance){
				target.viewDistance = desired;
				lastApplied = desired;
				Dungeon.observe();
			}
		}
	}
}


