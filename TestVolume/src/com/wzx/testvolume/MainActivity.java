package com.wzx.testvolume;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.example.testvolume.R;


public class MainActivity extends Activity implements OnSeekBarChangeListener {
Context mContext;
//    ImageView mute;
    NumSeekBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.volume_panel_item_philips);
//        setContentView(R.layout.activity_main);
        bar = (NumSeekBar)findViewById(R.id.seekbar);
//        mute = (ImageView)findViewById(R.id.mute_icon);
        mContext = this;
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setMasterMute(false);
//        
//        
//        Intent i = new Intent();
//        i.setClassName("com.philips.android.wizard", "com.philips.android.wizard.MainWizardActivity");
//        startActivity(i);
//        finish();
        
    }

    boolean mute = true;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_1){
            bar.setProgress(bar.getSelectProgress()+1); 
//            mute.setX(bar.getProgressButtonX() + bar.getX()-4);
            return true;
        }else if(keyCode == KeyEvent.KEYCODE_2){
            bar.setProgress(bar.getSelectProgress()-1); 
//            mute.setX(bar.getProgressButtonX() + bar.getX()-12);
            return true;
        }else if(keyCode == KeyEvent.KEYCODE_3){
            bar.setMute(mute);
            mute = !mute;
            return true;
        }
        
        
        return super.onKeyDown(keyCode, event);
    }
    
    private void setViewAnimation(View v, int state) {

        if (state == View.VISIBLE) {
            if (v.getVisibility() != View.VISIBLE) {
                v.setVisibility(View.VISIBLE);
                TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                mHiddenAction.setDuration(200);
                v.setAnimation(mHiddenAction);
            }
        } else if (state == View.GONE) {
            if (v.getVisibility() != View.GONE) {
                TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
                mHiddenAction.setDuration(200);
                v.setAnimation(mHiddenAction);
                v.setVisibility(View.GONE);
            }
        }
    }
    
    
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
        
    }

}
