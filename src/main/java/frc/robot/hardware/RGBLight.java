package frc.robot.hardware;

public class RGBLight {

    Motor ctrl;
    final static double minColor = 0.57, maxColor = 0.99;

    public static class Color {
        final public static double RED = 0.61, BLUE = 0.87;
    }

    public RGBLight(int port) {
        ctrl = new Motor(port, Motor.Model.SPARK);
    }

    private static double truncate(double value, int decimals) {
        // Ugly code, but this works
        return ((double) ((int) (value * Math.pow(10, decimals)))) / Math.pow(10, decimals);
    }

    public void setColor(int pos) {

        if (pos <= 0 || pos > 100)
            return;

        final double colorValue = ((pos == 0 ? 1 : pos) / 100) * maxColor;

        ctrl.move(truncate(colorValue, 2));
    }

    public void setMode(double value) {
        ctrl.move(value);
    }
}
