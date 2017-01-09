package com.org.pawpal.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.org.pawpal.R;
import com.org.pawpal.model.UserImages;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.List;

/**
 * Created by hp-pc on 02-01-2017.
 */

public class PalProfileImagesAdapter extends RecyclerView.Adapter<PalProfileImagesAdapter.MyViewHolder> {
    private List<UserImages> userImages;
    private Context context;

    public PalProfileImagesAdapter(List<UserImages> userImages, Context context)
    {
        this.context = context;
        this.userImages = userImages;
    }
    @Override
    public PalProfileImagesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_pics_list_row, parent, false);
        return new PalProfileImagesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PalProfileImagesAdapter.MyViewHolder holder, int position) {
        UserImages userImage = userImages.get(position);
    /*    URL url = null;
        try {
            new DownloadImageTask(holder.imageView)
                    .execute("http://blogs.nd.edu/oblation/files/2013/09/BreakingBad.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        Picasso.with(context).setLoggingEnabled(true);
        Picasso.with(context).cancelRequest(holder.imageView);
        Picasso.with(context).load(userImage.getUrl()).fit().centerCrop().placeholder(R.mipmap.img_default).into(holder.imageView);
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    public int getItemCount() {
        return userImages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.iv_image);
        }
    }
}
