package com.yurii.bronze.world.entity.animal.golem;

import com.yurii.bronze.sounds.BronzeModSoundEvents;
import com.yurii.bronze.world.entity.BronzeModEntityType;
import com.yurii.bronze.world.entity.ai.goal.BronzeGolemRangedCrossbowAttackGoal;
import com.yurii.bronze.world.entity.ai.goal.target.BronzeGolemDefendVillageTargetGoal;
import com.yurii.bronze.world.item.BronzeModItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Crackiness;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.GolemRandomStrollInVillageGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveBackToVillageGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.golem.AbstractGolem;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments.Mutable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class BronzeGolem extends AbstractGolem implements NeutralMob, CrossbowAttackMob {

    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(BronzeGolem.class, EntityDataSerializers.BYTE);
    private static final  EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(BronzeGolem.class, EntityDataSerializers.BOOLEAN);
    private EntityReference<LivingEntity> persistentAngerTarget;
    private long persistentAngerEndTime;

    public BronzeGolem(EntityType<? extends BronzeGolem> type, Level level) {
        super(type, level);
    }

    public static Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 50.0).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.KNOCKBACK_RESISTANCE, 0.5).add(Attributes.STEP_HEIGHT, 1.0).add(Attributes.FOLLOW_RANGE, 64.0);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new BronzeGolemRangedCrossbowAttackGoal<>(this));
        goalSelector.addGoal(2, new MoveBackToVillageGoal(this, 1.0, false));
        goalSelector.addGoal(3, new GolemRandomStrollInVillageGoal(this, 0.75));
        goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
        goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new BronzeGolemDefendVillageTargetGoal(this));
        targetSelector.addGoal(2, new HurtByTargetGoal(this));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Mob.class, 10, false, false, (target, level) -> target instanceof Enemy));
        targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader level) {
        BlockPos pos = blockPosition();
        BlockPos belowPos = pos.below();
        if (!level.getBlockState(belowPos).entityCanStandOn(level, belowPos, this)) return false;
        for (int i = 1; i < 2; i++) {
            BlockPos abovePos = pos.above(i);
            BlockState above = level.getBlockState(abovePos);
            if (!NaturalSpawner.isValidEmptySpawnBlock(level, abovePos, above, above.getFluidState(), BronzeModEntityType.BRONZE_GOLEM)) return false;
        }
        return NaturalSpawner.isValidEmptySpawnBlock(level, pos, level.getBlockState(pos), Fluids.EMPTY.defaultFluidState(), BronzeModEntityType.BRONZE_GOLEM) && level.isUnobstructed(this);
    }

    public void setPlayerCreated(boolean value) {
        byte current = entityData.get(DATA_FLAGS_ID);
        entityData.set(DATA_FLAGS_ID, (byte) (value ? current | 1 : current & -2));
    }

    public boolean isPlayerCreated() {
        return (entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_FLAGS_ID, (byte) 0);
        entityData.define(IS_CHARGING_CROSSBOW, false);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
        RandomSource random = level.getRandom();
        setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
        setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        populateDefaultEquipmentEnchantments(level, random, difficulty);
        groupData = super.finalizeSpawn(level, difficulty, spawnReason, groupData);
        if (spawnReason == EntitySpawnReason.TRIGGERED) setLeftHanded(false);
        return groupData;
    }

    @Override
    protected void enchantSpawnedWeapon(ServerLevelAccessor level, RandomSource random, DifficultyInstance difficulty) {
        super.enchantSpawnedWeapon(level, random, difficulty);
        ItemStack crossbow = getItemBySlot(EquipmentSlot.MAINHAND);
        if (!crossbow.is(Items.CROSSBOW)) return;
        float roll = random.nextFloat();
        int quickChargeLevel = roll <= 0.05F ? 0 : roll <= 0.15F ? 1 : roll <= 0.30F ? 2 : 3;
        int powerLevel = random.nextInt(6);
        if (quickChargeLevel > 0 || powerLevel > 0) {
            ItemEnchantments currentEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(crossbow);
            Mutable finalEnchantments = new Mutable(currentEnchantments);
            if (quickChargeLevel > 0) level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(Enchantments.QUICK_CHARGE).ifPresent(holder -> finalEnchantments.set(holder, quickChargeLevel));
            if (powerLevel > 0) level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(Enchantments.POWER).ifPresent(holder -> finalEnchantments.set(holder, powerLevel));
            EnchantmentHelper.setEnchantments(crossbow, finalEnchantments.toImmutable());
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level().isClientSide()) updatePersistentAnger((ServerLevel) level(), true);
    }

    @Override
    protected int decreaseAirSupply(int currentSupply) {
        return currentSupply;
    }

    @Override
    public boolean canSpawnSprintParticle() {
        return getDeltaMovement().horizontalDistanceSqr() > 2.5000003E-7 && random.nextInt(5) == 0;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!itemStack.is(BronzeModItems.BRONZE_INGOT)) return InteractionResult.PASS;
        float healthBefore = getHealth();
        heal(25.0F);
        if (getHealth() == healthBefore) return InteractionResult.PASS;
        playSound(BronzeModSoundEvents.BRONZE_GOLEM_REPAIR, 1.0F, 1.0F + (random.nextFloat() - random.nextFloat()) * 0.02F);
        itemStack.consume(1, player);
        return InteractionResult.SUCCESS;
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, (0.875 * getEyeHeight()), (getBbWidth() * 0.25F));
    }

    @Override
    protected void doPush(Entity entity) {
        if (entity instanceof Enemy && getRandom().nextInt(20) == 0) setTarget((LivingEntity) entity);
        super.doPush(entity);
    }

    @Override
    public void setPersistentAngerTarget(EntityReference<LivingEntity> persistentAngerTarget) {
        this.persistentAngerTarget = persistentAngerTarget;
    }

    @Override
    public EntityReference<LivingEntity> getPersistentAngerTarget() {
        return persistentAngerTarget;
    }

    @Override
    public void startPersistentAngerTimer() {
        setTimeToRemainAngry(TimeUtil.rangeOfSeconds(15, 29).sample(random));
    }

    @Override
    public void setPersistentAngerEndTime(long endTime) {
        persistentAngerEndTime = endTime;
    }

    @Override
    public long getPersistentAngerEndTime() {
        return persistentAngerEndTime;
    }

    @Override
    public ItemStack getProjectile(ItemStack heldWeapon) {
        return heldWeapon.getItem() instanceof ProjectileWeaponItem ? new ItemStack(Items.ARROW) : super.getProjectile(heldWeapon);
    }

    @Override
    public void setChargingCrossbow(boolean isCharging) {
        entityData.set(IS_CHARGING_CROSSBOW, isCharging);
    }

    public boolean isChargingCrossbow() {
        return entityData.get(IS_CHARGING_CROSSBOW);
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return !((isPlayerCreated() && target.is(EntityType.PLAYER)) || target instanceof AbstractGolem) && super.canAttack(target);
    }

    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        InteractionHand hand = ProjectileUtil.getWeaponHoldingHand(this, Items.CROSSBOW);
        ItemStack usedItem = getItemInHand(hand);
        if (usedItem.getItem() instanceof CrossbowItem crossbow) crossbow.performShooting(level(), this, hand, usedItem, 5.0F, 2.0F - level().getDifficulty().getId() * 0.5F, getTarget());
        onCrossbowAttackPerformed();
    }

    @Override
    public void onCrossbowAttackPerformed() {
        noActionTime = 0;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockState) {
        playSound(BronzeModSoundEvents.BRONZE_GOLEM_STEP, 1.0F, 1.0F);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return BronzeModSoundEvents.BRONZE_GOLEM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BronzeModSoundEvents.BRONZE_GOLEM_DEATH;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        Crackiness.Level previousCrackiness = getCrackiness();
        boolean wasHurt = super.hurtServer(level, source, damage);
        if (wasHurt && getCrackiness() != previousCrackiness && getCrackiness() != Crackiness.Level.MEDIUM) playSound(BronzeModSoundEvents.BRONZE_GOLEM_DAMAGE, 1.0F, 1.0F);
        return wasHurt;
    }

    public Crackiness.Level getCrackiness() {
        return Crackiness.GOLEM.byFraction(getHealth() / getMaxHealth());
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("PlayerCreated", isPlayerCreated());
        addPersistentAngerSaveData(output);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setPlayerCreated(input.getBooleanOr("PlayerCreated", false));
        readPersistentAngerSaveData(level(), input);
        setDropChance(EquipmentSlot.MAINHAND, 0.0F);
    }

}
