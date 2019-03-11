package com.plgf.animationdemo;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SurfaceActivity extends AppCompatActivity {

    private SurfaceAnimView surfaceAnimView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface);
        surfaceAnimView = (SurfaceAnimView) findViewById(R.id.surface);
        surfaceAnimView.start();
        button = (Button) findViewById(R.id.add);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SurfaceBean surfaceBean = new SurfaceBean(BitmapFactory.decodeResource(getResources(), R.drawable.zzlx2), surfaceAnimView);
                surfaceAnimView.addBean(surfaceBean);
            }
        });
    }
}
