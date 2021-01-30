package uni.fmi.masters.mycoolapp.ui.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import uni.fmi.masters.mycoolapp.MainActivity;
import uni.fmi.masters.mycoolapp.Product;
import uni.fmi.masters.mycoolapp.R;

public class ProductAdapter extends ArrayAdapter<Product> {

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

        nameTV.setText(getItem(position).getName());
        priceTV.setText(String.valueOf(getItem(position).getPrice()));
        quantityTV.setText(getItem(position).getQuantity() + "");
        numberTV.setText(getItem(position).getNumber());
        barcodeTV.setText(getItem(position).getBarcode());

        return  convertView;
    }
}
