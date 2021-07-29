package com.example.panshippingandroid.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.panshippingandroid.R;
import com.example.panshippingandroid.model.ProductDto;
import com.example.panshippingandroid.model.ProductModel;
import com.example.panshippingandroid.utils.ImageUtils;

import java.io.IOException;
import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.panshippingandroid.activities.LoginActivity.apiService;
import static com.example.panshippingandroid.utils.Const.AUTHENTICATION_FILE_NAME;
import static com.example.panshippingandroid.utils.Const.PICK_IMAGE;
import static com.example.panshippingandroid.utils.Const.SELECT_PICTURE;
import static com.example.panshippingandroid.utils.Const.USER_ID;

public class AddProductFragment extends Fragment {

    public static final String TAG = "Add products fragment";
    private EditText et_name;
    private EditText et_price;
    private EditText et_quantity;
    private EditText et_description;
    private ImageView iv_addImage;
    private Button btn_addProduct;
    private boolean isAllFieldsChecked = false;
    private SharedPreferences sharedPreferences;
    private String image;
    boolean isEdit;
    private Long id;

    public AddProductFragment() {
    }

    public static AddProductFragment newInstance() {
        AddProductFragment fragment = new AddProductFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = requireContext().getSharedPreferences(AUTHENTICATION_FILE_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edit();
    }

    public void edit() {
        if (getArguments() != null) {
            isEdit = getArguments().getBoolean("isEdit");
            id = getArguments().getLong("productId");
        }
        initUI();

        if (isEdit) {
            getProductCall(id);
            btn_addProduct.setText("Edit");
            iv_addImage.setOnClickListener(this::setImage);
            btn_addProduct.setOnClickListener(v -> {
                if (isEdit) {
                    Long userID = sharedPreferences.getLong(USER_ID, 0);
                    btn_addProduct.setText("Edit");
                    editProductCall(id, setProduct(userID));
                }
            });
        } else {

            iv_addImage.setOnClickListener(this::setImage);
            btn_addProduct.setOnClickListener(v -> {
                Long userID = sharedPreferences.getLong(USER_ID, 0);
                addProductCall(setProduct(userID));

            });
        }
    }

    public void editProductCall(Long id, ProductModel product) {
        Call<Void> call = apiService.editProduct(id, product);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.code() == HttpURLConnection.HTTP_OK || response.code() == HttpURLConnection.HTTP_CREATED) {
                    FragmentTransaction fr = getParentFragmentManager().beginTransaction();
                    fr.replace(R.id.container, ViewProductsFragment.newInstance());
                    fr.commit();
                } else {
                    Toast.makeText(getActivity(), R.string.was_not_change_product, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    private ProductModel setProduct(Long userID) {
        isAllFieldsChecked = CheckAllFields();
        ProductModel productModel = new ProductModel();
        if (isAllFieldsChecked) {
            productModel.setId(id);
            productModel.setName(et_name.getText().toString());
            productModel.setPrice(Double.parseDouble(et_price.getText().toString()));
            productModel.setQuantity(Integer.parseInt(et_quantity.getText().toString()));
            productModel.setDescription(et_description.getText().toString());
            productModel.setUser(userID);
            productModel.setImage(image);
        }
        return productModel;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri pictureUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), pictureUri);
            } catch (NullPointerException | IOException n) {
                n.printStackTrace();
            }
            assert bitmap != null;
            Bitmap productImage = ImageUtils.getResizedBitmap(bitmap, 400);
            if (productImage != null) {
                image = ImageUtils.convertBitmapToStringImage(productImage);
            }

            Glide.with(this)
                    .load(productImage)
                    .error(R.drawable.ic_add)
                    .override(400, 400)
                    .into(iv_addImage);
        }
    }

    private void initUI() {
        et_name = requireView().findViewById(R.id.et_name);
        et_price = requireView().findViewById(R.id.et_price);
        et_quantity = requireView().findViewById(R.id.et_quantity);
        et_description = requireView().findViewById(R.id.et_description);
        iv_addImage = requireView().findViewById(R.id.iv_addImage);
        btn_addProduct = requireView().findViewById(R.id.btn_addProduct);
    }

    private boolean CheckAllFields() {
        if (et_name.getText().toString().length() == 0) {
            et_name.setError(getString(R.string.field_is_required));
            return false;
        }
        if (et_price.getText().toString().length() == 0) {
            et_price.setError(getString(R.string.field_is_required));
            return false;
        }
        if (et_quantity.getText().toString().length() == 0) {
            et_quantity.setError(getString(R.string.field_is_required));
            return false;
        }
        if (et_description.getText().toString().length() == 0) {
            et_description.setError(getString(R.string.field_is_required));
            return false;
        } else if (iv_addImage == null) {
            Toast.makeText(getActivity(), R.string.select_image, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void getProductCall(Long id) {
        Call<ProductDto> call = apiService.getProduct(id);
        call.enqueue(new Callback<ProductDto>() {
            @Override
            public void onResponse(@NonNull Call<ProductDto> call, @NonNull Response<ProductDto> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    ProductDto product = response.body();
                    if (product != null) {
                        et_name.setText(product.getName());
                        et_price.setText(String.valueOf(product.getPrice()));
                        et_quantity.setText(String.valueOf(product.getQuantity()));
                        et_description.setText(product.getDescription());
                    }
                    if (product != null) {
                        if (product.getImage() != null) {
                            Glide.with(requireContext())
                                    .load(ImageUtils.convertStringImageToBitmap(product.getImage()))
                                    .override(400, 400)
                                    .into(iv_addImage);
                        } else {
                            Glide.with(requireContext())
                                    .load(AppCompatResources.getDrawable(requireContext(), R.drawable.sale))
                                    .override(400, 400)
                                    .into(iv_addImage);
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.was_not_added_product, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductDto> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    public void addProductCall(ProductModel productModel) {
        Call<Void> call = apiService.addProduct(productModel);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.code() == HttpURLConnection.HTTP_CREATED) {
                    Toast.makeText(getActivity(), R.string.successfully_added_product, Toast.LENGTH_SHORT).show();
                    FragmentTransaction fr = getParentFragmentManager().beginTransaction();
                    fr.replace(R.id.container, ViewProductsFragment.newInstance());
                    fr.commit();
                } else {
                    Toast.makeText(getActivity(), R.string.was_not_added_product, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    private void setImage(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }

    private void onBack(View v) {
        getFragmentManager().popBackStackImmediate();
    }
}