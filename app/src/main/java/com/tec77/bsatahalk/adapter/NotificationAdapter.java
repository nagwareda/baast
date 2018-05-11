package com.tec77.bsatahalk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.api.response.notification.PartModelNotification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;



/**
 * Created by Mahmoud Shaeer on 12/09/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private ArrayList<PartModelNotification> allNotification = new ArrayList<>();
    private Context context;
    private int count = -1;
    private boolean languageArabic;
    private NotificationOnClickListener mNotifOnClickListener;

    public interface NotificationOnClickListener{
        void onClick(PartModelNotification notification);
    }

    public void setCount(int count) {
        this.count = count;
    }

    public NotificationAdapter(ArrayList<PartModelNotification> allNotification, Context context, int count, NotificationOnClickListener mNotifOnClickListener) {
        this.allNotification = allNotification;
        this.context = context;
        this.count = count;
        this.mNotifOnClickListener = mNotifOnClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PartModelNotification partModelNotification = allNotification.get(position);
        Date cDate = new Date();
        long diffInMs = cDate.getTime() / 1000 - partModelNotification.getNotification_date();
        holder.dateNotification.setText(convertSecondsToHMmSs(diffInMs, partModelNotification.getNotification_date()));
        holder.detalisNotification.setText(partModelNotification.getNotification_content());
        if (count < position + 1) {
            holder.itemView.setBackgroundResource(R.drawable.shape_read_notification);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.shape_unread_notification);
        }
    }

    public String convertSecondsToHMmSs(long seconds, long dateInSeconds) {
        if (Locale.getDefault().getLanguage().contains("ar"))
           languageArabic=true ;
        String result = context.getString(R.string.sice);
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long second = seconds % 60;
        if (hours > 24) {
            result = getDate(dateInSeconds * 1000, "dd/MM/yyyy , hh:mm:ss");
        } else {
            result += " " + convert2String(String.valueOf(hours),languageArabic) + ":" + convert2String(String.valueOf(minutes),languageArabic)
                    + ":" + convert2String(String.valueOf(second),languageArabic);
        }
        return result;
    }

    public String convert2String(String str, boolean languageArabic) {
        char[] arabicChars;
        if(languageArabic) {
             arabicChars = new char[]{'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
        }else
        {
            arabicChars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                builder.append(arabicChars[(int) (str.charAt(i)) - 48]);
            } else {
                builder.append(str.charAt(i));
            }
        }
        return builder.toString();
    }


    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    @Override
    public int getItemCount() {
        return allNotification.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView detalisNotification, dateNotification;

        public ViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(final View itemView) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.setBackgroundResource(R.drawable.shape_read_notification);
                    int selected_position = getAdapterPosition();
                    mNotifOnClickListener.onClick(allNotification.get(selected_position));
                    //Toast.makeText(context, allNotification.get(selected_position).getNotification_content() + " is selected!", Toast.LENGTH_SHORT).show();
                }
            });
            detalisNotification = (TextView) itemView.findViewById(R.id.ItemAllNotification_TextView_detalisNotiication);
            dateNotification = (TextView) itemView.findViewById(R.id.ItemAllNotification_TextView_dateNotiication);
        }

    }
}
