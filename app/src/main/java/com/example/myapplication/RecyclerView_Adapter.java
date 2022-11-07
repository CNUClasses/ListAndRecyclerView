package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerView_Adapter extends RecyclerView.Adapter {
    private static final int DEFAULT_MAX_ROWS = 100;
    private static final int UNINITIALIZED = -1;
    private final LayoutInflater li;
    private final Context ctx;
    private int maxRows;

    private class GetNumber extends Thread {
        //holds a reference to the hosting activity
        //notice that we cannot easily use a viewmodel
        //since each RowViewHolder has an implicit reference to
        //the parent activity. (From the inflator)
        private final MainActivity act;
        //ref to a viewholder, this could change if
        //RowViewHolder myVH is recycled and reused!!!!!!!!!
        private RowViewHolder myVh;
        //since myVH may be recycled and reused
        //we have to verify that the result we are returning
        //is still what the viewholder wants
        private int original_number;
        private int result= UNINITIALIZED;

        public GetNumber(RowViewHolder myVh, MainActivity act) {
            //hold on to a reference to this viewholder
            //note that its contents (specifically iv) may change
            //iff the viewholder is recycled
            this.myVh = myVh;
            //make a copy to compare later, once we have the image
            this.original_number = myVh.numb;

            //hold on to the activity
            this.act=act;
        }
        @Override
        public void run() {
            super.run();

            //just sleep for a bit to simulate long running downloaded
            //but could just as easily make a network call
            try {
                Thread.sleep(2000); //sleep for 2 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //create result (does this need protection? Not as written)
            result=original_number*original_number;
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //got a result, if the following are NOT equal
                    // then the view has been recycled and is being used by another
                    // number DO NOT MODIFY
                    if (myVh.numb == original_number){
                        //still valid
                        //set the result on the main thread
                        myVh.iv.setImageResource(R.drawable.ok);
                        myVh.tvInfo.setText(Integer.toString(myVh.numb) + " squared =");
                        myVh.tvResult.setText(Integer.toString(result));
                    }
                    else{
                        myVh.iv.setImageResource(R.drawable.notneeded);
                        myVh.tvInfo.setText("DANG! work wasted");
                        myVh.tvResult.setText("");
                    }
                }
            });
        }
    }
    class RowViewHolder extends RecyclerView.ViewHolder {
        int numb = UNINITIALIZED;
        ImageView iv;
        TextView tvInfo;
        TextView tvResult;
        //after construction, above member variables hold references
        //to widgets IN THIS PARTICULAR ROW!
        public RowViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInfo = (TextView)itemView.findViewById(R.id.tvInfo );
            tvResult = (TextView)itemView.findViewById(R.id.tvResult );
            iv=(ImageView)itemView.findViewById(R.id.imageView1);
        }
    }

    //one arg constructor uses DEFAULT_MAX_ROWS
    public RecyclerView_Adapter(Context ctx) {
        this(ctx,DEFAULT_MAX_ROWS);
    }
    //two arg constructor in case user wants to define their own maxrows
    public RecyclerView_Adapter(Context ctx, int maxRows) {
        this.ctx = ctx;
        li = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.maxRows=maxRows;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //call this when we need to create a brand new PagerViewHolder
        View view = li.inflate(R.layout.row_layout2, parent, false);
        return new RowViewHolder(view);   //the new one
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //passing in an existing instance, reuse the internal resources
        //pass our data to our ViewHolder.
        RowViewHolder viewHolder = (RowViewHolder) holder;
        viewHolder.numb= holder.getAdapterPosition();

        //initialize the UI
        viewHolder.iv.setImageResource(R.drawable.unknown);
        viewHolder.tvInfo.setText("Hold on a sec...");
        viewHolder.tvResult.setText("");

        //launch a thread to 'retreive' the image (slow to get data)
        GetNumber myTask = new GetNumber(viewHolder,(MainActivity) ctx);
        myTask.start();
    }

    @Override
    public int getItemCount() {
        return maxRows;
    }
}
