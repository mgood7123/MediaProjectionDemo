package screen.utils;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.ImageView;

import static android.app.Activity.RESULT_OK;

public class ScreenUtils {

    public Variables variables = new Variables();

    public void onCreate(Service service, ImageView imageView) {
        // service overload
        variables.service = service;
        variables.imageView = imageView;
        variables.mProjectionManager = variables.mediaProjectionHelper.getMediaProjectionManager();
    }

    public void onCreate(Activity activity, ImageView imageView) {
        variables.activity = activity;
        variables.imageView = imageView;
        variables.mProjectionManager = variables.mediaProjectionHelper.getMediaProjectionManager();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == variables.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                variables.mediaProjectionHelper.startCapture(resultCode, data);
            }
        } else if (requestCode == variables.REQUEST_CODE_FLOATING_WINDOW) {
            if (resultCode == RESULT_OK) {
                startFloatingWindowService();
            }
        }
    }

    public void createFloatingWidget() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkDrawOverlayPermission()) {
                startFloatingWindowService();
            }
        }
    }

    void startFloatingWindowService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(variables.activity)) {
                variables.activity.startService(new Intent(variables.activity, FloatingViewService.class));
            }
        }
    }

    public boolean checkDrawOverlayPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (!Settings.canDrawOverlays(variables.activity)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + variables.activity.getPackageName()));
            variables.activity.startActivityForResult(intent, variables.REQUEST_CODE_FLOATING_WINDOW);
            return false;
        } else {
            return true;
        }
    }

    public void startScreenRecord() {
        variables.mediaProjectionHelper.startScreenRecord();
    }

    public void stopScreenRecord() {
        variables.mediaProjectionHelper.stopScreenRecord();
    }

    public void takeScreenShot() {
        variables.mediaProjectionHelper.takeScreenShot();
    }

    public void startScreenMirror() {
        variables.mediaProjectionHelper.startScreenMirror();
    }

    public void stopScreenMirror() {
        variables.mediaProjectionHelper.stopScreenMirror();
    }
}
