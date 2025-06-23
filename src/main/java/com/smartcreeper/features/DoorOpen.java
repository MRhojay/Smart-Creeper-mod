package com.smartcreeper.features;
‎
‎import com.smartcreeper.mixin.MobEntityAccessor;
‎import net.minecraft.block.Block;
‎import net.minecraft.block.Blocks;
‎import net.minecraft.block.DoorBlock;
‎import net.minecraft.entity.ai.goal.Goal;
‎import net.minecraft.entity.mob.CreeperEntity;
‎import net.minecraft.server.world.ServerWorld;
‎import net.minecraft.state.property.Properties;
‎import net.minecraft.util.math.BlockPos;
‎import net.minecraft.world.World;
‎import net.minecraft.world.event.GameEvent;
‎
‎import java.util.EnumSet;
‎
‎public class DoorOpen {
‎
‎    public static void register(CreeperEntity creeper) {
‎        try {
‎            ((MobEntityAccessor) (Object) creeper).getGoalSelector()
‎                    .add(3, new OpenDoorGoal(creeper));
‎            System.out.println("Goal added to Creeper");
‎        } catch (Exception e) {
‎            System.err.println("Failed to add goal to Creeper:");
‎            e.printStackTrace();
‎        }
‎    }
‎
‎    private static class OpenDoorGoal extends Goal {
‎        private final CreeperEntity creeper;
‎        private int ironDoorTries = 0;
‎
‎        public OpenDoorGoal(CreeperEntity creeper) {
‎            this.creeper = creeper;
‎            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
‎        }
‎
‎        @Override
‎        public boolean canStart() {
‎            return creeper.getTarget() != null;
‎        }
‎
‎        @Override
‎        public void tick() {
‎            World world = creeper.getWorld();
‎            BlockPos creeperPos = creeper.getBlockPos();
‎
‎            for (BlockPos pos : BlockPos.iterateOutwards(creeperPos, 2, 2, 2)) {
‎                var state = world.getBlockState(pos);
‎                Block block = state.getBlock();
‎
‎                if (block instanceof DoorBlock) {
‎                    if (block == Blocks.IRON_DOOR) {
‎                        if (!state.get(Properties.OPEN)) {
‎                            if (ironDoorTries < 3) {
‎                                ironDoorTries++;
‎                            } else {
‎                                for (BlockPos redstonePos : BlockPos.iterateOutwards(pos, 2, 2, 2)) {
‎                                    var redState = world.getBlockState(redstonePos);
‎                                    Block redBlock = redState.getBlock();
‎
‎                                    if (redBlock == Blocks.LEVER) {
‎                                        boolean powered = redState.get(Properties.POWERED);
‎                                        world.setBlockState(redstonePos, redState.with(Properties.POWERED, !powered));
‎                                        world.emitGameEvent(creeper, GameEvent.BLOCK_ACTIVATE, redstonePos);
‎                                        ironDoorTries = 0;
‎                                        return;
‎                                    }
‎
‎                                    if (redBlock == Blocks.STONE_BUTTON || redBlock == Blocks.OAK_BUTTON ||
‎                                        redBlock == Blocks.SPRUCE_BUTTON || redBlock == Blocks.BIRCH_BUTTON ||
‎                                        redBlock == Blocks.ACACIA_BUTTON || redBlock == Blocks.JUNGLE_BUTTON ||
‎                                        redBlock == Blocks.DARK_OAK_BUTTON || redBlock == Blocks.CRIMSON_BUTTON ||
‎                                        redBlock == Blocks.WARPED_BUTTON) {
‎
‎                                        if (!redState.get(Properties.POWERED)) {
‎                                            world.setBlockState(redstonePos, redState.with(Properties.POWERED, true));
‎                                            world.emitGameEvent(creeper, GameEvent.BLOCK_ACTIVATE, redstonePos);
‎
‎                                            if (world instanceof ServerWorld serverWorld) {
‎                                                serverWorld.scheduleBlockTick(redstonePos, redBlock, 20);
‎                                            }
‎
‎                                            ironDoorTries = 0;
‎                                            return;
‎                                        }
‎                                    }
‎                                }
‎                            }
‎                        }
‎                    } else {
‎                        if (!state.get(Properties.OPEN)) {
‎                            world.setBlockState(pos, state.with(Properties.OPEN, true));
‎                            world.emitGameEvent(creeper, GameEvent.BLOCK_ACTIVATE, pos);
‎                            return;
‎                        }
‎                    }
‎                }
‎            }
‎        }
‎    }
