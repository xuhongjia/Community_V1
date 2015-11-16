package cn.andrewlu.community.ui.user;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.andrewlu.community.R;

public class AddressMapPonit extends Activity implements LocationSource, AMapLocationListener, OnCameraChangeListener, OnGeocodeSearchListener{
	private MapView mapView;
	private AMap aMap;
	private UiSettings mUiSettings;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy aMapManager;
	private TextView address_display;
	private GeocodeSearch geocoderSearch;
	private Button address_sure;
	@Override
	public void onCreate(Bundle b){
		super.onCreate(b);
		setContentView(R.layout.address_map_point);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(b);// 必须要写
		init();

	}
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			mUiSettings = aMap.getUiSettings();
		}
		address_display=(TextView) findViewById(R.id.address_display);
		address_sure=(Button) findViewById(R.id.address_sure);
		aMap.setLocationSource(this);// 设置定位监听
		mUiSettings.setMyLocationButtonEnabled(true); // 是否显示默认的定位按钮
		aMap.setMyLocationEnabled(true);
		aMap.moveCamera(CameraUpdateFactory.zoomTo((float) 17.0));
		mUiSettings.setCompassEnabled(true);
		mUiSettings.setScaleControlsEnabled(true);
		aMap.setOnCameraChangeListener(this);
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		//回传选点地址
		address_sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent();
				i.putExtra("address", address_display.getText());
				setResult(1,i);
				finish();
			}
		});
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mapView.onDestroy();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapView.onPause();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapView.onResume();
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (mListener != null) {
			mListener.onLocationChanged(aLocation);// 显示系统小蓝点
			CameraUpdateFactory.zoomIn();
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (aMapManager == null) {
			aMapManager = LocationManagerProxy.getInstance(this);
			/*
			 * mAMapLocManager.setGpsEnable(false);//
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true
			 */
			// Location API定位采用GPS和网络混合定位方式，时间最短是2000毫秒
			aMapManager.requestLocationData(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (aMapManager != null) {
			aMapManager.removeUpdates(this);
			aMapManager.destroy();
			
		}
		aMapManager = null;
	}
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onCameraChange(CameraPosition arg0) {
		// TODO Auto-generated method stub
		address_display.setVisibility(View.GONE);
	}
	@Override
	public void onCameraChangeFinish(CameraPosition arg0) {
		// TODO Auto-generated method stub
		getAddress();
		address_display.setVisibility(View.VISIBLE);
	}
	@Override
	public void onGeocodeSearched(GeocodeResult arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onRegeocodeSearched(RegeocodeResult arg0, int arg1) {
		// TODO Auto-generated method stub
		String addressName = arg0.getRegeocodeAddress().getFormatAddress();
		address_display.setText(addressName);
	}
	/**
	 * 响应逆地理编码
	 */
	public void getAddress() {
		LatLonPoint latLonPoint = new LatLonPoint(aMap.getCameraPosition().target.latitude, aMap.getCameraPosition().target.longitude);
		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 5,
				GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
		geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
	}
}

