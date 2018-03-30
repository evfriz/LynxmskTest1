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

public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter<ArticleRecyclerViewAdapter.ViewHolder> {

    private List<ArticleClass> records;
    private ItemClickListener mClickListener;

    public ArticleRecyclerViewAdapter(List<ArticleClass> records) {
        this.records = records;
    }

     // Создание новых View и ViewHolder элемента списка, которые впоследствии могут переиспользоваться.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.article_recycler_item, viewGroup, false);
        return new ViewHolder(v);
    }

     // Заполнение виджетов View данными из элемента списка с номером i
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ArticleClass record = records.get(i);
        viewHolder.header.setText(record.header);
        viewHolder.text.setText(record.text);
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

     // Реализация класса ViewHolder, хранящего ссылки на виджеты.
    class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private TextView header;
        private TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.tv_header);
            text = (TextView) itemView.findViewById(R.id.tv_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getHeader(int id) {
        ArticleClass record = records.get(id);
        return record.header;
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