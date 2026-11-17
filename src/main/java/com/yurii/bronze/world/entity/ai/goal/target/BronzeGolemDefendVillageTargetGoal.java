package com.yurii.bronze.world.entity.ai.goal.target;

import java.util.EnumSet;
import java.util.List;

import com.yurii.bronze.world.entity.animal.golem.BronzeGolem;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public final class BronzeGolemDefendVillageTargetGoal extends TargetGoal {

    private final BronzeGolem bronzeGolem;
    private LivingEntity potentialTarget;
    private final TargetingConditions attackTargeting = TargetingConditions.forCombat().range(64.0);

    public BronzeGolemDefendVillageTargetGoal(BronzeGolem bronzeGolem) {
        super(bronzeGolem, false, true);
        this.bronzeGolem = bronzeGolem;
        setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        AABB grow = bronzeGolem.getBoundingBox().inflate(32.0, 20.0, 32.0);
        ServerLevel level = getServerLevel(bronzeGolem);
        potentialTarget = null;
        List<Player> players = level.getNearbyPlayers(attackTargeting, bronzeGolem, grow);
        for (Villager villager : level.getNearbyEntities(Villager.class, attackTargeting, bronzeGolem, grow)) {
            for (Player player : players) {
                if (villager.getPlayerReputation(player) <= -100) {
                    potentialTarget = player;
                    return !player.isCreative() && !player.isSpectator();
                }
            }
        }
        return false;
    }

    @Override
    public void start() {
        bronzeGolem.setTarget(potentialTarget);
        super.start();
    }

}
