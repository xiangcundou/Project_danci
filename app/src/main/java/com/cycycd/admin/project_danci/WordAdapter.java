package com.cycycd.admin.project_danci;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Administrator on 2017/10/20.
 */

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder>{
    List<WordCard> wc;
    private OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView t1;
        TextView t2;
        View v;
        public ViewHolder(View view)
        {

            super(view);
            v=view;
            t1=(TextView) view.findViewById(R.id.rawword);
            t2=(TextView) view.findViewById(R.id.rawtran);
        }
    }
    public WordAdapter(List<WordCard> fl)
    {
        this.wc=fl;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        WordCard f=wc.get(position);
        holder.t1.setText(f.word);
        holder.t2.setText(f.tran);
        if(mOnItemClickListener != null){
            //为ItemView设置监听器
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView,position);
                }
            });
        }
    }
    @Override
    public int getItemCount()
    {
        return wc.size();
    }
}
