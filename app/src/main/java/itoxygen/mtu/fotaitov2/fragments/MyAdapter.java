package itoxygen.mtu.fotaitov2.fragments;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import itoxygen.mtu.fotaitov2.R;
import itoxygen.mtu.fotaitov2.data.Product;

/**
 * Created by keagan on 12/1/15.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ProductCardViewHolder> {

    private List<Product> products;

    // Constructor
    public MyAdapter(List<Product> products) {
        this.products = products;
    }

    // Create the product card
    @Override
    public MyAdapter.ProductCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create new product card view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);

        return new ProductCardViewHolder(v);
    }

    // Replace contents of view with proper product information
    @Override
    public void onBindViewHolder(ProductCardViewHolder holder, int position) {

        // bind product to card elements
        Product p = products.get(position);
        holder.cardTitle.setText(p.getTitle());
        holder.cardPrice.setText(p.getPrice());

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(p.getImage(), holder.cardImage);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return products.size();
    }

    // ViewHolder - get references to all elements inside the card
    public static class ProductCardViewHolder extends RecyclerView.ViewHolder {

        public TextView cardTitle;
        public TextView cardPrice;
        public ImageView cardImage;

        public ProductCardViewHolder(View v) {
            super(v);
            cardTitle = (TextView) v.findViewById(R.id.text_card_title);
            cardPrice = (TextView) v.findViewById(R.id.text_card_price);
            cardImage = (ImageView) v.findViewById(R.id.image_card_product);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // handle clicks here.
                    // maybe. not sure exactly how this should be done.
                }
            });


        }



    }

}
