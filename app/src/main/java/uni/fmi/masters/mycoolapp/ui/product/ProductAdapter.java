package uni.fmi.masters.mycoolapp.ui.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import uni.fmi.masters.mycoolapp.MainActivity;
import uni.fmi.masters.mycoolapp.Product;
import uni.fmi.masters.mycoolapp.R;

public class ProductAdapter extends ArrayAdapter<Product> implements Filterable {


    public Filter getFilter(){

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();

                ArrayList<Product> temp = new ArrayList<>();

                for(int i = 0; i < getCount(); i++){

                    if(getItem(i).getName().contains(constraint)){
                        temp.add(getItem(i));
                    }
                }

                results.count = temp.size();
                results.values = temp;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {


                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }

            }
        };

        return filter;
    }




    public ProductAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = ((MainActivity)getContext()).getLayoutInflater();
            convertView = inflater.inflate(R.layout.product_list_row, parent, false);
        }

        TextView nameTV = convertView.findViewById(R.id.nameTextView);
        TextView priceTV = convertView.findViewById(R.id.priceTextView);
        TextView quantityTV = convertView.findViewById(R.id.quantityTextView);
        TextView numberTV = convertView.findViewById(R.id.numberTextView);
        TextView barcodeTV = convertView.findViewById(R.id.barcodeTextView);
    //    TextView idTV = convertView.findViewById(R.id.idTextView);

        nameTV.setText(getItem(position).getName());
        priceTV.setText(String.valueOf(getItem(position).getPrice()));
        quantityTV.setText(getItem(position).getQuantity() + "");
        numberTV.setText(getItem(position).getNumber());
        barcodeTV.setText(getItem(position).getBarcode());
//        idTV.setText(getItem(position).getID());

        return  convertView;
    }
}
