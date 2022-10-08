import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * This program is able to find the pair of 3d points with the shortest Euclidean distance among the
 * points provided. It uses the methodology of divide and conquer and the time complexity is
 * (n*logn).
 */
public class ClosestPair {

    // pointsSortedByX stores the input points with points sorted based on x.
    // closestPair stores the closest pair of points.
    private Point[] pointsSortedByX;
    private Point[] closestPair = new Point[2];

    /**
     * Gets the closest pair of points.
     *
     * @return the closest pair of points
     * @throws IllegalStateException if the pair is empty
     */
    public Point[] getClosestPair() {
        if (closestPair[0] == null || closestPair[1] == null) {
            throw new IllegalStateException("Closest pair not generated yet!");
        }
        return closestPair;
    }

    /**
     * Gets the closest distance between points.
     *
     * @return the closest distance between points
     * @throws IllegalArgumentException if the pair is empty
     */
    public double getClosestDistance() {
        if (closestPair[0] == null || closestPair[1] == null) {
            throw new IllegalStateException("Closest pair not generated yet!");
        }
        return Point.getDistance(closestPair[0], closestPair[1]);
    }

    /**
     * Updates the closest pair.
     *
     * @param pair the potential pair with a shorter distance
     */
    private void updateClosestPair(Point[] pair) {
        if (Point.getDistance(pair[0], pair[1]) < getClosestDistance()) {
            closestPair[0] = pair[0];
            closestPair[1] = pair[1];
        }
    }

    /**
     * Searches the closest pair.
     *
     * @param points an array of points
     */
    public void search(Point[] points) {
        if (points.length < 2) {
            throw new IllegalArgumentException(
                "Invalid argument. Should contain at least 2 points.");
        }

        // Initialize pointsSortedByX and sort points by x.
        pointsSortedByX = new Point[points.length];
        pointsSortedByX = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsSortedByX, Comparator.comparingDouble(cur -> cur.x));

        // Initialize the closestPair.
        closestPair[0] = pointsSortedByX[0];
        closestPair[1] = pointsSortedByX[1];

