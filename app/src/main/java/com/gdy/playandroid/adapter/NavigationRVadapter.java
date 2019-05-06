package com.gdy.playandroid.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gdy.playandroid.R;
import com.gdy.playandroid.constants.Constant;
import com.gdy.playandroid.mvp.bean.Article;
import com.gdy.playandroid.mvp.bean.Navigation;
import com.gdy.playandroid.ui.activity.WebContentActivity;
import com.gdy.playandroid.utils.GlobalUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

public class NavigationRVadapter extends BaseQuickAdapter<Navigation,BaseViewHolder> {

    public NavigationRVadapter(@Nullable List<Navigation> data) {
        super(R.layout.navigation_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final Navigation item) {
        helper.setText(R.id.titleTV,item.getName());
        TagFlowLayout tagFlowLayout = helper.getView(R.id.flowlayout);
        tagFlowLayout.setAdapter(new TagAdapter<Article>(item.getArticles()) {
            @Override
            public View getView(FlowLayout parent, int position, Article article) {
                TextView textView = new TextView(mContext);
                textView.setText(article.getTitle());
                textView.setTextColor(GlobalUtils.getColor(R.color.color_head_blue));
                textView.setPadding(0,0,GlobalUtils.dp2px(10),GlobalUtils.dp2px(26));
                return textView;
            }
        });
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Intent intent = new Intent(mContext, WebContentActivity.class);
                intent.putExtra(Constant.CONTENT_URL_KEY, item.getArticles().get(position).getLink());
                intent.putExtra(Constant.CONTENT_TITLE_KEY,item.getArticles().get(position).getTitle());
                mContext.startActivity(intent);
                return false;
            }
        });
    }
}
