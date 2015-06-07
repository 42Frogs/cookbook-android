package com.frogs42.cookbook.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.frogs42.cookbook.R;
import com.frogs42.cookbook.data.DataStore;
import com.frogs42.cookbook.data.DbAdapter;
import com.frogs42.cookbook.model.Recipe;
import com.frogs42.cookbook.utils.EventsManager;
import com.frogs42.cookbook.utils.Utils;

import java.util.ArrayList;

/**
 * Created by igor on 05.06.15.
 */
public class RecipesListAdapter extends BaseAdapter implements EventsManager.EventHandler {

    private Context mContext;
    private ArrayList<Recipe> mObjects;

    public RecipesListAdapter(Context context, ArrayList<Recipe> objects) {
        mContext = context;
        mObjects = objects;
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public Recipe getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public void handleEvent(String eventType, Object eventData) {
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView textView;
        ImageView icon;
        Button favouriteAction;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_list_recipe, null);

            ViewHolder holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.recipe);
            holder.icon = (ImageView) convertView.findViewById(R.id.recipe_icon);
            holder.favouriteAction = (Button) convertView.findViewById(R.id.recipe_favourite_text);
            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        final Recipe item = getItem(position);
        holder.textView.setText(item.getTitle());

        Bitmap iconBitmap = Utils.loadFromFile(item.getIcoPath());
        if (iconBitmap != null)
            holder.icon.setImageDrawable(new BitmapDrawable(mContext.getResources(), iconBitmap));

        if (item.isFavorite()) {
            holder.favouriteAction.setText(R.string.recipe_item_make_non_favourite);
            holder.favouriteAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataStore.makeNonFavourite(item);
                }
            });
        } else {
            holder.favouriteAction.setText(R.string.recipe_item_make_favourite);
            holder.favouriteAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataStore.makeFavourite(item);
                }
            });
        }

        return convertView;
    }
}
