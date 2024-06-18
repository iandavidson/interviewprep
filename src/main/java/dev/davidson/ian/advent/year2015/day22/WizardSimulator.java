package dev.davidson.ian.advent.year2015.day22;

import dev.davidson.ian.advent.year2015.day22.spell.Drain;
import dev.davidson.ian.advent.year2015.day22.spell.MagicMissile;
import dev.davidson.ian.advent.year2015.day22.spell.Poison;
import dev.davidson.ian.advent.year2015.day22.spell.Recharge;
import dev.davidson.ian.advent.year2015.day22.spell.Shield;
import dev.davidson.ian.advent.year2015.day22.spell.Spell;
import dev.davidson.ian.advent.year2015.day22.state.GameState;
import dev.davidson.ian.advent.year2015.day22.state.SpellEffect;
import dev.davidson.ian.advent.year2015.day22.state.StatEffects;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class WizardSimulator {

    private static final int ENEMY_HEALTH = 58;
    private static final int ENEMY_DAMAGE = 9;
    private static final int PLAYER_HIT_POINTS = 50;
    private static final int PLAYER_MANA_POINTS = 500;

    private static final MagicMissile MAGIC_MISSILE = new MagicMissile();
    private static final Drain DRAIN = new Drain();
    private static final Poison POISON = new Poison();
    private static final Shield SHIELD = new Shield();
    private static final Recharge RECHARGE = new Recharge();
    private final List<Spell> SPELLS = new ArrayList<>();

    public static void main(String[] args) {
        WizardSimulator wizardSimulator = new WizardSimulator();
        log.info("Part1: {}", wizardSimulator.part1());
    }

    public int part1() {
        Player player = new Player(PLAYER_HIT_POINTS, PLAYER_MANA_POINTS);
        GameState gameState = new GameState();

        //todo: hack
        SPELLS.add(MAGIC_MISSILE);
        SPELLS.add(DRAIN);
        SPELLS.add(POISON);
        SPELLS.add(SHIELD);
        SPELLS.add(RECHARGE);

        int min = Integer.MAX_VALUE;
        for (Spell spell : SPELLS) {
            min = Math.min(min, part1Helper(gameState, ENEMY_HEALTH, player, spell, 0));
        }

        //1119 too low
        return min;
    }

    private int part1Helper(final GameState gameState, int enemyHealth, final Player player, final Spell currentSpell, final int turn) {
        StatEffects statEffects = StatEffects.newStatEffects(gameState);

        //apply current effects on player and enemy
        player.addMana(statEffects.getManaRegen());
        player.regenHitPoints(statEffects.getHealthRegen());
        enemyHealth -= statEffects.getDamageLinger();

        //this will decrement the duration on spells by 1; if they now have 0 duration we will evict
        gameState.endTurn();


        if (enemyHealth <= 0) {
            return 0;
        }

        if (turn % 2 == 0) {
            int manaConsumedNow;
            //players turn
            switch (currentSpell) {
                case Drain drain -> {
                    manaConsumedNow = drain.getManaDrain();
                    player.useMana(drain.getManaDrain());
                    enemyHealth -= drain.getDamage();
                    player.regenHitPoints(drain.getHealthRegen());

                }
                case MagicMissile magicMissile -> {
                    manaConsumedNow = magicMissile.getManaDrain();
                    player.useMana(magicMissile.getManaDrain());
                    enemyHealth -= magicMissile.getDamage();

                }
                case Poison poison -> {
                    manaConsumedNow = poison.getManaDrain();
                    player.useMana(poison.getManaDrain());
                    gameState.getSpellEffects().add(SpellEffect.toSpellEffect(poison));

                }
                case Shield shield -> {
                    manaConsumedNow = shield.getManaDrain();
                    player.useMana(shield.getManaDrain());
                    gameState.getSpellEffects().add(SpellEffect.toSpellEffect(shield));

                }
                case Recharge recharge -> {
                    manaConsumedNow = recharge.getManaDrain();
                    player.useMana(recharge.getManaDrain());
                    gameState.getSpellEffects().add(SpellEffect.toSpellEffect(recharge));

                }
                default -> throw new IllegalStateException("Unexpected value: " + currentSpell);
            }

//            log.info("turn: {}, enemyHealth: {}; {}; {};", turn, enemyHealth, player, currentSpell);

            if (enemyHealth <= 0) {
                return manaConsumedNow;
            }

            //todo: hack
            Collections.shuffle(SPELLS);

            int min = Integer.MAX_VALUE;
            for (Spell spell : SPELLS) {

                if (!gameState.isMember(spell) && spell.canCast(player.getMana())) {
//                    if (!(spell instanceof Recharge)) {
////                        if(spell.getManaDrain())
//                        int i = 0;
//                    }

                    int consumedInFuture = part1Helper(gameState.copy(), enemyHealth,
                            new Player(player.getHitPoints(), player.getMana()), spell, turn + 1);

                    if (consumedInFuture != Integer.MAX_VALUE) {
                        min = Math.min(min, manaConsumedNow + consumedInFuture);
                    }
                }
            }

            return min;
        } else {
            //bosses turn
            player.attacked(ENEMY_DAMAGE, statEffects.getArmor());

            if (player.getHitPoints() <= 0) {
                return Integer.MAX_VALUE;
            }

            return part1Helper(gameState, enemyHealth, player, currentSpell, turn + 1);
        }
    }

//    private void logState(int enemyHealth, Player player, Spell currentSpell, int turn) {
//        log.info("turn: {}, enemyHealth: {}; {}; {};", turn, enemyHealth, player, currentSpell);
//    }



    /*
    boss:
    Hit Points: 58
    Damage: 9

    Magic Missile costs 53 mana. It instantly does 4 damage.
    Drain costs 73 mana. It instantly does 2 damage and heals you for 2 hit points.
    Shield costs 113 mana. It starts an effect that lasts for 6 turns. While it is active, your armor is increased by 7.
    Poison costs 173 mana. It starts an effect that lasts for 6 turns. At the start of each turn while it is active, it deals the boss 3 damage.
    Recharge costs 229 mana. It starts an effect that lasts for 5 turns. At the start of each turn while it is active, it gives you 101 new mana.
     */
}
