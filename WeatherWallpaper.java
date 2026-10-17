package com.weatherlive.wallpaper;
import android.service.wallpaper.WallpaperService;import android.view.*;import android.graphics.*;import android.os.*;import java.util.*;
public class WeatherWallpaper extends WallpaperService{
 public Engine onCreateEngine(){return new EngineImpl();}
 class EngineImpl extends Engine{
 final Handler handler=new Handler();boolean visible=false;Paint paint=new Paint(3);Random random=new Random(91);float[] xs=new float[180],ys=new float[180],speed=new float[180];Bitmap sun,rain,snow,night,cloud;String current="";long lastFetch=0;
 EngineImpl(){int[] ids={R.drawable.sun,R.drawable.rain,R.drawable.snow,R.drawable.night,R.drawable.cloud};Bitmap[] a=new Bitmap[5];for(int i=0;i<5;i++)a[i]=BitmapFactory.decodeResource(getResources(),ids[i]);sun=a[0];rain=a[1];snow=a[2];night=a[3];cloud=a[4];for(int i=0;i<180;i++){xs[i]=random.nextFloat();ys[i]=random.nextFloat();speed[i]=.4f+random.nextFloat()*1.5f;}}
 Runnable frame=new Runnable(){public void run(){draw();if(visible)handler.postDelayed(this,33);}};
 public void onVisibilityChanged(boolean v){visible=v;handler.removeCallbacks(frame);if(v){fetch();handler.post(frame);}}
 void fetch(){if(System.currentTimeMillis()-lastFetch<60000)return;lastFetch=System.currentTimeMillis();new Thread(()->WeatherData.refresh(getApplicationContext())).start();}
 Bitmap select(){String s=WeatherData.scene();switch(s){case "sun":return sun;case "rain":return rain;case "snow":return snow;case "night":return night;default:return cloud;}}
 void draw(){SurfaceHolder h=getSurfaceHolder();Canvas c=null;try{c=h.lockCanvas();if(c==null)return;int w=c.getWidth(),hh=c.getHeight();Bitmap b=select();Rect src=new Rect(0,0,b.getWidth(),b.getHeight());float scale=Math.max((float)w/b.getWidth(),(float)hh/b.getHeight());int sw=(int)(w/scale),sh=(int)(hh/scale);int x=(b.getWidth()-sw)/2,y=(b.getHeight()-sh)/2;src.set(x,y,x+sw,y+sh);c.drawBitmap(b,src,new Rect(0,0,w,hh),paint);String scene=WeatherData.scene();boolean snowy=scene.equals("snow"),raining=scene.equals("rain");if(snowy||raining){int count=raining?160:110;for(int i=0;i<count;i++){float xx=xs[i]*w,yy=ys[i]*hh;paint.setColor(snowy?Color.argb(200,255,255,255):Color.argb(110,183,216,255));paint.setStrokeWidth(snowy?2.8f:1.5f);if(snowy)c.drawCircle(xx,yy,2.5f+speed[i],paint);else c.drawLine(xx,yy,xx-8,yy+20*speed[i],paint);ys[i]+=(snowy?.0015f:.008f)*speed[i];xs[i]+=(snowy?.0003f:-.0007f);if(ys[i]>1){ys[i]=0;xs[i]=random.nextFloat();}if(xs[i]<0)xs[i]=1;}}paint.setColor(Color.WHITE);fetch();}catch(Exception ignored){}finally{if(c!=null)try{h.unlockCanvasAndPost(c);}catch(Exception ignored){}}}
 public void onSurfaceChanged(SurfaceHolder h,int f,int w,int he){super.onSurfaceChanged(h,f,w,he);handler.removeCallbacks(frame);if(visible)handler.post(frame);}
 public void onDestroy(){visible=false;handler.removeCallbacks(frame);for(Bitmap b:new Bitmap[]{sun,rain,snow,night,cloud})if(b!=null)b.recycle();super.onDestroy();}
 }
}
