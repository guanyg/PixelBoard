package me.yung.pixelBoard;

import java.util.List;

public interface FrameListener {
    void onFrameUpdate(List<FrameIncrement> ev);
}
