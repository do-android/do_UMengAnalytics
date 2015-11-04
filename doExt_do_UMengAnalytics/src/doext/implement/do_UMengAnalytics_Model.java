package doext.implement;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

import core.DoServiceContainer;
import core.helper.DoJsonHelper;
import core.interfaces.DoIScriptEngine;
import core.object.DoInvokeResult;
import core.object.DoSingletonModule;
import doext.define.do_UMengAnalytics_IMethod;

/**
 * 自定义扩展SM组件Model实现，继承DoSingletonModule抽象类，并实现do_UMengAnalytics_IMethod接口方法；
 * #如何调用组件自定义事件？可以通过如下方法触发事件：
 * this.model.getEventCenter().fireEvent(_messageName, jsonResult);
 * 参数解释：@_messageName字符串事件名称，@jsonResult传递事件参数对象； 获取DoInvokeResult对象方式new
 * DoInvokeResult(this.getUniqueKey());
 */
public class do_UMengAnalytics_Model extends DoSingletonModule implements do_UMengAnalytics_IMethod {

	public do_UMengAnalytics_Model() throws Exception {
		super();
		MobclickAgent.setDebugMode(true);
//		MobclickAgent.openActivityDurationTrack(false);
	}

	/**
	 * 同步方法，JS脚本调用该组件对象方法时会被调用，可以根据_methodName调用相应的接口实现方法；
	 * 
	 * @_methodName 方法名称
	 * @_dictParas 参数（K,V）
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public boolean invokeSyncMethod(String _methodName, JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		if ("beginPageLog".equals(_methodName)) {
			beginPageLog(_dictParas, _scriptEngine, _invokeResult);
			return true;
		}
		if ("endPageLog".equals(_methodName)) {
			endPageLog(_dictParas, _scriptEngine, _invokeResult);
			return true;
		}
		if ("eventLog".equals(_methodName)) {
			eventLog(_dictParas, _scriptEngine, _invokeResult);
			return true;
		}
		if ("eventValueLog".equals(_methodName)) {
			eventValueLog(_dictParas, _scriptEngine, _invokeResult);
			return true;
		}
		if ("readConfig".equals(_methodName)) {
			readConfig(_dictParas, _scriptEngine, _invokeResult);
			return true;
		}
		if ("setCrashReportEnabled".equals(_methodName)) {
			setCrashReportEnabled(_dictParas, _scriptEngine, _invokeResult);
			return true;
		}
		if ("setEncryptLog".equals(_methodName)) {
			setEncryptLog(_dictParas, _scriptEngine, _invokeResult);
			return true;
		}
		return super.invokeSyncMethod(_methodName, _dictParas, _scriptEngine, _invokeResult);
	}

	/**
	 * 异步方法（通常都处理些耗时操作，避免UI线程阻塞），JS脚本调用该组件对象方法时会被调用， 可以根据_methodName调用相应的接口实现方法；
	 * 
	 * @_methodName 方法名称
	 * @_dictParas 参数（K,V）
	 * @_scriptEngine 当前page JS上下文环境
	 * @_callbackFuncName 回调函数名 #如何执行异步方法回调？可以通过如下方法：
	 *                    _scriptEngine.callback(_callbackFuncName,
	 *                    _invokeResult);
	 *                    参数解释：@_callbackFuncName回调函数名，@_invokeResult传递回调函数参数对象；
	 *                    获取DoInvokeResult对象方式new
	 *                    DoInvokeResult(this.getUniqueKey());
	 */
	@Override
	public boolean invokeAsyncMethod(String _methodName, JSONObject _dictParas, DoIScriptEngine _scriptEngine, String _callbackFuncName) throws Exception {
		// ...do something
		return super.invokeAsyncMethod(_methodName, _dictParas, _scriptEngine, _callbackFuncName);
	}

	/**
	 * 进入页面的统计；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public void beginPageLog(JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		String pageName = DoJsonHelper.getString(_dictParas, "pageName", "");
		MobclickAgent.onPageStart(pageName);
		MobclickAgent.onResume(DoServiceContainer.getPageViewFactory().getAppContext());
	}

	/**
	 * 离开页面的统计；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public void endPageLog(JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		String pageName = DoJsonHelper.getString(_dictParas, "pageName", "");
		MobclickAgent.onPageEnd(pageName);
		MobclickAgent.onPause(DoServiceContainer.getPageViewFactory().getAppContext());
	}

	/**
	 * 单次事件统计；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public void eventLog(JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		String id = DoJsonHelper.getString(_dictParas, "id", "");
		JSONObject data = DoJsonHelper.getJSONObject(_dictParas, "data");
		HashMap<String, String> map = new HashMap<String, String>();
		Map<String, Object> keyValues = DoJsonHelper.getAllKeyValues(data);
		if (keyValues != null) {
			for (String name : keyValues.keySet()) {
				if (name == null || name.length() <= 0)
					continue;
				map.put(name, DoJsonHelper.getText(keyValues.get(name), ""));
			}
		}
		MobclickAgent.onEvent(DoServiceContainer.getPageViewFactory().getAppContext(), id, map);
	}

	/**
	 * 单次事件计算；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public void eventValueLog(JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		String id = DoJsonHelper.getString(_dictParas, "id", "");
		JSONObject data = DoJsonHelper.getJSONObject(_dictParas, "data");
		int counter = DoJsonHelper.getInt(_dictParas, "counter", 0);
		HashMap<String, String> map = new HashMap<String, String>();
		Map<String, Object> keyValues = DoJsonHelper.getAllKeyValues(data);
		if (keyValues != null) {
			for (String name : keyValues.keySet()) {
				if (name == null || name.length() <= 0)
					continue;
				map.put(name, DoJsonHelper.getText(keyValues.get(name), ""));
			}
		}
		MobclickAgent.onEventValue(DoServiceContainer.getPageViewFactory().getAppContext(), id, map, counter);
	}

	/**
	 * 读取在线参数；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public void readConfig(JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		String configID = DoJsonHelper.getString(_dictParas, "configID", "");
		String value = MobclickAgent.getConfigParams(DoServiceContainer.getPageViewFactory().getAppContext(), configID);
		_invokeResult.setResultText(value);
	}

	/**
	 * 是否统计后异常信息；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public void setCrashReportEnabled(JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		boolean value = DoJsonHelper.getBoolean(_dictParas, "value", true);
		MobclickAgent.setCatchUncaughtExceptions(value);
		throw new RuntimeException("123.substring(10) 崩溃了");
	}

	/**
	 * 是否加密传输日志；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public void setEncryptLog(JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		boolean value = DoJsonHelper.getBoolean(_dictParas, "value", true);
		AnalyticsConfig.enableEncrypt(value);
	}
}