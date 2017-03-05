package com.example.progressbar;

import com.cultraview.view.HorizontalprograssbarAndprograss;
import com.cultraview.view.RoundProgressBarWithProgress;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {
    
	private static final int  MSG_UPDATE = 0X001;
	private HorizontalprograssbarAndprograss mHprogress;
	
	private RoundProgressBarWithProgress mRprogress;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mHprogress = (HorizontalprograssbarAndprograss) findViewById(R.id.id_progress01);
        mRprogress = (RoundProgressBarWithProgress) findViewById(R.id.id_progress02);
        mHandle.sendEmptyMessage(MSG_UPDATE);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private Handler mHandle = new Handler(){
    	public void handleMessage(android.os.Message msg){
    		int progress = mHprogress.getProgress();
    		mHprogress.setProgress(++progress);
    		mRprogress.setProgress(++progress);
    		if(progress >= 100){
    			mHandle.removeMessages(MSG_UPDATE);
    		}else{
    			mHandle.sendEmptyMessageDelayed(MSG_UPDATE,500);
    		}
    	};
    };
    
}
