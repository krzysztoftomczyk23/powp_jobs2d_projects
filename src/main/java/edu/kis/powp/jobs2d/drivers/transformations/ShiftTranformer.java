package edu.kis.powp.jobs2d.drivers.transformations;

public class ShiftTranformer implements CoordinateTransformer {
    private final int dx;
    private final int dy;

    public ShiftTranformer(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public int[] transform(int x, int y) {
        return new int[]{x + dx, y + dy};
    }
}