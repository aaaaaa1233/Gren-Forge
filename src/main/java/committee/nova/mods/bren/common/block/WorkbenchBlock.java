package committee.nova.mods.bren.common.block;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WorkbenchBlock extends Block {

    public VoxelShape shape(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.or(shape, Shapes.create(0, 0, 0, 1, 0.875, 1));

        return shape;
    }

    public WorkbenchBlock(Properties settings) {
        super(settings.noOcclusion());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        var vec3d = state.getOffset(world, pos);
        return this.shape().move(vec3d.x, vec3d.y, vec3d.z);
    }
}
