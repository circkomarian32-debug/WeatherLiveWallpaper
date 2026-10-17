package com.weatherlive.wallpaper;
import android.content.Context;import android.location.*;import android.Manifest;import android.content.pm.PackageManager;import org.json.*;import java.net.*;import java.io.*;import java.util.*;
public class WeatherData {
 static volatile int code=0;static volatile boolean day=true;static volatile String status="Очікування геолокації";static volatile long updated=0;
 static String read(String u)throws Exception{HttpURLConnection c=(HttpURLConnection)new URL(u).openConnection();c.setConnectTimeout(12000);c.setReadTimeout(12000);try{InputStream in=c.getInputStream();ByteArrayOutputStream b=new ByteArrayOutputStream();byte[] a=new byte[4096];int n;while((n=in.read(a))!=-1)b.write(a,0,n);return b.toString("UTF-8");}finally{c.disconnect();}}
 static void refresh(Context context){if(System.currentTimeMillis()-updated<15*60*1000)return;try{
 LocationManager lm=(LocationManager)context.getSystemService(Context.LOCATION_SERVICE);Location loc=null;
 if(context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED||context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
 for(String provider:lm.getProviders(true)){try{Location x=lm.getLastKnownLocation(provider);if(x!=null&&(loc==null||x.getTime()>loc.getTime()))loc=x;}catch(Exception ignored){}}
 }
 if(loc==null){status="Немає координат. Увімкніть GPS і відкрийте програму";return;}
 String url=String.format(Locale.US,"https://api.open-meteo.com/v1/forecast?latitude=%.5f&longitude=%.5f&current=weather_code,is_day&timezone=auto",loc.getLatitude(),loc.getLongitude());JSONObject cur=new JSONObject(read(url)).getJSONObject("current");code=cur.getInt("weather_code");day=cur.getInt("is_day")==1;updated=System.currentTimeMillis();status="Погода оновлена";
 }catch(Exception e){status="Помилка погоди: "+e.getClass().getSimpleName();}}
 static String scene(){if(!day)return "night";if(code==0||code==1)return "sun";if(code==2||code==3)return "cloud";if(code==45||code==48)return "cloud";if(code>=51&&code<=82)return "rain";if(code>=95)return "rain";if(code>=71&&code<=77||code>=85&&code<=86)return "snow";return "cloud";}
}
