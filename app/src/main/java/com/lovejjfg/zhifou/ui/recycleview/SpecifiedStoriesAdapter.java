package com.lovejjfg.zhifou.ui.recycleview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.lovejjfg.zhifou.R;
import com.lovejjfg.zhifou.data.model.DailyStories;
import com.lovejjfg.zhifou.data.model.Story;
import com.lovejjfg.zhifou.ui.recycleview.holder.StoryViewHolder;
import com.lovejjfg.zhifou.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lovejjfg on 2016/2/21.
 */
public class SpecifiedStoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = SpecifiedStoriesAdapter.class.getSimpleName();
    protected List<Item> mItems;
    protected List<Item> mTmpItem;
    private OnItemClickListener listener;
    private boolean isLoading;


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void isLoadingMore(boolean loading) {
        isLoading = loading;
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public class Type {
        public static final int TYPE_HEADER = 0;
        public static final int TYPE_DATE = 1;
        public static final int TYPE_STORY = 2;
        public static final int TYPE_BOTTOM = 3;
    }

    public SpecifiedStoriesAdapter() {
        mItems = new ArrayList<>();
        mTmpItem = new ArrayList<>();
    }

    public void setList(DailyStories dailyStories) {
        mItems.clear();
        appendList(dailyStories);
    }

    public void appendList(DailyStories dailyStories) {
        List<Story> stories = dailyStories.getStories();
        for (int i = 0, num = stories.size(); i < num; i++) {
            Item storyItem = new Item();
            storyItem.setType(Type.TYPE_STORY);
            storyItem.setStory(stories.get(i));
            mItems.add(storyItem);
        }
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView;
        itemView = UIUtils.inflate(R.layout.recycler_item_story, parent);
        return new StoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        Item item = null;
        if (null != mItems && mItems.size() > 0 && position < mItems.size()) {
            item = mItems.get(position);
        }
        assert item != null;
        ((StoryViewHolder) holder).bindStoryView(item.getStory());
        final Item finalItem = item;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {

                    listener.onItemClick(holder.itemView, ((StoryViewHolder) holder).getImage(), Integer.valueOf(finalItem.getStory().getId()));
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public Item getItem(int position) {
        return mItems.get(position);
    }


    public static class Item {
        private int type;
        private String date;
        private Story story;
        private List<Story> stories;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Story getStory() {
            return story;
        }

        public void setStory(Story story) {
            this.story = story;
        }

        public List<Story> getStories() {
            return stories;
        }

        public void setStories(List<Story> stories) {
            this.stories = stories;
        }
    }
}
