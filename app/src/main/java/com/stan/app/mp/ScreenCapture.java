package com.stan.app.mp;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.display.DisplayManager;
import android.view.Display;

public class ScreenCapture {
    public class DisplayManager {
        private DisplayManager displayManager;

        public DisplayManager(Context context) {
            displayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        }

        public Display getDisplay(int displayId) {
            return displayManager.getDisplay(displayId);
        }

        public int getScreenWidth(int displayId) {
            return getDisplay(displayId).getWidth();
        }

        public int getScreenHeight(int displayId) {
            return getDisplay(displayId).getHeight();
        }
    }


}
