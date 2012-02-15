package com.jsolutionssp.patch.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.jsolutionssp.patch.R;

public class GalleryImageAdapter extends BaseAdapter {

	private int[] id = {R.drawable.theme_pink, R.drawable.theme_green, R.drawable.theme_blue};
	private Context context;
	private Object mGalleryItemBackground;

	public GalleryImageAdapter(Context context, Dialog dialog) {
		this.context = context;
		 TypedArray attr = context.obtainStyledAttributes(R.styleable.HelloGallery);
	        mGalleryItemBackground = attr.getResourceId(
	                R.styleable.HelloGallery_android_galleryItemBackground, 0);
	        attr.recycle();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return id.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ImageView image = new ImageView(context);
		image.setImageResource(id[position]);
		image.setBackgroundResource((Integer) mGalleryItemBackground);
		image.setLayoutParams(new 
				 Gallery.LayoutParams(Gallery.LayoutParams.WRAP_CONTENT, Gallery.LayoutParams.WRAP_CONTENT));
       
        image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		
		
		return image;
	}
}
