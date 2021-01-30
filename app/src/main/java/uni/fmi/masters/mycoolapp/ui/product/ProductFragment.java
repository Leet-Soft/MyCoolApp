package uni.fmi.masters.mycoolapp.ui.product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import uni.fmi.masters.mycoolapp.Product;
import uni.fmi.masters.mycoolapp.R;

public class ProductFragment extends Fragment {

    ListView productLV;
    ProductAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("Kola", 2.5, 33,"SKu333", "1233243545"));
        products.add(new Product("Bira", 1.5, 3,"SKu333", "1233243545"));
        products.add(new Product("Voda", 0.5, 12,"SKu333", "1233243545"));
        products.add(new Product("Fanta", 2.5, 4,"SKu333", "1233243545"));
        products.add(new Product("Domati", 3.5, 51,"SKu333", "1233243545"));
        products.add(new Product("Dinq", 4.5, 23,"SKu333", "1233243545"));
        products.add(new Product("Sirene", 11.5, 56,"SKu333", "1233243545"));
        products.add(new Product("Kashkaval", 7.3, 12,"SKu333", "1233243545"));
        products.add(new Product("Sandvichi", 2.2, 55,"SKu333", "1233243545"));
        products.add(new Product("Chuski", 2.11, 66,"SKu333", "1233243545"));
        products.add(new Product("Krastavici", 3.25, 123,"SKu333", "1233243545"));
        products.add(new Product("Kola", 2.5, 33,"SKu333", "1233243545"));
        products.add(new Product("Bira", 1.5, 3,"SKu333", "1233243545"));
        products.add(new Product("Voda", 0.5, 12,"SKu333", "1233243545"));
        products.add(new Product("Fanta", 2.5, 4,"SKu333", "1233243545"));
        products.add(new Product("Domati", 3.5, 51,"SKu333", "1233243545"));
        products.add(new Product("Dinq", 4.5, 23,"SKu333", "1233243545"));
        products.add(new Product("Sirene", 11.5, 56,"SKu333", "1233243545"));
        products.add(new Product("Kashkaval", 7.3, 12,"SKu333", "1233243545"));
        products.add(new Product("Sandvichi", 2.2, 55,"SKu333", "1233243545"));
        products.add(new Product("Chuski", 2.11, 66,"SKu333", "1233243545"));
        products.add(new Product("Krastavici", 3.25, 123,"SKu333", "1233243545"));
        products.add(new Product("Kola", 2.5, 33,"SKu333", "1233243545"));
        products.add(new Product("Bira", 1.5, 3,"SKu333", "1233243545"));
        products.add(new Product("Voda", 0.5, 12,"SKu333", "1233243545"));
        products.add(new Product("Fanta", 2.5, 4,"SKu333", "1233243545"));
        products.add(new Product("Domati", 3.5, 51,"SKu333", "1233243545"));
        products.add(new Product("Dinq", 4.5, 23,"SKu333", "1233243545"));
        products.add(new Product("Sirene", 11.5, 56,"SKu333", "1233243545"));
        products.add(new Product("Kashkaval", 7.3, 12,"SKu333", "1233243545"));
        products.add(new Product("Sandvichi", 2.2, 55,"SKu333", "1233243545"));
        products.add(new Product("Chuski", 2.11, 66,"SKu333", "1233243545"));
        products.add(new Product("Krastavici", 3.25, 123,"SKu333", "1233243545"));

        View root = inflater.inflate(R.layout.fragment_product, container, false);
        productLV = root.findViewById(R.id.productsListView);
        adapter = new ProductAdapter(getContext(), R.layout.product_list_row, products);
        productLV.setAdapter(adapter);

        return root;
    }
}