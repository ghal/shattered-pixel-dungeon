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

package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class BeaconOfLight extends Artifact {

    public static final String AC_SHINE = "SHINE";

    {
        image = ItemSpriteSheet.ARTIFACT_BEACON;

        levelCap = 5;

        charge = 0;
        partialCharge = 0;
        chargeCap = 5; // uses a simple integer charge system

        defaultAction = AC_SHINE;

        unique = true;
        bones = false;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped(hero)
                && !cursed
                && hero.buff(MagicImmune.class) == null
                && (charge > 0)) {
            actions.add(AC_SHINE);
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (hero.buff(MagicImmune.class) != null) return;

        if (AC_SHINE.equals(action)){
            if (!isEquipped(hero))        GLog.i(Messages.get(Artifact.class, "need_to_equip"));
            else if (cursed)              GLog.i(Messages.get(this, "cursed"));
            else if (charge <= 0)         GLog.i(Messages.get(this, "no_charge"));
            else {
                // grant light for a short duration and consume 1 charge
                Buff.prolong(hero, Light.class, 50f + 10f*level());
                artifactProc(hero, visiblyUpgraded(), 1);
                charge--;
                Catalog.countUse(BeaconOfLight.class);
                updateQuickslot();
                Talent.onArtifactUsed(Dungeon.hero);
            }
        }
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new ShineRecharge();
    }

    @Override
    public void charge(Hero target, float amount) {
        if (cursed || target.buff(MagicImmune.class) != null) return;
        if (charge < chargeCap) {
            // slow steady recharge; full in ~200 turns baseline
            partialCharge += 0.05f * amount;
            while (partialCharge >= 1f) {
                charge++;
                partialCharge--;
            }
            if (charge >= chargeCap) {
                charge = chargeCap;
                partialCharge = 0;
            }
            updateQuickslot();
        }
    }

    @Override
    public Item upgrade() {
        if (level() < levelCap) {
            // every level increases max charges by 1 up to cap
            chargeCap = Math.min(chargeCap + 1, 10);
        }
        return super.upgrade();
    }

    @Override
    public String desc() {
        String desc = super.desc();
        if (isEquipped(Dungeon.hero)) {
            if (cursed) desc += "\n\n" + Messages.get(this, "desc_cursed");
            else        desc += "\n\n" + Messages.get(this, "desc_worn");
        }
        return desc;
    }

    public class ShineRecharge extends ArtifactBuff {
        @Override
        public boolean act() {
            // very gentle natural recharge over time
            if (charge < chargeCap && !cursed && target.buff(MagicImmune.class) == null) {
                partialCharge += 0.02f; // independent of regen; very slow trickle
                while (partialCharge >= 1f) {
                    charge++;
                    partialCharge--;
                    if (charge >= chargeCap) {
                        charge = chargeCap;
                        partialCharge = 0;
                    }
                }
            } else if (charge >= chargeCap) {
                partialCharge = 0;
            }

            updateQuickslot();
            spend(TICK);
            return true;
        }

        @Override
        public int icon() {
            return BuffIndicator.LIGHT;
        }
    }
}


