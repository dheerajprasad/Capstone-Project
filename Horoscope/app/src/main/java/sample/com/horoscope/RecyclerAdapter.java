package sample.com.horoscope;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by dheerajprasad on 1/10/2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> mItemList;
    private Context ctx;

    public RecyclerAdapter(List<String> itemList, Context ctmx) {

        ctx=ctmx;
        mItemList = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list, parent, false);

        final ViewHolder vh = new ViewHolder(view,null);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                long itemId = vh.getAdapterPosition();
            String chk=    mItemList.get((int)itemId);

                if (chk.equalsIgnoreCase(ctx.getString(R.string.Contactus))) {
                    Intent intent = new Intent(ctx, ContactUs.class);
                 //   intent.putExtra("selectiontype", chk);
                    ctx.startActivity(intent);

                }
                else {
                    Intent intent = new Intent(ctx, ListView.class);
                    intent.putExtra("selectiontype", chk);
                    ctx.startActivity(intent);
                }
            }
        });
     //    return ViewHolder.newInstance(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;

        String itemText = mItemList.get(position);
        holder.mItemTextView.setText(itemText);
       // holder.setItemText();
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }


}
