package uni.fmi.masters.mycoolapp.ui.product;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import uni.fmi.masters.mycoolapp.BarcodeActivity;
import uni.fmi.masters.mycoolapp.Product;
import uni.fmi.masters.mycoolapp.R;

import static android.app.Activity.RESULT_OK;

public class ProductFragment extends Fragment {

    public static final int REQUEST_BARCODE_CODE = 13;
    ListView productLV;
    EditText filterET;

    ProductAdapter adapter;
    ArrayList<Product> products;
    ProgressDialog dialog;
    FloatingActionButton addProductB;

    Dialog customDialog;

    EditText barcodeET;

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            customDialog = new Dialog(getContext());
            customDialog.setContentView(R.layout.add_edit_product_dialog);

            final EditText nameET = customDialog.findViewById(R.id.nameEditText);
            final EditText priceET = customDialog.findViewById(R.id.priceEditText);
            final EditText quantityET = customDialog.findViewById(R.id.quantityEditText);
            final EditText numberET = customDialog.findViewById(R.id.numberEditText);
            barcodeET = customDialog.findViewById(R.id.barcodeEditText);
            Button scanB = customDialog.findViewById(R.id.scanBarcodeButton);
            Button okB = customDialog.findViewById(R.id.okButton);
            Button cancelB = customDialog.findViewById(R.id.cancelButton);

            cancelB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customDialog.cancel();
                }
            });


            okB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String urlString = String.format("http://78.130.252.70:8989/AddProduct?name=%s&price=%s&quantity=%s&number=%s&barcode=%s&id=%s",
                                    nameET.getText().toString(), priceET.getText().toString(),quantityET.getText().toString(),
                                    numberET.getText().toString(), barcodeET.getText().toString(), 0);

                            HttpURLConnection conn = null;

                            try{
                                URL url = new URL(urlString);
                                conn = (HttpURLConnection) url.openConnection();

                                InputStream stream = new BufferedInputStream(conn.getInputStream());
                                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                                String response = reader.readLine();

                                if(response != null){

                                    int recievedID = Integer.parseInt(response);

                                    final Product product = new Product();
                                    product.setID(recievedID);
                                    product.setName(nameET.getText().toString());
                                    product.setQuantity(Integer.parseInt(quantityET.getText().toString()));
                                    product.setPrice(Double.parseDouble(priceET.getText().toString()));
                                    product.setNumber(numberET.getText().toString());
                                    product.setBarcode(barcodeET.getText().toString());

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            products.add(product);
                                            adapter.notifyDataSetChanged();

                                            customDialog.hide();
                                        }
                                    });
                                }

                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (NumberFormatException e){
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Article has not been added!!!", Toast.LENGTH_LONG).show();
                            }
                            finally {
                                if(conn != null)
                                    conn.disconnect();
                            }
                        }
                    }).start();
                }
            });


            scanB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(getActivity(), BarcodeActivity.class), REQUEST_BARCODE_CODE);
                }
            });

            customDialog.setTitle("Add Product");
            customDialog.setCanceledOnTouchOutside(false);

            customDialog.show();
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        products = new ArrayList<>();

        View root = inflater.inflate(R.layout.fragment_product, container, false);
        productLV = root.findViewById(R.id.productsListView);
        adapter = new ProductAdapter(getContext(), R.layout.product_list_row, products);
        productLV.setAdapter(adapter);
        addProductB = root.findViewById(R.id.addProductButton);
        addProductB.setOnClickListener(onClick);
        filterET = root.findViewById(R.id.filterEditText);

        filterET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        dialog = new ProgressDialog(getContext());
        dialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String urlString = "http://78.130.252.70:8989/GetAllProducts";

                HttpURLConnection urlConnection = null;

                try{
                    URL url = new URL(urlString);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    BufferedInputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                    String response = reader.readLine();

                    if(response != null){
                        JSONArray jsonArray = new JSONArray(response);

                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            Product product = new Product();

                            product.setBarcode(jsonObject.getString("Barcode"));
                            product.setID(jsonObject.getInt("ID"));
                            product.setName(jsonObject.getString("Name"));
                            product.setNumber(jsonObject.getString("Number"));
                            product.setQuantity(jsonObject.getInt("Quantity"));
                            product.setPrice(jsonObject.getDouble("Price"));

                            products.add(product);
                        }
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            dialog.hide();
                        }
                    });



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if(urlConnection != null)
                        urlConnection.disconnect();
                }

            }
        }).start();



        productLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final Product prod = (Product) parent.getItemAtPosition(position);


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String urlString = "http://78.130.252.70:8989/DeleteProduct?id=" + prod.getID();

                                HttpURLConnection urlConnection = null;

                                try{
                                    URL url = new URL(urlString);
                                    urlConnection = (HttpURLConnection) url.openConnection();

                                    BufferedInputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                                    String response = reader.readLine();

                                    if(response != null){
                                if(response.equals("false")){
                                    Toast.makeText(getActivity(), "Item was not deleted!", Toast.LENGTH_LONG).show();
                                }

                            }

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    for(int i = 0 ; i < products.size(); i++){
                                        if(products.get(i).getID() == prod.getID()){
                                            products.remove(i);
                                        }
                                    }

                                    adapter.notifyDataSetChanged();
                                }
                            });

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if(urlConnection != null)
                                urlConnection.disconnect();
                        }
                    }
                }).start();

                return false;
            }
        });



        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_BARCODE_CODE){
            if(resultCode == RESULT_OK){
                barcodeET.setText(data.getData().toString());
            }else{
                Toast.makeText(getActivity(), "No barcode selected!", Toast.LENGTH_LONG).show();
            }
        }
    }
}