package personal.kqnade.model;

public class Statistics {
    private String algorithmName;
    private int stepCount;
    private int visitedCellCount;
    private int pathLength;
    private boolean completed;

    public Statistics(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }
    public int getStepCount() {
        return stepCount;
    }
    public int getVisitedCellCount() {
        return visitedCellCount;
    }
    public int getPathLength() {
        return pathLength;
    }
    public boolean isCompleted() {
        return completed;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }
    public void setVisitedCellCount(int visitedCellCount) {
        this.visitedCellCount = visitedCellCount;
    }
    public void setPathLength(int pathLength) {
        this.pathLength = pathLength;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void incrementStepCount() {
        this.stepCount++;
    }

    public double getEfficiency() {
        if (visitedCellCount == 0) {
            return 0;
        }
        return (double) pathLength / visitedCellCount;
    }
}
