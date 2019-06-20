package me.pwns.logistica.util.zones;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashSet;

public class CuboidZone extends Zone {
    private Vec3d positionA;
    private Vec3d positionB;
    private Vec3d minPoint;
    private Vec3d maxPoint;

    private CuboidZone(Vec3d positionOne, Vec3d positionTwo, World world) {
        this.world = world;
        positionA = positionOne;
        positionB = positionTwo;
        players = new HashSet<>();

        // Creating a second set of points for min/max along each axis, to make the intersection detection cheaper later.
        double[] x = positionOne.x < positionTwo.x ? new double[]{positionOne.x, positionTwo.x} : new double[]{positionTwo.x, positionOne.x};
        double[] y = positionOne.y < positionTwo.y ? new double[]{positionOne.y, positionTwo.y} : new double[]{positionTwo.y, positionOne.y};
        double[] z = positionOne.z < positionTwo.z ? new double[]{positionOne.z, positionTwo.z} : new double[]{positionTwo.z, positionOne.z};
        minPoint = new Vec3d(x[0], y[0], z[0]);
        maxPoint = new Vec3d(x[1], y[1], z[1]);
    }

    @Override
    public boolean isInside(Vec3d position, World world) {
        return minPoint.x < position.x && position.x < maxPoint.x &&
                minPoint.y < position.y && position.y < maxPoint.y &&
                minPoint.z < position.z && position.z < maxPoint.z;
    }

    @Override
    public String getName() {
        return null;
    }
}
