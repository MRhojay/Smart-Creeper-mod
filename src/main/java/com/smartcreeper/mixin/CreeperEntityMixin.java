package com.smartcreeper.mixin;
‎
‎import com.smartcreeper.features.DoorOpen;
‎import net.minecraft.entity.mob.CreeperEntity;
‎import org.spongepowered.asm.mixin.Mixin;
‎import org.spongepowered.asm.mixin.injection.At;
‎import org.spongepowered.asm.mixin.injection.Inject;
‎import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
‎
‎@Mixin(CreeperEntity.class)
‎public abstract class CreeperEntityMixin {
‎
‎    @Inject(method = "initGoals", at = @At("TAIL"))
‎    private void injectSmartDoorGoal(CallbackInfo ci) {
‎        System.out.println("Injecting DoorOpen goal into Creeper...");
‎        CreeperEntity creeper = (CreeperEntity) (Object) this;
‎        DoorOpen.register(creeper);
‎    }
‎}
