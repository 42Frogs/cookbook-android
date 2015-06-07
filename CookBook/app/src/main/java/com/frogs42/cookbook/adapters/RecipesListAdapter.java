package com.frogs42.cookbook.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.frogs42.cookbook.R;
import com.frogs42.cookbook.data.DbAdapter;
import com.frogs42.cookbook.model.Recipe;
import com.frogs42.cookbook.utils.EventsManager;

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
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_list_recipe, null);

            ViewHolder holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.recipe);
            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.textView.setText(getItem(position).getTitle());

        return convertView;
    }
}
