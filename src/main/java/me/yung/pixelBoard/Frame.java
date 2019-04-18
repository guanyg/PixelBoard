package me.yung.pixelBoard;

public class Frame implements Cloneable {
    private final int height;
    private final int width;
    private String frameId;
    private String[][] data;

    Frame(int height, int width) {
        this.height = height;
        this.width = width;
        this.data = new String[height][width];
    }

    void update(FrameIncrement increment) {
        this.data[increment.getY()][increment.getX()] = increment.getColor();
    }

    public Frame clone() {
        Frame clone = null;
        try {
            clone = (Frame) super.clone();
            clone.data = new String[height][width];
            for (int i = 0; i < height; i++) {
                System.arraycopy(this.data[i], 0, clone.data[i], 0, this.data[i].length);
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return clone;
    }

    String getFrameId() {
        return frameId;
    }

    void setFrameId(String frameId) {
        this.frameId = frameId;
    }
}
