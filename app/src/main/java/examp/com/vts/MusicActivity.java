package examp.com.vts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import cn.jzvd.JZVideoPlayerStandard;

public class MusicActivity extends AppCompatActivity {
    private JZVideoPlayerStandard jzVideoPlayerStandard;
    private ImageView             ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            String music = intent.getStringExtra("music");
//            music = "http://res.webftp.bbs.hnol.net/zhangyu/music/cd114/01.mp3";
            jzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.videoplayer);
            jzVideoPlayerStandard.setUp(music, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
            jzVideoPlayerStandard.startVideo();
        }

    }

    @Override
    public void onBackPressed() {
        if (jzVideoPlayerStandard.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jzVideoPlayerStandard.releaseAllVideos();
    }
}
