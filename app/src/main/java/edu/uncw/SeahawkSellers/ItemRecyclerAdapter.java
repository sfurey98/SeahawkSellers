package edu.uncw.SeahawkSellers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ItemRecyclerAdapter extends FirestoreRecyclerAdapter<Item, ItemRecyclerAdapter.ItemViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private final OnItemClickListener listener;

    ItemRecyclerAdapter(FirestoreRecyclerOptions<Item> options, OnItemClickListener listener) {
        super(options);
        this.listener = listener;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        final CardView view;
        final TextView title;
        final TextView description;
        final TextView price;
        final TextView seller;

       ItemViewHolder(CardView v) {
            super(v);
            view = v;
            title = v.findViewById(R.id.item_title);
            description = v.findViewById(R.id.item_description);
            price = v.findViewById(R.id.item_price);
            seller = v.findViewById(R.id.item_seller);

        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ItemViewHolder holder, @NonNull int position, @NonNull final Item item) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.price.setText("$" +item.getPrice());
        holder.seller.setText(item.getSeller());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.getAdapterPosition());
            }
        });
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);

        return new ItemViewHolder(v);
    }
}



