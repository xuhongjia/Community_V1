package cn.andrewlu.community.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GeneralResponse<T> {
	public static <T> GeneralResponse<T> fromJson(String json, Class<T> c) {

        GeneralResponse<T> response = new Gson().fromJson(json,
                new TypeToken<GeneralResponse<T>>() {
                }.getType());
        return response;
    }
	
	@Override
    public String toString() {
        return new Gson().toJson(this);
    }
	
	private String msg;
    private boolean success = true;
    private T data;
    
    public GeneralResponse(){
    	
    }
    
    public GeneralResponse(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public  T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
	
    /**
     * 判断对象值得情况并赋值
     * @param obj 判断的对象
     * @param successMsg 成功信息
     * @param errorMsg 失败信息
     * @return
     */
    public String getJsonString(T obj,String successMsg,String errorMsg)
    {
    	if(obj==null)
    	{
    		setMsg(errorMsg);
    		setSuccess(false);
    	}
    	else
    	{
    		setMsg(successMsg);
    		setSuccess(true);
    	}
    	
    	setData(obj);
    	
    	return this.toString();
    }
    /**
     * 判断更新，查找是否成功
     * @param obj
     * @param successMsg
     * @param errorMsg
     * @return
     */
    public String getJsonString(int obj,String successMsg,String errorMsg)
    {
    	if(obj==0)
    	{
    		setMsg(errorMsg);
    		setSuccess(false);
    	}
    	else
    	{
    		setMsg(successMsg);
    		setSuccess(true);
    	}
    	
    	return this.toString();
    }
}
