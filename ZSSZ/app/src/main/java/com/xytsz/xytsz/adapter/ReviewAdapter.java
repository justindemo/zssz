package com.xytsz.xytsz.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xytsz.xytsz.R;
import com.xytsz.xytsz.bean.Review;
import com.xytsz.xytsz.global.Data;
import com.xytsz.xytsz.global.GlobalContanstant;
import com.xytsz.xytsz.util.SpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/1/11.
 *
 */
public class ReviewAdapter extends BaseAdapter {


    private List<Review.ReviewRoad> reviewRoads;
    private int personId;


    public ReviewAdapter(List<Review.ReviewRoad> reviewRoads, int personId) {
        this.reviewRoads = reviewRoads;
        this.personId = personId;



    }


    public void updateAdapter(List<Review.ReviewRoad> reviewRoads){
        this.reviewRoads = reviewRoads;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return reviewRoads.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_review, null);
            holder.unreadmsg = (TextView) convertView.findViewById(R.id.tv_un_read_msg_count);
            holder.roadname = (TextView) convertView.findViewById(R.id.tv_road_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Review.ReviewRoad reviews = reviewRoads.get(position);
        int size = reviews.getList().size();
        if (size == 0) {
            holder.unreadmsg.setVisibility(View.INVISIBLE);
        } else {
            holder.unreadmsg.setVisibility(View.VISIBLE);
            holder.unreadmsg.setText(size + "");
        }
        holder.roadname.setText(reviews.getStreetName());


        return convertView;
    }

    static class ViewHolder {
        public TextView unreadmsg;
        public TextView roadname;

    }
}
