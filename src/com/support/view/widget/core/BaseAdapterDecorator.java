package com.support.view.widget.core;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

/**
 * Created by ivonhoe on 14-11-14.
 */
public abstract class BaseAdapterDecorator extends BaseAdapter {

    private final BaseAdapter mDecoratedBaseAdapter;

    protected ListViewWrapper mListViewWrapper;

    protected AnimationImpl mAnimationImpl;

    public BaseAdapterDecorator(BaseAdapter baseAdapter) {
        this.mDecoratedBaseAdapter = baseAdapter;
    }

    @Override
    public int getCount() {
        return mDecoratedBaseAdapter.getCount();
    }

    @Override
    public Object getItem(int position) {
        return mDecoratedBaseAdapter.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return mDecoratedBaseAdapter.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return mDecoratedBaseAdapter.getView(position, convertView, parent);
    }

    public void setListViewWrapper(ListViewWrapper listViewWrapper) {
        mListViewWrapper = listViewWrapper;
    }

    public void setAbsListView(AbsListView absListView){
        setListViewWrapper(new AbsListViewWrapper(absListView));
    }

    public void setAnimatorWrapper(AnimationImpl animationImpl) {
        if (animationImpl == null) {
            return;
        }
        this.mAnimationImpl = animationImpl;
    }


    @Override
    public boolean areAllItemsEnabled() {
        return mDecoratedBaseAdapter.areAllItemsEnabled();
    }

    @Override
    public View getDropDownView(final int position, final View convertView, final ViewGroup parent) {
        return mDecoratedBaseAdapter.getDropDownView(position, convertView, parent);
    }

    @Override
    public int getItemViewType(final int position) {
        return mDecoratedBaseAdapter.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return mDecoratedBaseAdapter.getViewTypeCount();
    }

    @Override
    public boolean hasStableIds() {
        return mDecoratedBaseAdapter.hasStableIds();
    }

    @Override
    public boolean isEmpty() {
        return mDecoratedBaseAdapter.isEmpty();
    }

    @Override
    public boolean isEnabled(final int position) {
        return mDecoratedBaseAdapter.isEnabled(position);
    }

    @Override
    public void notifyDataSetChanged() {
        if (!(mDecoratedBaseAdapter instanceof ArrayAdapter<?>)) {
            // fix #35 dirty trick !
            // leads to an infinite loop when trying because ArrayAdapter triggers notifyDataSetChanged itself
            // TODO: investigate
            mDecoratedBaseAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Helper function if you want to force notifyDataSetChanged()
     */
    @SuppressWarnings("UnusedDeclaration")
    public void notifyDataSetChanged(final boolean force) {
        if (force || !(mDecoratedBaseAdapter instanceof ArrayAdapter<?>)) {
            // leads to an infinite loop when trying because ArrayAdapter triggers notifyDataSetChanged itself
            // TODO: investigate
            mDecoratedBaseAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void notifyDataSetInvalidated() {
        mDecoratedBaseAdapter.notifyDataSetInvalidated();
    }

    @Override
    public void registerDataSetObserver(final DataSetObserver observer) {
        mDecoratedBaseAdapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(final DataSetObserver observer) {
        mDecoratedBaseAdapter.unregisterDataSetObserver(observer);
    }

}
