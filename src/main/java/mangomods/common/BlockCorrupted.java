package mangomods.common;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class BlockCorrupted extends TBlock {

    private static final int MAX_SPREAD_DISTANCE = 3;
    private static final int PROBE_RADIUS = 128;
    private static final int PROBE_COUNT = 128;
    private static final double CDU_THRESHOLD = -1;

    private static final ArrayList<Vec3i> spreadLocations = new ArrayList<>();

    static {
        // Initialize spreadLocations with blocks that are a Manhattan distance <= MAX_SPREAD_DISTANCE from center
        for(int k = -MAX_SPREAD_DISTANCE; k <= MAX_SPREAD_DISTANCE; k++) {
            final int kBound = MAX_SPREAD_DISTANCE - Math.abs(k);
            for(int j = -kBound; j <= kBound; j++) {
                final int jkBound = MAX_SPREAD_DISTANCE - Math.abs(j) - Math.abs(k);
                for(int i = -jkBound; i <= jkBound; i++) {
                    if(i != 0 || j != 0 || k != 0) spreadLocations.add(new Vec3i(i, j, k));
                }
            }
        }
    }

    public BlockCorrupted() {
        super("corrupted", Material.ROCK);
        setTickRandomly(true);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);

        Collections.shuffle(spreadLocations);

        // Find valid location to spread to
        boolean spreadLocationFound = false;
        Vec3i spreadLocation = null;
        for(Vec3i possibleSpreadLocation : spreadLocations) {
            BlockPos possibleSpreadLocationPos = pos.add(possibleSpreadLocation);
            if(worldIn.isBlockLoaded(possibleSpreadLocationPos) && worldIn.getBlockState(possibleSpreadLocationPos).getBlock() != BlockInit.CORRUPTED && worldIn.getBlockState(possibleSpreadLocationPos).getMaterial().isSolid()) {
                spreadLocation = possibleSpreadLocation;
                spreadLocationFound = true;
                break;
            }
        }

        if(!spreadLocationFound) return;

        // Probe the surrounding area to get a feel for its corruption status
        ArrayList<BlockPos> corruptedProbePoints = new ArrayList<>();
        ArrayList<BlockPos> uncorruptedProbePoints = new ArrayList<>();
        for(int probe = 0; probe < PROBE_COUNT; probe++) {
            BlockPos probePos = pos.add(rand.nextInt(2 * PROBE_RADIUS + 1) - PROBE_RADIUS, rand.nextInt(2 * PROBE_RADIUS + 1) - PROBE_RADIUS, rand.nextInt(2 * PROBE_RADIUS + 1) - PROBE_RADIUS);
            if(!worldIn.isBlockLoaded(probePos)) {
                uncorruptedProbePoints.add(probePos);
            } else if(worldIn.getBlockState(probePos).getMaterial().isSolid()) {
                if(worldIn.getBlockState(probePos).getBlock() == BlockInit.CORRUPTED) {
                    corruptedProbePoints.add(probePos);
                } else uncorruptedProbePoints.add(probePos);
            }
        }

        Vec3d uncorruptedAverage = Vec3d.ZERO;
        for(BlockPos uncorruptedProbePoint : uncorruptedProbePoints) uncorruptedAverage = uncorruptedAverage.add(new Vec3d(uncorruptedProbePoint));
        if(uncorruptedProbePoints.size() != 0) uncorruptedAverage.scale(1.0 / uncorruptedProbePoints.size());

        Vec3d corruptedAverage = Vec3d.ZERO;
        for(BlockPos corruptedProbePoint : corruptedProbePoints) corruptedAverage = corruptedAverage.add(new Vec3d(corruptedProbePoint));
        if(corruptedProbePoints.size() != 0) corruptedAverage.scale(1.0 / corruptedProbePoints.size());

        double corruptedDotUncorrupted = corruptedAverage.dotProduct(uncorruptedAverage);
        double directionDotUncorrupted = new Vec3d(spreadLocation).dotProduct(uncorruptedAverage);

        if(corruptedDotUncorrupted >= CDU_THRESHOLD && directionDotUncorrupted >= 0) {
            worldIn.setBlockState(pos.add(spreadLocation), BlockInit.CORRUPTED.getDefaultState());
        }
    }
}
