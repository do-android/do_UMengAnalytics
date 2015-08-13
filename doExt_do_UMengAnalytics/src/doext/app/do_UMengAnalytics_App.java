package doext.app;
import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import core.interfaces.DoIAppDelegate;

/**
 * APP启动的时候会执行onCreate方法；
 *
 */
public class do_UMengAnalytics_App implements DoIAppDelegate {

	private static do_UMengAnalytics_App instance;
	
	private do_UMengAnalytics_App(){
		
	}
	
	public static do_UMengAnalytics_App getInstance() {
		if(instance == null){
			instance = new do_UMengAnalytics_App();
		}
		return instance;
	}
	
	@Override
	public void onCreate(Context context) {
		MobclickAgent.setDebugMode(true);
		MobclickAgent.openActivityDurationTrack(false);
		MobclickAgent.updateOnlineConfig(context);
	}
	
	public String getModuleTypeID() {
		return "do_UMengAnalytics";
	}

	@Override
	public String getTypeID() {
		// TODO Auto-generated method stub
		return getModuleTypeID();
	}
}
