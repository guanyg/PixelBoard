package me.yung.pixelBoard;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

class FrameWrapper {
    private static Supplier<String> fidGenerator = new Supplier<String>() {
        private int count = 1;

        @Override
        public synchronized String get() {
            return "FRAME" + (count++);
        }
    };
    private transient Set<FrameListener> listeners = new HashSet<>();
    private Frame frame;

    FrameWrapper(Frame frame) {
        this.frame = frame;
        this.frame.setFrameId(fidGenerator.get());
    }

    FrameWrapper fork() {
        return new FrameWrapper(this.frame.clone());
    }

    void update(FrameIncrement increment) {
        frame.update(increment);
        for (FrameListener listener :
                listeners) {
            listener.onFrameUpdate(Collections.singletonList(increment));
        }
    }

    void addListener(FrameListener frameListener) {
        listeners.add(frameListener);
    }

    void removeListener(FrameListener frameListener) {
        listeners.remove(frameListener);
    }

    String getFrameId() {
        return frame.getFrameId();
    }

    Frame getFrame() {
        return frame;
    }
}
