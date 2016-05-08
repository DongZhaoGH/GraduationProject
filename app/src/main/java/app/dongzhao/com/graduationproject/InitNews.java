package app.dongzhao.com.graduationproject;

import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import app.dongzhao.com.graduationproject.activity.MainActivity;
import app.dongzhao.com.graduationproject.domain.GlobalConstants;
import app.dongzhao.com.graduationproject.domain.NewsMenu;
import app.dongzhao.com.graduationproject.utils.CacheUtils;

/**
 * 初始化新闻内容数据
 */
public class InitNews {
    private MainActivity mActivity;
    private NewsData newsData;

    //定义接口
    public interface NewsData {
        void sendNewsData(NewsMenu news);
    }

    //接口的实现方法
    public void getNewsData(NewsData newsData) {
        this.newsData = newsData;
    }


    public void getDataFromServer(final MainActivity mActivity) {
        this.mActivity = mActivity;
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, GlobalConstants.CATEGORY_URL,
                new RequestCallBack<String>() {

                    private NewsMenu newsMenu;

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        // 请求成功,获取服务器返回json数据
                        String result = responseInfo.result;

                        // 先判断有没有缓存,如果有的话,就加载缓存
                        String cache = CacheUtils.getCache(GlobalConstants.CATEGORY_URL,
                                mActivity);
                        Gson gson = new Gson();
                        if (!TextUtils.isEmpty(cache)) {

                            newsMenu = gson.fromJson(cache, NewsMenu.class);
                        } else {

                            newsMenu = gson.fromJson(result, NewsMenu.class);
                        }

                        //回调
                        if (newsData != null) {
                            newsData.sendNewsData(newsMenu);
                        }

                        // 写缓存
                        CacheUtils.setCache(GlobalConstants.CATEGORY_URL,
                                result, mActivity);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        // 请求失败
                        error.printStackTrace();
                        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT)
                                .show();
                    }

                });
    }

}
