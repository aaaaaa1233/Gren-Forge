package committee.nova.mods.bren.common.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import committee.nova.mods.bren.Bren;
import committee.nova.mods.bren.common.entity.IGunUser;

public class LongShootingCriterion extends SimpleCriterionTrigger<LongShootingCriterion.Conditions> {
    static final ResourceLocation ID = new ResourceLocation(Bren.MODID, "shooting");


    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    protected Conditions createInstance(JsonObject jsonObject, ContextAwarePredicate lootContextPredicate, DeserializationContext advancementEntityPredicateDeserializer) {
        ItemPredicate itemPredicate = ItemPredicate.fromJson(jsonObject.get("firearm"));
        return new Conditions(lootContextPredicate, itemPredicate);
    }

    public static class Conditions extends AbstractCriterionTriggerInstance {
        private final ItemPredicate firearm;

        public Conditions(ContextAwarePredicate entity, ItemPredicate itemPredicate) {
            super(ID, entity);
            this.firearm = itemPredicate;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext predicateSerializer) {
            JsonObject jsonObject = super.serializeToJson(predicateSerializer);
            jsonObject.add("firearm", this.firearm.serializeToJson());
            return jsonObject;
        }


        public boolean test(Player player, ItemStack stack) {
            return ((IGunUser)player).shootingDuration() > 80 && player.getUseItem() == stack && this.firearm.matches(stack);
        }
    }

    public void trigger(ServerPlayer player, ItemStack stack) {
        trigger(player, conditions -> conditions.test(player, stack));
    }
}
