package sample.com.horoscope;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by dheerajprasad on 1/10/2015.
 */
public class ViewHolder extends RecyclerView.ViewHolder {
      TextView mItemTextView;

    public ViewHolder(View parent, TextView itemTextView) {
        super(parent);
      //  mItemTextView = itemTextView;
        mItemTextView =  (TextView) parent.findViewById(R.id.home_textview);
    }

    public static ViewHolder newInstance(View parent) {
        TextView itemTextView = (TextView) parent.findViewById(R.id.home_textview);
        return new ViewHolder(parent, itemTextView);
    }

    public void setItemText(CharSequence text) {
        mItemTextView.setText(text);
    }

}