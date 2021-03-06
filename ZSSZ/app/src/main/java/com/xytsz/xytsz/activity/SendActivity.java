package com.xytsz.xytsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;


import com.xytsz.xytsz.R;
import com.xytsz.xytsz.adapter.SendAdapter;
import com.xytsz.xytsz.bean.Review;
import com.xytsz.xytsz.global.GlobalContanstant;
import com.xytsz.xytsz.net.NetUrl;
import com.xytsz.xytsz.util.JsonUtil;
import com.xytsz.xytsz.util.SpUtils;
import com.xytsz.xytsz.util.ToastUtil;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by admin on 2017/2/17.
 * 派发界面
 */
public class SendActivity extends AppCompatActivity {

    private static final int ISSEND = 2003;
    public static final int FAIL = 500;
    private ListView mLv;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ISSEND:
                    mProgressBar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(), "没有已审核的数据");
                    break;
                case FAIL:
                    mProgressBar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(), "未获取数据,请稍后");
                    break;

            }
        }
    };
    private int personId;
    private int roleId;

    private ProgressBar mProgressBar;
    private SendAdapter adapter;
    private List<Review.ReviewRoad> list;
    private int size;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        initAcitionbar();
        personId = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        roleId = SpUtils.getInt(getApplicationContext(), GlobalContanstant.ROLE);
        initView();
        initData();

    }

    private void initView() {
        mLv = (ListView) findViewById(R.id.lv_send);
        mProgressBar = (ProgressBar) findViewById(R.id.review_progressbar);
    }

    private void initData() {

        mProgressBar.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {

                try {
                    //获取json
                    String reviewData = getReviewData(GlobalContanstant.GETSEND, personId);

                    if (reviewData != null) {

                        Review review = JsonUtil.jsonToBean(reviewData, Review.class);
                        list = review.getReviewRoadList();


                        if (list.size() == 0) {
                            Message message = Message.obtain();
                            message.what = ISSEND;
                            handler.sendMessage(message);

                        } else {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter = new SendAdapter(list);
                                    mLv.setAdapter(adapter);
                                    mProgressBar.setVisibility(View.GONE);
                                    mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent intent = new Intent(SendActivity.this, SendRoadActivity.class);
                                            intent.putExtra("position", position);
                                            startActivityForResult(intent, 600);
                                        }
                                    });
                                }

                            });

                        }
                    }

                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = FAIL;
                    handler.sendMessage(message);

                }
            }
        }.start();


    }

    public static String getReviewData(int phaseIndication, int personId) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getTaskList);
        soapObject.addProperty("PhaseIndication", phaseIndication);
        soapObject.addProperty("personid", personId);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);
        envelope.bodyOut = soapObject;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getTasklist_SOAP_ACTION, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String json = object.getProperty(0).toString();

        Log.i("json", json);
        return json;
    }


    private int position;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 600) {
            switch (resultCode) {
                //多选的删除
                case 202:
                    position = data.getIntExtra("passposition", -1);
                    List<Review.ReviewRoad.ReviewRoadDetail> deletlist =
                            (List<Review.ReviewRoad.ReviewRoadDetail>) data.getSerializableExtra("deletlist");

                    for (Review.ReviewRoad.ReviewRoadDetail reviewRoadDetail : deletlist) {
                        String taskNumber = reviewRoadDetail.getTaskNumber();
                        Iterator<Review.ReviewRoad.ReviewRoadDetail> iterator =
                                list.get(position).getList().iterator();

                        while (iterator.hasNext()){
                            Review.ReviewRoad.ReviewRoadDetail next = iterator.next();
                            if (next.getTaskNumber().equals(taskNumber)){
                                iterator.remove();
                            }
                        }

                    }


                    size = list.get(position).getList().size();
                    if (size == 0) {
                        list.remove(position);
                    }
                    adapter.updateAdapter(list);
                    //adapter.notifyDataSetChanged();
                    break;
                case 201:
                    position = data.getIntExtra("failposition", -1);
                    List<Review.ReviewRoad.ReviewRoadDetail> deletbacklist =
                            (List<Review.ReviewRoad.ReviewRoadDetail>) data.getSerializableExtra("deletbacklist");

                    for (Review.ReviewRoad.ReviewRoadDetail reviewRoadDetail : deletbacklist) {
                        String taskNumber = reviewRoadDetail.getTaskNumber();
                        Iterator<Review.ReviewRoad.ReviewRoadDetail> iterator =
                                list.get(position).getList().iterator();

                        while (iterator.hasNext()){
                            Review.ReviewRoad.ReviewRoadDetail next = iterator.next();
                            if (next.getTaskNumber().equals(taskNumber)){
                                iterator.remove();
                            }
                        }

                    }

                    size = list.get(position).getList().size();
                    if (size == 0) {
                        list.remove(position);
                    }
                    adapter.updateAdapter(list);
                    //adapter.notifyDataSetChanged();
                    break;
                //单选的删除
                case 505:
                    //点击的条目
                    position = data.getIntExtra("position", -1);
                    //删除的条目
                    int passposition = data.getIntExtra("passposition", -1);
                    list.get(position).getList().remove(passposition);
                    size = list.get(position).getList().size();
                    if (size == 0) {
                        list.remove(position);
                    }
                    adapter.notifyDataSetChanged();
                    break;

                case 605:
                    position = data.getIntExtra("position", -1);
                    int failposition = data.getIntExtra("failposition", -1);
                    list.get(position).getList().remove(failposition);
                    size = list.get(position).getList().size();
                    if (size == 0) {
                        list.remove(position);
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }
        }

    }


    private void initAcitionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.send);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
