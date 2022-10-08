/**
 * The class that represents three-dimensional points.
 */
public class Point {

    // x, y, z present the x, y and z coordinate of the point
    // zLayerNumber and zLayIndex represent the imaginary number and index for the point based on
    // its location in the 3d strip zones.
    public double x, y, z;
    int[] zLayerNumber;
    int[] zLayerIndex;

    /**
     * Constructor for the class.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        zLayerNumber = new int[3];
        zLayerIndex = new int[3];
    }

    /**
     * Gets the distance between two 3d points.
     *
     * @param p1 the first point
     * @param p2 the second point
     * @return the distance between the two 3d points
     */
    public static double getDistance(Point p1, Point p2) {
        double x_distance_square = Math.pow((p1.x - p2.x), 2);
        double y_distance_square = Math.pow((p1.y - p2.y), 2);
        double z_distance_square = Math.pow((p1.z - p2.z), 2);
        return Math.sqrt(x_distance_square + y_distance_square + z_distance_square);
    }

    /**
     * Returns a string representation of the point.
     *
     * @return a string representation of the point
     */
    @Override
    public String toString() {
        return "(" + x + " " + y + " " + z + ") ";
    }
}
