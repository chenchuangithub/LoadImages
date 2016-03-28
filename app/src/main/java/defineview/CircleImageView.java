package defineview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.android_chen.loadimages.R;

/**
 * Created by android_chen on 2016/3/28.
 */
public class CircleImageView extends ImageView{
    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;//缩放类型
    private static final Bitmap.Config CONFIG = Bitmap.Config.ARGB_8888;//Bitmap配置
    private static final int COLORDRAWABLE_DIMENSION = 2;
    private static final int DEFAULT_BORDER_WIDTH = 0;//默认的外边框的高度
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private static final int DEFAULT_FILL_COLOR = Color.TRANSPARENT;
    private static final boolean DEFAULT_BORDER_OVERLAY = false;

    private RectF mDrawableRect = new RectF();
    private RectF mBorderRect = new RectF();

    private Matrix mShaderMatrix = new Matrix();
    private Paint mBitmapPaint = new Paint();
    private Paint mBorderPaint = new Paint();
    private Paint mFillPaint = new Paint();

    private int mBorderColor = DEFAULT_BORDER_COLOR;
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;
    private int mFillColor = DEFAULT_FILL_COLOR;

    private Bitmap mBitmap;
    private BitmapShader mBitmapShader;
    private int mBitmapWidth;
    private int mBitmapHeight;

    private float mDrawableRadius;
    private float mBorderRadius;

    private ColorFilter mColorFilter;

    private boolean mReady;
    private boolean mSetupPending;
    private boolean mBorderOverlay;



    public CircleImageView(Context context) {
        super(context);
        init();
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
        init();
    }


    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView,defStyleAttr,0);
        mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_civ_border_width,DEFAULT_BORDER_WIDTH);
        mBorderColor = a.getColor(R.styleable.CircleImageView_civ_border_color,DEFAULT_BORDER_COLOR);
        mBorderOverlay = a.getBoolean(R.styleable.CircleImageView_civ_border_overlay,DEFAULT_BORDER_OVERLAY);
        mFillColor = a.getColor(R.styleable.CircleImageView_civ_fill_color,DEFAULT_FILL_COLOR);
        a.recycle();
        init();
    }

    private void init() {
      super.setScaleType(SCALE_TYPE);
        mReady = true;
        if(mSetupPending){
            setUp();
            mSetupPending = false;
        }
    }

    private void setUp() {
        if(!mReady){
            mSetupPending = true;
            return;
        }
        if(getWidth() == 0 || getHeight() == 0){
            return;
        }
        if(mBitmap == null){
            invalidate();
            return;
        }
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);

        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        mBorderPaint.setColor(mBorderColor);

        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setAntiAlias(true);
        mFillPaint.setColor(mFillColor);

        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();
        mBorderRect.set(0,0,mBitmapWidth,mBitmapHeight);
        mBorderRadius =  Math.min((mBorderRect.height() - mBorderWidth)/2.0f,(mBorderRect.width() - mBorderWidth)/2.0f);
        mDrawableRect.set(mBorderRect);
        if(!mBorderOverlay){
            mDrawableRect.inset(mBorderWidth,mBorderWidth);
        }
        mDrawableRadius = Math.min(mDrawableRect.height()/2.0f,mDrawableRect.width()/2.0f);
        updateShaderMatrix();
        invalidate();
    }

    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;
        mShaderMatrix.set(null);
        if(mBitmapWidth/mDrawableRect.width() > mBitmapHeight/mDrawableRect.height()){
            scale = mDrawableRect.height()/mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        }else {
            scale = mDrawableRect.width()/mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }
        mShaderMatrix.setScale(scale,scale);
        mShaderMatrix.postTranslate((int)(dx + 0.5f) + mDrawableRect.left,(int)(dy + 0.5f) + mDrawableRect.top);
        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }


    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        if(adjustViewBounds){
            throw new IllegalArgumentException("adjustViewBounds not support");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mBitmap == null){
            return;
        }
        if(mFillColor != Color.TRANSPARENT){
            canvas.drawCircle(getWidth()/2.0f,getHeight()/2.0f,mDrawableRadius,mFillPaint);
        }
        canvas.drawCircle(getWidth()/2.0f,getHeight()/2.0f,mDrawableRadius,mBitmapPaint);
        if(mBorderWidth != 0){
            canvas.drawCircle(getWidth()/2.0f,getHeight()/2.0f,mBorderRadius,mBorderPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setUp();
    }
    public int getBorderColor(){
        return mBorderColor;
    }
    public void setBorderColor(@ColorInt int borderColor){
        if(borderColor == mBorderColor)return;
        mBorderColor = borderColor;
        mBorderPaint.setColor(mBorderColor);
        invalidate();
    }
    public void setBorderColorRes(@ColorRes int borderColorRes){
        setBorderColor(getContext().getResources().getColor(borderColorRes));
    }
    public int getFillCorlor(){
        return mFillColor;
    }
    public void setFillCorlor(@ColorInt int fillColor){
        if(fillColor == mFillColor)return;
        mFillColor = fillColor;
        mFillPaint.setColor(mFillColor);
        invalidate();
    }
    public void setFillColorRes(@ColorRes int fillColorRes){
        setFillCorlor(getContext().getResources().getColor(fillColorRes));
    }
    public int getBorderWidth(){
        return mBorderWidth;
    }
    public void setBorderWidth(int borderWidth){
        if(borderWidth == mBorderWidth)return;
        mBorderWidth = borderWidth;
        setUp();
    }
    public boolean isBorderOverlay(){
        return mBorderOverlay;
    }
    public void setBorderOverlay(boolean borderOverlay){
        if(borderOverlay = mBorderOverlay)return;
        mBorderOverlay = borderOverlay;
        setUp();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
        setUp();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(drawable);
        setUp();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setUp();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        mBitmap = uri == null ? getBitmapFromDrawable(getDrawable()) : null;
        setUp();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if(cf == mColorFilter)return;
        mColorFilter = cf;
        mBitmapPaint.setColorFilter(mColorFilter);
        invalidate();
    }


    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if(drawable == null){
            return null;
        }
        if(drawable instanceof BitmapDrawable){
            return ((BitmapDrawable)drawable).getBitmap();
        }
        try{
            Bitmap bitmap;
            if(drawable instanceof ColorDrawable){
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION,COLORDRAWABLE_DIMENSION,CONFIG);
            }else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),CONFIG);
            }
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
