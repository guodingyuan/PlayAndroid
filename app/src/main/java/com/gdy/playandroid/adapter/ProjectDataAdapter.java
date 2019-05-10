package com.gdy.playandroid.adapter;


import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gdy.playandroid.R;
import com.gdy.playandroid.mvp.bean.Article;
import com.gdy.playandroid.utils.GlobalUtils;
import com.gdy.playandroid.utils.glide.GlideApp;

import java.util.List;

public class ProjectDataAdapter extends BaseQuickAdapter<Article,BaseViewHolder> {

    private final int imgSize;

    public ProjectDataAdapter(@Nullable List<Article> data) {
        super(R.layout.project_data_article_item,data);
        imgSize = GlobalUtils.dp2px(140);
    }

    @Override
    protected void convert(BaseViewHolder helper, Article item) {
        helper.setText(R.id.titleTV,Html.fromHtml(item.getTitle()))
                .setText(R.id.authorTV,item.getAuthor())
                .setText(R.id.dateTV,item.getNiceDate())
                .setText(R.id.descTV,item.getDesc());
        String envelopePic = item.getEnvelopePic();
        ImageView picIV = helper.getView(R.id.picIV);
        GlideApp.with(mContext).load(envelopePic).placeholder(R.color.gray_bg).thumbnail(0.25f).override(imgSize).centerCrop().into(picIV);
    }
}