        // Initialize pointsSortedByY array.
        Point[] pointsSortedByY = new Point[points.length];
        pointsSortedByY = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsSortedByY, Comparator.comparingDouble(cur -> cur.y));

        search(0, Arrays.asList(pointsSortedByY));
    }

    /**
     * Recursively searches the specified list of points, updates the current closest pair, and
     * returns the current smallest distance.
     *
     * @param start           the starting index
     * @param pointsSortedByY the specified list of points to be searched
     * @return the current smallest distance
     */
    public double search(int start, List<Point> pointsSortedByY) {
        // Handles base cases
        int size = pointsSortedByY.size();
        if (size <= 3) {
            return handleBaseCase(pointsSortedByY);
        }

        // Divides the points into left and right sections.
        int middle = start + size / 2;
        Point middlePoint = pointsSortedByX[middle];
        List<Point> left = new ArrayList<>();
        List<Point> right = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            if (pointsSortedByY.get(i).x <= middlePoint.x) {
                left.add(pointsSortedByY.get(i));
            } else if (pointsSortedByY.get(i).x > middlePoint.x) {
                right.add(pointsSortedByY.get(i));
            }
        }

        // Gets the current delta - the minimum distance from both left and right sections.
        double closestDistanceFromLeft = search(start, left);
        double closestDistanceFromRight = search(start + left.size(), right);
        double delta = Math.min(closestDistanceFromLeft, closestDistanceFromRight);

        // Finds and puts points that are close to the middle(distance < delta) into the
        // strip section.
        List<Point> strip = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            double distanceToMiddle = Math.abs(pointsSortedByY.get(i).x - middlePoint.x);
            if (distanceToMiddle < delta) {
                strip.add(pointsSortedByY.get(i));
            }
        }

        return searchStrip(strip, delta);
    }

    /**
     * Searches the strip section, updates the closest pair, and returns the current smallest
     * distance.
     *
     * @param strip the strip sections that contains points
     * @param delta the smallest distance between two points carried on from left and right
     *              sections
     * @return the current smallest distance
     */
    private double searchStrip(List<Point> strip, double delta) {
        // Initializes the closestDistance
        double closestDistance = delta;

        // Handles base cases
        int size = strip.size();
        if (size < 2) {
            return closestDistance;
        }
        if (size <= 3) {
            double curDistance = handleBaseCase(strip);
            if (curDistance < closestDistance) {
                closestDistance = curDistance;
            }
            return closestDistance;
        }

        // When the number of points in the strip is greater than 3, gets the range of z of points
        // in the strip for further processing.
        double minZ = strip.get(0).z;
        double maxZ = strip.get(0).z;
        for (Point p : strip) {
            if (p.z < minZ) {
                minZ = p.z;
            }
            if (p.z > maxZ) {
                maxZ = p.z;
            }
        }

        // Creates an array of list of points to store the points in different subsections.
        double rangeOfZ = maxZ - minZ;
        int zLayerCount = (int) (rangeOfZ / delta) + 1;
        ArrayList<Point>[] zLayers = new ArrayList[zLayerCount];
        for (int i = 0; i < zLayers.length; i++) {
            zLayers[i] = new ArrayList<>();
        }

        // Organize points and put points into imaginary zLayers they belong to based on their
        // locations in the 3d strip zone.
        // zLayerNumber is assigned because we only need to care about the distances between this
        // point and points located before or after the zLayer that this point belongs to. Points
        // from other zLayers will be absolutely further.
        // zLayerNumber[1] indicates the zLayerNumber location of this point.
        // zLayerNumber[0] indicates the previous zLayerNumber location of this point.
        // zLayerNumber[1] indicates the next zLayerNumber location of this point.
        for (int i = 0; i < size; i++) {
            // Updates the zLayerNumber and zLayerIndex for the current point.
            strip.get(i).zLayerNumber[1] = (int) ((strip.get(i).z - minZ) / delta);
            zLayers[strip.get(i).zLayerNumber[1]].add(strip.get(i));
            strip.get(i).zLayerIndex[1] = zLayers[(strip.get(i).zLayerNumber[1])].size() - 1;

            strip.get(i).zLayerNumber[0] = strip.get(i).zLayerNumber[1] - 1;
            strip.get(i).zLayerNumber[2] = strip.get(i).zLayerNumber[1] + 1;
            // Handle edge cases.
            if (strip.get(i).zLayerNumber[1] != 0) {
                strip.get(i).zLayerIndex[0] = zLayers[strip.get(i).zLayerNumber[0]].size() - 1;
            }
            if (strip.get(i).zLayerNumber[1] != zLayerCount - 1) {
                strip.get(i).zLayerIndex[2] = zLayers[strip.get(i).zLayerNumber[2]].size() - 1;
            }
        }

        // Attempts to find any shorter distance in the 3d strip section.
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < 3; j++) {
                // Invalid cases.
                if (strip.get(i).zLayerNumber[j] < 0
                    || strip.get(i).zLayerNumber[j] >= zLayerCount) {
                    continue;
                }
                for (int k = strip.get(i).zLayerIndex[j] + 1;
                    k < (int) zLayers[(strip.get(i).zLayerNumber[j])].size(); k++) {
                    // Unnecessary cases.
                    if (zLayers[(strip.get(i).zLayerNumber[j])].get(k).y > strip.get(i).y + delta) {
                        break;
                    }
                    double curDistance = Point.getDistance(strip.get(i),
                        zLayers[(strip.get(i).zLayerNumber[j])].get(k));
                    if (curDistance < closestDistance) {
                        closestDistance = curDistance;
                        Point[] curPair = new Point[]{strip.get(i),
                            zLayers[(strip.get(i).zLayerNumber[j])].get(k)};
                        updateClosestPair(curPair);
                    }
                }
            }
        }

        return closestDistance;
    }

    /**
     * Brute force approach that can be used for the bases cases.
     *
     * @param points the array of points to search
     * @return the closest pair of points
     * @throws IllegalArgumentException if the argument is invalid
     */
    private double handleBaseCase(List<Point> points) {
        if (points == null || points.size() > 3) {
            throw new IllegalArgumentException(
                "Invalid argument. Number of points should be either 2 or 3 for base case.");
        }

        double result = Double.MAX_VALUE;

        for (int i = 0; i < points.size() - 1; i++) {
            for (int j = i + 1; j < points.size(); j++) {
                double curDistance = Point.getDistance(points.get(i), points.get(j));
                if (curDistance < getClosestDistance()) {
                    updateClosestPair(new Point[]{points.get(i), points.get(j)});
                    result = curDistance;
                }
            }
        }

        return result;
    }
}
