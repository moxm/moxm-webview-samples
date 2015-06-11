package org.apache.cordova.camera;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * This class launches the camera view, allows the user to take a picture, closes the camera view,
 * and returns the captured image.  When the camera view is closed, the screen displayed before
 * the camera view was shown is redisplayed.
 */
public class GetPhotoPlugin extends CameraLauncher {
    private AlertDialog dialog = null;

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action        	The action to execute.
     * @param args          	JSONArry of arguments for the plugin.
     * @param callbackContext   The callback id used when calling back into JavaScript.
     * @return              	A PluginResult object with a status and message.
     */
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;

        Log.d("CameraLauncher","--->"+action);

        if (action.equals("takePicture")) {
            this.saveToPhotoAlbum = false;
            this.targetHeight = 0;
            this.targetWidth = 0;
            this.encodingType = JPEG;
            this.mediaType = PICTURE;
            this.mQuality = 80;
            int srcType = CAMERA;

            this.mQuality = args.getInt(0);
            this.destType = args.getInt(1);
            srcType = args.getInt(2);
            this.targetWidth = args.getInt(3);
            this.targetHeight = args.getInt(4);
            this.encodingType = args.getInt(5);
            this.mediaType = args.getInt(6);
            //this.allowEdit = args.getBoolean(7); // This field is unused.
            this.correctOrientation = args.getBoolean(8);
            this.saveToPhotoAlbum = args.getBoolean(9);

            // If the user specifies a 0 or smaller width/height
            // make it -1 so later comparisons succeed
            if (this.targetWidth < 1) {
                this.targetWidth = -1;
            }
            if (this.targetHeight < 1) {
                this.targetHeight = -1;
            }

            PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);
            r.setKeepCallback(true);
            callbackContext.sendPluginResult(r);
            createPictureDialog().show();

            return true;
        }
        return false;
    }

    public AlertDialog createPictureDialog(){
        if(dialog == null){
            dialog = new AlertDialog.Builder(this.cordova.getActivity()).setTitle("请选择").setSingleChoiceItems(new String[]{"拍照", "手机相册"}, 0,
                    new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.dismiss();
                            switch (i){
                                case 0://从相机拍照获取图片
                                    try {
                                        takePicture(destType, encodingType);
                                    } catch (IllegalArgumentException e) {
                                        callbackContext.error("Illegal Argument Exception");
                                        PluginResult r = new PluginResult(PluginResult.Status.ERROR);
                                        callbackContext.sendPluginResult(r);
                                        return;
                                    }
                                    break;
                                case 1://从手机相册获取图片
                                    try {
                                        getImage(PHOTOLIBRARY, destType, encodingType);
                                    } catch (IllegalArgumentException e) {
                                        callbackContext.error("Illegal Argument Exception");
                                        PluginResult r = new PluginResult(PluginResult.Status.ERROR);
                                        callbackContext.sendPluginResult(r);
                                        return;
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }).setNegativeButton("取消", null).create();
        }
        return dialog;
    }
}
