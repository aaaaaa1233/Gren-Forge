package committee.nova.mods.bren.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.Tags;
import committee.nova.mods.bren.init.config.MConfig;
import committee.nova.mods.bren.init.registry.DamageTypeReg;
import committee.nova.mods.bren.init.registry.EntityReg;
import committee.nova.mods.bren.init.registry.ParticleReg;
import org.jetbrains.annotations.NotNull;

public class BulletEntity extends Projectile {
    private static final EntityDataAccessor<Integer> LIFESPAN = SynchedEntityData.defineId(BulletEntity.class, EntityDataSerializers.INT);
    private float damage;

    public BulletEntity(EntityType<? extends Projectile> entityType, Level world) {
        super(entityType, world);
    }

    public static BulletEntity create(Level level, float damage, int lifespan, LivingEntity owner) {
        BulletEntity entity = EntityReg.BULLET.get().create(level);
        if (entity != null) {
            entity.damage = damage;
            entity.setLifespan(lifespan);
            entity.setNoGravity(true);
            entity.setOwner(owner);
        }
        return entity;

    }


    protected void setLifespan(int lifespan) {
        this.entityData.set(LIFESPAN, lifespan);
    }

    public int getLifespan() {
        return this.entityData.get(LIFESPAN);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(LIFESPAN, 0);
    }


    public void tick() {
        float h;
        super.tick();
        var hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        boolean bl = false;
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = ((BlockHitResult)hitResult).getBlockPos();
            BlockState blockState = this.level().getBlockState(blockPos);
            if (blockState.is(Blocks.NETHER_PORTAL)) {
                this.handleInsidePortal(blockPos);
                bl = true;
            } else if (blockState.is(Blocks.END_GATEWAY)) {
                BlockEntity blockEntity = this.level().getBlockEntity(blockPos);
                if (blockEntity instanceof TheEndGatewayBlockEntity && TheEndGatewayBlockEntity.canEntityTeleport(this)) {
                    TheEndGatewayBlockEntity.teleportEntity(this.level(), blockPos, blockState, this, (TheEndGatewayBlockEntity)blockEntity);
                }
                bl = true;
            }
        }
        if (hitResult.getType() != HitResult.Type.MISS && !bl) {
            this.onHit(hitResult);
        }
        this.checkInsideBlocks();
        var vec3d = this.getDeltaMovement();
        double d = this.getX() + vec3d.x;
        double e = this.getY() + vec3d.y;
        double f = this.getZ() + vec3d.z;
        this.updateRotation();
        if (this.isInWater()) {
            for (int i = 0; i < 4; ++i) {
                float g = 0.25f;
                this.level().addParticle(ParticleTypes.BUBBLE, d - vec3d.x * g, e - vec3d.y * g, f - vec3d.z * g, vec3d.x, vec3d.y, vec3d.z);
            }
            h = 0.8f;
        } else {
            h = 0.99f;
        }
        this.setDeltaMovement(vec3d.scale(h));
        if (!this.isNoGravity()) {
            var vec3d2 = this.getDeltaMovement();
            this.setDeltaMovement(vec3d2.x, vec3d2.y - (double)this.getGravity(), vec3d2.z);
        }
        this.setPos(d, e, f);

        double l = this.getDeltaMovement().length();

        if (Math.ceil(l) == 0) {
            this.discard();
        }

        if (this.tickCount >= this.getLifespan()) {
            this.discard();
        }

        if (this.level().isClientSide() && this.tickCount >= 2 && this.tickCount % 3 == 0) {
            this.level().addParticle(ParticleReg.AIR_RING_PARTICLE.get(), this.getX(), this.getY() + this.getBbHeight()/2, this.getZ(), 0, 0, 0);
        }
    }

    protected float getGravity() {
        return 0.03f;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity entity = entityHitResult.getEntity();

        if (entity.equals(this.getOwner())) {
            return;
        }

        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.invulnerableTime = 0;
            DamageSource damageSource = DamageTypeReg.shot(this.level(), this, this.getOwner());
            livingEntity.hurt(damageSource, this.damage);

            if (this.isOnFire()) {
                entity.setSecondsOnFire(4);
            }
        }
        this.discard();
    }


    @Override
    protected void onHitBlock(@NotNull BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        BlockPos pos = blockHitResult.getBlockPos();
        BlockState state = this.level().getBlockState(pos);

        var vec3d = blockHitResult.getBlockPos();

        if (!state.isAir() && state.isSolid() && this.tickCount > 1) {

            if ((state.is(Tags.Blocks.GLASS) || state.is(Tags.Blocks.GLASS_PANES)) && MConfig.bulletsBreakGlass.get()) {
                if (this.level().isClientSide()) { return;}
                this.level().destroyBlock(pos, false, this.getOwner());
            } else {
                this.level().playSound(null,vec3d.getX(),vec3d.getY(),vec3d.getZ(),state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 1.0F, 3.0F);

                boolean isAirAbove = this.level().getBlockState(pos.above()).isAir();
                boolean hitGround = !this.level().getBlockState(pos).isAir();

                if (this.level().isClientSide()) {
                    for (int i = 0; i < 4; ++i) {

                        float x = this.random.nextFloat() - .5f;
                        float y = this.random.nextFloat() - .5f;
                        float z = this.random.nextFloat() - .5f;

                        this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), vec3d.getX(),vec3d.getY(),vec3d.getZ(), x, y, z);
                    }
                } else if (hitGround && isAirAbove && this.isOnFire()){
                    this.level().setBlockAndUpdate(pos.above(), Blocks.FIRE.defaultBlockState());
                }

                this.discard();
            }
        }
    }
}