package me.pwns.logistica.util.zones;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.vecmath.Vector3d;
import java.util.HashSet;
import java.util.Set;

public class CuboidZone implements Zone {
    private Vector3d positionA;
    private Vector3d positionB;
    private Vector3d minPoint;
    private Vector3d maxPoint;
    private Set<PlayerEntity> players;
    private World world;

    public CuboidZone(BlockPos positionOne, BlockPos positionTwo, World world) {

        Vector3d vectorOne = new Vector3d(positionOne.getX(), positionOne.getY(), positionOne.getZ());
        Vector3d vectorTwo = new Vector3d(positionTwo.getX(), positionTwo.getY(), positionTwo.getZ());
        constructorBase(vectorOne, vectorTwo, world);
    }

    public CuboidZone(Vector3d positionOne, Vector3d positionTwo, World world) {
        constructorBase(positionOne, positionTwo, world);
    }

    private void constructorBase(Vector3d positionOne, Vector3d positionTwo, World world) {
        this.world = world;
        positionA = positionOne;
        positionB = positionTwo;
        players = new HashSet<>();

        // Creating a second set of points for min/max along each axis, to make the intersection detection cheaper later.
        double[] x = positionOne.x < positionTwo.x ? new double[]{positionOne.x, positionTwo.x} : new double[]{positionTwo.x, positionOne.x};
        double[] y = positionOne.y < positionTwo.y ? new double[]{positionOne.y, positionTwo.y} : new double[]{positionTwo.y, positionOne.y};
        double[] z = positionOne.z < positionTwo.z ? new double[]{positionOne.z, positionTwo.z} : new double[]{positionTwo.z, positionOne.z};
        minPoint = new Vector3d(x[0], y[0], z[0]);
        maxPoint = new Vector3d(x[1], y[1], z[1]);
    }

    /**
     * @param position The position to check
     * @param world    The world corresponding to the position to check.
     * @return True if position is inside this zone. False if not.
     */
    @Override
    public boolean isInside(Vector3d position, World world) {
        return minPoint.x < position.x && position.x < maxPoint.x &&
                minPoint.y < position.y && position.y < maxPoint.y &&
                minPoint.z < position.z && position.z < maxPoint.z;
    }

    @Override
    public Set<PlayerEntity> getPlayers() {
        return players;
    }

    @Override
    public boolean addPlayer(PlayerEntity player) {

        if (players.contains(player)) {
            return false;
        }
        players.add(player);
        return true;
    }

    @Override
    public boolean removePlayer(PlayerEntity player) {
        if (!players.contains(player)) {
            return false;
        }
        players.remove(player);
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}
