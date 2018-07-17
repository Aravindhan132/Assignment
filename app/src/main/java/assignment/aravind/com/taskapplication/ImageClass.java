package assignment.aravind.com.taskapplication;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by aravindhan Software on 07/17/18
 */

public class ImageClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_image_class);
        Bitmap bitmap = this.getIntent().getParcelableExtra("DATA");
        ImageView viewBitmap = (ImageView) findViewById(R.id.bitmapview);
        viewBitmap.setImageBitmap(bitmap);
    }
}
