package com.lovejjfg.zhifou.presenters;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.lovejjfg.zhifou.data.BaseDataManager;
import com.lovejjfg.zhifou.data.model.DailyStories;
import com.lovejjfg.zhifou.data.model.Story;
import com.lovejjfg.zhifou.util.WebUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhangjun on 2016-03-01.
 */
public class DetailPresenterImpl implements DetailPresenter {

    private boolean isLoading;
    View view;
    Activity activity;
//    private final ExecutorService service;

    public DetailPresenterImpl(View view) {
        this.view = view;
        activity = (Activity) view;
//        service = Executors.newCachedThreadPool();

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoading(int id) {
        if (!isLoading) {
            BaseDataManager.getDailyApiService().getStoryDetail(String.valueOf(id))
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            view.isLoading(true);
                            isLoading = true;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Story>() {
                        @Override
                        public void call(Story story) {
                            isLoading = false;
                            if (!TextUtils.isEmpty(story.getImage())) {
                                view.onBindImage(story.getImage());
                            }
                            if (!TextUtils.isEmpty(story.getBody())) {
                                String data = WebUtils.BuildHtmlWithCss(story.getBody(), story.getCssList(), false);
                                view.onBindWebView(data);
                            }
                            if (!TextUtils.isEmpty(story.getTitle())) {
                                view.onBindTittle(story.getTitle());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            isLoading = false;
                        }
                    });

        }


    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        Log.i("TAG", "onDestroy: xxxxxxx");
//        service.shutdownNow();
        activity = null;
        view = null;

    }

    @Override
    public void subscribe(Subscription subscriber) {

    }

    @Override
    public void unSubscribe() {

    }
}
