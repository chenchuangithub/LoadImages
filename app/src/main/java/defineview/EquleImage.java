package defineview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by android_chen on 2016/3/27.
 */
public class EquleImage extends ImageView{

    public EquleImage(Context context) {
        super(context);
    }

    public EquleImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EquleImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
