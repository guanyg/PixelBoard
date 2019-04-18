package me.yung.pixelBoard;

class FrameIncrement {
    private int x;
    private int y;
    private String color;

    static FrameIncrement parse(String msg) {
        FrameIncrement ret = new FrameIncrement();
        String[] msgArr = msg.split("#");
        if (msgArr.length == 4 && "INC".equals(msgArr[0])) {
            ret.x = Integer.valueOf(msgArr[1]);
            ret.y = Integer.valueOf(msgArr[2]);
            ret.color = msgArr[3];
        }
        return ret;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    String getColor() {
        return color;
    }

    String buildMessage() {
        return String.format("INC#%d#%d#%s", x, y, color);
    }
}
