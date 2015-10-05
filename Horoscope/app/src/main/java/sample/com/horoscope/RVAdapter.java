package sample.com.horoscope;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ZodiacSignViewHolder> {

    List<ZodiacSign> ZodiacSign;
    Context ctx;


    RVAdapter(List<ZodiacSign> ZodiacSign,Context ctx){
        this.ZodiacSign = ZodiacSign;
        this.ctx=ctx;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ZodiacSignViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
     final   View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.zodiac_sign_list_item, viewGroup, false);
     final    ZodiacSignViewHolder pvh = new ZodiacSignViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long itemId = (pvh.getAdapterPosition());

                sample.com.horoscope.ZodiacSign zk = new ZodiacSign();

                zk=    ZodiacSign.get((int) itemId);

           String s=     zk.name;

                Intent intent = new Intent(ctx, HoroscopeDetail.class);
                intent.putExtra("sign",zk.name);
                intent.putExtra("selectiontype",ListView.selectionType);




                ctx.startActivity(intent);

            }
        });

        return pvh;
    }

    @Override
    public void onBindViewHolder(RVAdapter.ZodiacSignViewHolder holder, int position) {


        holder.ZodiacSignname.setText(ZodiacSign.get(position).name);
        holder.Zodiactimeline.setText(ZodiacSign.get(position).duration);
        holder.Zodiacfortime.setText(ZodiacSign.get(position).fordate);
    /*    Picasso.with(ctx)
                .load(ZodiacSign.get(position).image)
                .into(holder.zodiacsignPhoto);
*/
        Picasso.with(ctx)
                .load(ZodiacSign.get(position).image)

                .error(R.drawable.ic_launcher)      // optional
                                     // optional
                .into(holder.zodiacsignPhoto);
        holder.zodiacsignPhoto.setContentDescription(ZodiacSign.get(position).name);
      //  holder.zodiacsignPhoto.setImageResource(ZodiacSign.get(position).image);

    }


    @Override
    public int getItemCount() {
        return ZodiacSign.size();
    }



    public static class ZodiacSignViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView ZodiacSignname;
        TextView Zodiactimeline;
        ImageView zodiacsignPhoto;
        TextView Zodiacfortime;

        @Override
        public String toString() {
            return super.toString();
        }

        ZodiacSignViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.ZodiacCardView);
            ZodiacSignname = (TextView) itemView.findViewById(R.id.ZodiacSignname);
            Zodiactimeline = (TextView) itemView.findViewById(R.id.Zodiactimeline);
            Zodiacfortime = (TextView) itemView.findViewById(R.id.Zodiacfortime);
            zodiacsignPhoto = (ImageView) itemView.findViewById(R.id.ZodiacSignImageView);
        }


    }

}