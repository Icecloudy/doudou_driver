/**  
 * Project Name:Android_Car_Example  
 * File Name:Utils.java  
 * Package Name:com.amap.api.car.example  
 * Date:2015年4月7日下午3:43:05  
 *  
*/  
  
package com.doudou.driver.ui.main.task;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.doudou.driver.R;

import java.util.ArrayList;


/**  
 * ClassName:Utils <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2015年4月7日 下午3:43:05 <br/>  
 * @author   yiyi.qi  
 * @version    
 * @since    JDK 1.6  
 * @see        
 */
public class Utils {

	private static ArrayList<Marker> markers=new ArrayList<>();
	
	/**  
	 * 添加模拟测试的车的点
	 */
	public static void addEmulateData(AMap amap, LatLng center){
		 if(markers.size()==0){
			 BitmapDescriptor bitmapDescriptor= BitmapDescriptorFactory
						.fromResource(R.drawable.white_car);
				
				for(int i=0;i<6;i++){
				double latitudeDelt=(Math.random()-0.5)*0.03;
				double longtitudeDelt=(Math.random()-0.5)*0.03;
				MarkerOptions markerOptions = new MarkerOptions();
				markerOptions.setFlat(true);
				markerOptions.anchor(0.5f, 0.5f);		
				markerOptions.icon(bitmapDescriptor);
				markerOptions.position(new LatLng(center.latitude+latitudeDelt, center.longitude+longtitudeDelt));
				Marker marker=amap.addMarker(markerOptions);
				markers.add(marker);
				}
		 }
		 else{
			 for(Marker marker:markers){
				double latitudeDelt=(Math.random()-0.5)*0.1;
				double longtitudeDelt=(Math.random()-0.5)*0.1;
				marker.setPosition(new LatLng(center.latitude+latitudeDelt, center.longitude+longtitudeDelt));
				 
			 }
		 }
	}
	public static void addEmulateData(AMap amap, LatLng center, int carCount){
		if(markers.size()==0){
			BitmapDescriptor bitmapDescriptor= BitmapDescriptorFactory
					.fromResource(R.drawable.white_car);

			for(int i=0;i<carCount;i++){
				double latitudeDelt=(Math.random()-0.5)*0.03;
				double longtitudeDelt=(Math.random()-0.5)*0.03;
				MarkerOptions markerOptions = new MarkerOptions();
				markerOptions.setFlat(true);
				markerOptions.anchor(0.5f, 0.5f);
				markerOptions.icon(bitmapDescriptor);
				markerOptions.position(new LatLng(center.latitude+latitudeDelt, center.longitude+longtitudeDelt));
				Marker marker=amap.addMarker(markerOptions);
				markers.add(marker);
			}
		}
		else{
			for(Marker marker:markers){
				double latitudeDelt=(Math.random()-0.5)*0.03;
				double longtitudeDelt=(Math.random()-0.5)*0.03;
				marker.setPosition(new LatLng(center.latitude+latitudeDelt, center.longitude+longtitudeDelt));

			}
		}
	}

	/**
	 * 移除marker
	 */
	public static void removeMarkers() {
		for(Marker marker:markers){
			marker.remove();
			marker.destroy();
		}
		markers.clear();
	}
	
}
  
