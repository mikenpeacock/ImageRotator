package com.example.mike.imagemover;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by Mike on 3/11/2017.
 */

public class TouchAndDrag extends Activity implements View.OnTouchListener, View.OnClickListener{
    Button addImgButton;
    float x,y=0.0f;
    int touched =0;
    ArrayList<ImageView> images;
    ImageView selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.touch_and_drag);
        addImgButton = (Button) findViewById(R.id.addImgButton);
        addImgButton.setOnClickListener(this);
        images = new ArrayList<>();
        RelativeLayout r1 = (RelativeLayout) findViewById(R.id.rl);
        r1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //gesture detector to detect swipe.
               if(selectedImage!=null)
                   selectedImage.setBackgroundColor(Color.TRANSPARENT);
                return true;//always return true to consume event
            }
        });
    }

    public void UpdatedSelectedView(ImageView currentSelectedView, ImageView newView)
    {
        if(currentSelectedView!=null)
        {
            currentSelectedView.setBackgroundColor(Color.TRANSPARENT);
            newView.setBackgroundColor(Color.YELLOW);
            selectedImage = newView;
        }
        else
        {
            selectedImage = newView;
            selectedImage.setBackgroundColor(Color.YELLOW);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                ImageView newView = (ImageView)v;
                UpdatedSelectedView(selectedImage,newView);
                touched+=1;
                break;
            case MotionEvent.ACTION_MOVE:
                if(touched!=0)
                {
                    for(int i=0; i < images.size();i++)
                    {
                        ImageView iv = images.get(i);
                        if (v.getId() == iv.getId())
                        {
                            x=event.getRawX()-iv.getWidth()/2;
                            y=event.getRawY()-(iv.getHeight());
                            iv.setX(x);
                            iv.setY(y);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                touched-=1;
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        ImageView  iv = new ImageView(this);
        iv.setImageResource(R.drawable.ball);
        iv.setOnTouchListener(this);
        iv.setId(generateViewId());
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        rl.addView(iv);
        images.add(iv);
    }
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }
}
