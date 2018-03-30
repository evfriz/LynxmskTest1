package ru.good_trends.lynxmsktest1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Класс адаптера наследуется от RecyclerView.
 * Adapter с указанием класса, который будет хранить ссылки на виджеты элемента списка, т.е. класса, имплементирующего ViewHolder.
 * В нашем случае класс объявлен внутри класса адаптера.
 */

public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<EventsRecyclerViewAdapter.ViewHolder> {

    private List<EventsClass> records;
    private ItemClickListener mClickListener;

    public EventsRecyclerViewAdapter(List<EventsClass> records) {
        this.records = records;
    }

    // Создание новых View и ViewHolder элемента списка, которые впоследствии могут переиспользоваться.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.events_recycler_item, viewGroup, false);
        return new ViewHolder(v);
    }

    // Заполнение виджетов View данными из элемента списка с номером i
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        EventsClass record = records.get(i);
        viewHolder.title.setText(record.title);
        viewHolder.coefficient.setText(record.coefficient);
        viewHolder.time.setText(record.time);
        viewHolder.place.setText(record.place);
        viewHolder.preview.setText(record.preview);
        viewHolder.article.setText(record.article);
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    // Реализация класса ViewHolder, хранящего ссылки на виджеты.
    class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        //private ImageView icon;
        private TextView title;
        private TextView coefficient;
        private TextView time;
        private TextView place;
        private TextView preview;
        private TextView article;

        public ViewHolder(View itemView) {
            super(itemView);
            //icon = (ImageView) itemView.findViewById(R.id.recyclerViewItemIcon);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            coefficient = (TextView) itemView.findViewById(R.id.tv_coefficient);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            place = (TextView) itemView.findViewById(R.id.tv_place);
            preview = (TextView) itemView.findViewById(R.id.tv_preview);
            article = (TextView) itemView.findViewById(R.id.tv_article);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getArticle(int id) {
        EventsClass record = records.get(id);
        return record.article;
    }


    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}