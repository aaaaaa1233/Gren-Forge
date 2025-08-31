package committee.nova.mods.bren.utils;



import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.entity.player.Player;

import java.util.function.IntFunction;

public class GunHelper {
    private static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);

    public static void dataTracker(SynchedEntityData dataTracker) {
        dataTracker.define(STATE, 0);
    }

    public static GunHelper.GunStates getGunState(SynchedEntityData dataTracker) {
        return GunHelper.GunStates.byIndex(dataTracker.get(STATE));
    }

    public static void setGunState(GunHelper.GunStates state, SynchedEntityData dataTracker) {
        dataTracker.set(STATE, state.getId());
    }

    public static void readCustomDataFromNbt(CompoundTag nbt, SynchedEntityData dataTracker) {
        setGunState(GunHelper.GunStates.byIndex(nbt.getInt("MachineGunState")), dataTracker);
    }

    public static void writeCustomDataToNbt(CompoundTag nbt, SynchedEntityData dataTracker) {
        nbt.putInt("MachineGunState", getGunState(dataTracker).getId());
    }

    public enum GunStates {
        NORMAL(0),
        RELOADING(1);

        final int id;

        private static final IntFunction<GunStates> BY_ID = ByIdMap.continuous(GunStates::getId, values(), ByIdMap.OutOfBoundsStrategy.CLAMP);

        GunStates(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public static GunStates byIndex(int index) {
            return BY_ID.apply(index);
        }
    }
}
