package plugins;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * js调用java方法 
 * 
 * 必须继承CordovaPlugin   
 * CordovaPlugin里面有实现cordovaActivity的方法  提供startActivityForResult();
 * 
 * 我使用的   cordova  3.3.0版本
 * @author Baozi
 *
 */
public class Plugin_intent extends CordovaPlugin {

	/**
	 * 注意 构造方法不能为
	 * 
	 * Plugin_intent(){}
	 * 
	 * 可以不写或者 定义为如下
	 * 
	 */
	public Plugin_intent() {
	}

	CallbackContext callbackContext;

	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		this.callbackContext = callbackContext;
		Log.i("123", action);

		if (action.equals("intent")) {
			// 获取args的第一个参数
			String message = args.getString(0);
			this.function();
			return true;
		}
		return false;

	}

	private void function() {
		// cordova.getActivity() 获取当前activity的this
		Log.i("123", cordova.getActivity().toString());
        Toast.makeText(cordova.getActivity(), "Cordova Plugin Intent", Toast.LENGTH_SHORT).show();
//		Intent intent = new Intent(cordova.getActivity(), DemoActivity.class);
//		cordova.startActivityForResult((CordovaPlugin) this,intent, 200);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		
		super.onActivityResult(requestCode, resultCode, intent);
		//传递返回值 给js方法
		callbackContext.success("activity 跳转成功了");
        Toast.makeText(cordova.getActivity(), "123", Toast.LENGTH_SHORT).show();

	}

}
