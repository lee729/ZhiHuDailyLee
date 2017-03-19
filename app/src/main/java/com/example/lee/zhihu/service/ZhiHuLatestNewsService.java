package com.example.lee.zhihu.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.lee.zhihu.Modle.ZhiHuNewsItem;
import com.example.lee.zhihu.R;
import com.example.lee.zhihu.util.ZhiHuConnect;
import com.example.lee.zhihu.view.ZhiHuListActivity;

import java.util.List;

/**
 * Created by lee on 2017/3/17.
 * service，用于后台获取最新news。
 */
public class ZhiHuLatestNewsService extends IntentService{
    private static final String TAG = "ZhiHuLatestNewsService";
    private static final String EXTRA_OLD_NEWS_ID = "com.example.lee.zhihu.service.EXTRA_OLD_NEWS_ID";

    private static final long INTEVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES;

    private String mOldNewsId;

    /**
     * 获取该service的intent，用于启动本service
     * */
    public static Intent newIntent(Context context,String oldNewsId){
        Intent intent = new Intent(context,ZhiHuLatestNewsService.class);
        intent.putExtra(EXTRA_OLD_NEWS_ID, oldNewsId);

        return intent;
    }
    /**
     * 默认构造函数
     * */
    public ZhiHuLatestNewsService(){
        super(TAG);
    }
    /**
     * 该方法是服务在后台线程调用的方法，执行传过来的intent命令。
     * */
    @Override
    protected void onHandleIntent(Intent intent) {
        mOldNewsId = intent.getStringExtra(EXTRA_OLD_NEWS_ID);
        //如果网络不可用，该方法不继续
        if (!ZhiHuConnect.isNetwordAvailableAndConnected(this)){
            return;
        }else {
            Log.i(TAG,"get an intent ::" +intent);
            //联网获取最新的lsit，因为本方法就是在子线程中运行，所以不用写asynctask
            List<ZhiHuNewsItem> latestList = new ZhiHuConnect().sendUrl(this,
                    ZhiHuConnect.ZhiHuUrl.URL_NEWS_LATEST,
                    ZhiHuConnect.KIND_NEWS_LATEST,
                    null);
            ZhiHuNewsItem latestItem = latestList.get(0);
            //判断oldnews和新的news，如果相同，不显示通知
            if (mOldNewsId.equals(latestItem.getId())){
                Log.i(TAG,"已经是最新消息了");
            }else{
                //最新消息用notification通知用户
                //获取启动Zhihulistactivity的intent
                Intent zhihuListActivityIntent = ZhiHuListActivity.newIntent(this);
                PendingIntent pendingIntent = PendingIntent.getActivity(this,0,zhihuListActivityIntent,0);//启动activity
                //创建通知消息
                Notification notification = new NotificationCompat.Builder(this)
                        .setTicker(latestItem.getTitle())//一闪而过的内容,item的标题
                        .setSmallIcon(android.R.drawable.ic_menu_report_image)//通知的icon
                        .setContentTitle(getResources().getString(R.string.get_a_new_news))//通知的标题，
                        .setContentText(latestItem.getTitle())//通知的正文内容，item的正文标题
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build();
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
                notificationManagerCompat.notify(0,notification);
            }
        }
    }
    /**
     * 设置定时器,启动本service
     * */
    public static void setServiceAlarm(Context context, boolean isOn, String oldNewsId){
        Intent intent = ZhiHuLatestNewsService.newIntent(context,oldNewsId);
        PendingIntent pendingIntent = PendingIntent.getService(
                context,
                0,
                intent,
                0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (isOn){
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(),
                    INTEVAL,
                    pendingIntent);

        }else {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }
    /**
     * 判断定时器是否开着
     * */
    public static boolean isServiceAlarmOn(Context context,String oldNewsId){
        Intent intent = ZhiHuLatestNewsService.newIntent(context,oldNewsId);
        //这个flag的意思是，如果pendingintent不存在，那么返回null，而不是创建pengdingintent
        PendingIntent pendingIntent = PendingIntent.getService(context,0,intent,PendingIntent.FLAG_NO_CREATE);
        return pendingIntent!=null;
    }
}
