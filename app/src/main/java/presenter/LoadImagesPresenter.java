package presenter;

import android.os.Handler;

import com.example.android_chen.loadimages.IShowImagesView;

import java.util.ArrayList;

import model.GetImageResource;
import model.IGetImageResource;

/**
 * Created by android_chen on 2016/3/28.
 */
public class LoadImagesPresenter {
    private Handler mHandler = new Handler();
    private IGetImageResource mGetImageResource;
    private IShowImagesView mShowImagesView;

    public LoadImagesPresenter(IShowImagesView showImagesView) {
        this.mGetImageResource = new GetImageResource();
        this.mShowImagesView = showImagesView;
    }

    public void firstGetImageResource(String url) {
        mGetImageResource.getImageUrlsFromInternet(url, new GetImageResource.OnLoadListener() {
            @Override
            public void onLoadSuccess(final ArrayList<String> urls) {//设置为final可能有问题
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mShowImagesView.toShowLayout(urls);
                        mShowImagesView.hideProgressbar();
                    }
                });
            }

            @Override
            public void onLoadFail() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mShowImagesView.showLoadFail();
                    }
                });
            }
        });
    }

    public void loadMoreFromNetwork() {
        mGetImageResource.loadMoreUrlsFromInternet(new GetImageResource.OnLoadListener() {
            @Override
            public void onLoadSuccess(ArrayList<String> urls) {
                mShowImagesView.getAdapter().notifyDataSetChanged();//可能有问题
                mShowImagesView.getSwipeRefreshLayout().setRefreshing(false);
            }

            @Override
            public void onLoadFail() {
                mShowImagesView.showLoadFail();
            }
        });
    }

}
