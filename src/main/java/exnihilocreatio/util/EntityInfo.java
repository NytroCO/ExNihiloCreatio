package exnihilocreatio.util;

import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityInfo {
    public static EntityInfo EMPTY = new EntityInfo(null);
    @Getter
    private Class<? extends Entity> entityClass;
    private String name;

    public EntityInfo(String entityName) {
        this.name = entityName;
        this.entityClass = entityName == null ? null : EntityList.getClass(new ResourceLocation(entityName));
    }

    /**
     * Attempts to spawn entity located  within `range` of `pos`
     *
     * @param pos
     * @param range
     * @param worldIn
     * @return whether it did spawn the mob
     */
    public boolean spawnEntityNear(BlockPos pos, int range, World worldIn) {
        if (entityClass == null || name == null)
            return false;

        if (!worldIn.isRemote && worldIn.getDifficulty() != EnumDifficulty.PEACEFUL) {
            Entity entity = EntityList.newEntity(entityClass, worldIn);
            if (entity instanceof EntityLiving) {
                EntityLiving entityLiving = (EntityLiving) entity;

                double dx = (worldIn.rand.nextDouble() - worldIn.rand.nextDouble()) * range + 0.5;
                double dy = (worldIn.rand.nextDouble() - worldIn.rand.nextDouble()) * range;
                double dz = (worldIn.rand.nextDouble() - worldIn.rand.nextDouble()) * range + 0.5;
                BlockPos spawnPos = new BlockPos(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz);

                entityLiving.setPosition(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());

                boolean canSpawn = worldIn.getCollisionBoxes(entityLiving, entityLiving.getEntityBoundingBox()).isEmpty();
                if (canSpawn) {
                    worldIn.spawnEntity(entityLiving);
                    worldIn.playEvent(2004, pos, 0);
                    entityLiving.spawnExplosionParticle();
                    return true;
                } else {
                    return false;
                }
            }
            return false; // Not a Living Entity, not currently handled.
        }

        return false;
    }

    public String getName() {
        return name;
    }
}
