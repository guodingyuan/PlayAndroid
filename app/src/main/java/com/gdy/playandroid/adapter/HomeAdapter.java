package com.gdy.playandroid.adapter;


import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gdy.playandroid.R;
import com.gdy.playandroid.mvp.bean.Article;
import com.gdy.playandroid.mvp.bean.Tag;
import com.gdy.playandroid.utils.glide.GlideApp;

import java.util.List;

public class HomeAdapter extends BaseQuickAdapter<Article,BaseViewHolder> {

    public HomeAdapter(@Nullable List<Article> data) {
        super(R.layout.home_article_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Article item) {
        helper.setText(R.id.titleTV,item.getTitle())
                .setText(R.id.authorTV,item.getAuthor())
                .setText(R.id.chapterNameTV,item.getSuperChapterName()+" | "+item.getChapterName())
                .setText(R.id.dateTV,item.getNiceDate())
                .setGone(R.id.topTV,item.isTop());
        List<Tag> tags = item.getTags();
        TextView tagTV = helper.getView(R.id.tagTV);
        if(tags!=null && tags.size()>0){
            tagTV.setVisibility(View.VISIBLE);
            tagTV.setText(tags.get(0).getName());
        }else {
            tagTV.setVisibility(View.GONE);
        }
        String envelopePic = item.getEnvelopePic();
        ImageView picIV = helper.getView(R.id.picIV);
        if(!TextUtils.isEmpty(envelopePic)){
            picIV.setVisibility(View.VISIBLE);
            GlideApp.with(mContext).load(envelopePic).centerCrop().into(picIV);
        }else {
            picIV.setVisibility(View.GONE);
        }

    }
}
