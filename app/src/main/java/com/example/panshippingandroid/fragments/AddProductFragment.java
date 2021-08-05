package com.example.panshippingandroid.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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

    private EditText addNameEt;
    private EditText addPriceEt;
    private EditText addQuantityEt;
    private EditText addDescriptionEt;
    private ImageView addImageIv;
    private ImageView cancelIv;
    private Button addProductBtn;
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
            cancelIv.setVisibility(View.VISIBLE);

            getProductCall(id);
            addProductBtn.setText("Edit");
            addImageIv.setOnClickListener(this::setImage);
            addProductBtn.setOnClickListener(v -> {
                if (isEdit) {
                    Long userID = sharedPreferences.getLong(USER_ID, 0);
                    addProductBtn.setText("Edit");
                    editProductCall(id, setProduct(userID));
                }
            });
        } else {
            addImageIv.setOnClickListener(this::setImage);
            addProductBtn.setOnClickListener(v -> {
                Long userID = sharedPreferences.getLong(USER_ID, 0);
                addProductCall(setProduct(userID));
            });
        }
        cancelIv.setOnClickListener(v -> {
            Glide.with(requireContext())
                    .load(AppCompatResources.getDrawable(requireContext(), R.drawable.sale))
                    .override(400, 400)
                    .into(addImageIv)
                    .clearOnDetach();
            cancelIv.setVisibility(View.GONE);
        });
    }

    public void editProductCall(Long id, ProductModel product) {
        Call<Void> call = apiService.editProduct(id, product);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.code() == HttpURLConnection.HTTP_OK || response.code() == HttpURLConnection.HTTP_CREATED) {
                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, ViewProductsFragment.newInstance());
                    fragmentTransaction.commit();
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
            productModel.setName(addNameEt.getText().toString());
            productModel.setPrice(Double.parseDouble(addPriceEt.getText().toString()));
            productModel.setQuantity(Integer.parseInt(addQuantityEt.getText().toString()));
            productModel.setDescription(addDescriptionEt.getText().toString());
            productModel.setUser(userID);
            Bitmap bitmap = ((BitmapDrawable) addImageIv.getDrawable()).getBitmap();
            image = ImageUtils.convertBitmapToStringImage(bitmap);
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
            Bitmap productImage = null;
            if (bitmap != null) {
                productImage = ImageUtils.getResizedBitmap(bitmap, 400);
            }
            if (productImage != null) {
                image = ImageUtils.convertBitmapToStringImage(productImage);
            }

            Glide.with(this)
                    .load(productImage)
                    .error(R.drawable.ic_add)
                    .override(400, 400)
                    .into(addImageIv);
        }
    }

    private void initUI() {
        addNameEt = requireView().findViewById(R.id.et_name);
        addPriceEt = requireView().findViewById(R.id.et_price);
        addQuantityEt = requireView().findViewById(R.id.et_quantity);
        addDescriptionEt = requireView().findViewById(R.id.et_description);
        addImageIv = requireView().findViewById(R.id.iv_addImage);
        addProductBtn = requireView().findViewById(R.id.btn_addProduct);
        cancelIv = requireView().findViewById(R.id.iv_cancel);
    }

    private boolean CheckAllFields() {
        if (addNameEt.getText().toString().length() == 0) {
            addNameEt.setError(getString(R.string.field_is_required));
            return false;
        }
        if (addPriceEt.getText().toString().length() == 0) {
            addPriceEt.setError(getString(R.string.field_is_required));
            return false;
        }
        if (addQuantityEt.getText().toString().length() == 0) {
            addQuantityEt.setError(getString(R.string.field_is_required));
            return false;
        }
        if (addDescriptionEt.getText().toString().length() == 0) {
            addDescriptionEt.setError(getString(R.string.field_is_required));
            return false;
        } else if (addImageIv == null) {
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
                        addNameEt.setText(product.getName());
                        addPriceEt.setText(String.valueOf(product.getPrice()));
                        addQuantityEt.setText(String.valueOf(product.getQuantity()));
                        addDescriptionEt.setText(product.getDescription());
                    }
                    if (product != null) {
                        if (product.getImage() != null) {
                            Glide.with(requireContext())
                                    .load(ImageUtils.convertStringImageToBitmap(product.getImage()))
                                    .override(400, 400)
                                    .into(addImageIv);
                        } else {
                            Glide.with(requireContext())
                                    .load(AppCompatResources.getDrawable(requireContext(), R.drawable.sale))
                                    .override(400, 400)
                                    .into(addImageIv);
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
        addProductBtn.setEnabled(false);
        Call<Void> call = apiService.addProduct(productModel);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.code() == HttpURLConnection.HTTP_CREATED) {
                    Toast.makeText(getActivity(), R.string.successfully_added_product, Toast.LENGTH_SHORT).show();
                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, ViewProductsFragment.newInstance());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    addProductBtn.setEnabled(true);
                } else {
                    Toast.makeText(getActivity(), R.string.was_not_added_product, Toast.LENGTH_SHORT).show();
                    addProductBtn.setEnabled(true);
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
}