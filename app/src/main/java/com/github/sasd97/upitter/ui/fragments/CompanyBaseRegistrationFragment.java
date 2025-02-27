package com.github.sasd97.upitter.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.components.ImageUploaderView;
import com.github.sasd97.upitter.events.OnCompanyRegistrationListener;
import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.ui.adapters.recyclers.ContactPhonesRecycler;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.ui.results.CategoriesSelectionResult;
import com.github.sasd97.upitter.utils.Gallery;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.CATEGORIES_ATTACH;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.PUT_CROPPED_IMAGE;
import static com.github.sasd97.upitter.constants.RequestCodesConstants.CATEGORIES_ACTIVITY_REQUEST;
import static com.github.sasd97.upitter.constants.RequestCodesConstants.GALLERY_ACTIVITY_REQUEST;

/**
 * Created by Alexadner Dadukin on 24.06.2016.
 */

public class CompanyBaseRegistrationFragment
        extends BaseFragment {

    @BindString(R.string.empty_field) String EMPTY_ERROR;

    private CompanyModel.Builder companyBuilder;
    private ArrayList<Integer> categoriesSelected;
    private OnCompanyRegistrationListener listener;
    private ContactPhonesRecycler contactPhonesRecycler;

    @BindView(R.id.add_phone_button_registration_base_fragment) RelativeLayout addPhoneLayout;
    @BindView(R.id.set_position_business_registration_base_fragment) Button setPositionButton;
    @BindView(R.id.phones_recyclerview_registration_base_fragment) RecyclerView phonesRecyclerView;
    @BindView(R.id.categories_choose_business_registration_base_fragment) RelativeLayout categoriesLayout;
    @BindView(R.id.name_edittext_business_registration_base_fragment) MaterialEditText companyNameEditText;
    @BindView(R.id.site_edittext_business_registration_base_fragment) MaterialEditText companySiteEditText;
    @BindView(R.id.description_edittext_business_registration_base_fragment) MaterialEditText companyDescriptionEditText;

    public CompanyBaseRegistrationFragment() {
        super(R.layout.fragment_company_base_registration);
    }

    public static CompanyBaseRegistrationFragment getFragment(OnCompanyRegistrationListener listener) {
        CompanyBaseRegistrationFragment fragment = new CompanyBaseRegistrationFragment();
        fragment.setRegistrationListener(listener);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void setupViews() {
        companyBuilder = new CompanyModel.Builder();

        categoriesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCategoryChooseClick();
            }
        });
        addPhoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddPhoneClick();
            }
        });
        setPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {onAddressChooseClick(true);
            }
        });

        contactPhonesRecycler = new ContactPhonesRecycler();
        phonesRecyclerView.setHasFixedSize(true);
        phonesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        phonesRecyclerView.setAdapter(contactPhonesRecycler);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        LinearLayout.LayoutParams lp =
                                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                        contactPhonesRecycler.removePhone(viewHolder.getAdapterPosition());
                        phonesRecyclerView.setLayoutParams(lp);
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(phonesRecyclerView);
    }

    private void setRegistrationListener(OnCompanyRegistrationListener listener) {
        this.listener = listener;
    }

    private void onAddressChooseClick(boolean isExtraRequired) {
        if (!validateForm(isExtraRequired)) return;

        companyBuilder
                        .name(companyNameEditText.getText().toString().trim())
                        .description(companyDescriptionEditText.getText().toString().trim())
                        .contactPhones(contactPhonesRecycler.getPhones())
                        .site(companySiteEditText.getText().toString().trim());

        listener.onBaseInfoPrepared(companyBuilder);
    }

    private void onAddPhoneClick() {
        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        contactPhonesRecycler.addPhone();
        phonesRecyclerView.setLayoutParams(lp);
    }

    private void onAvatarChooseClick() {
        startActivityForResult(new Gallery.Builder()
                .from(getContext())
                .multiSelectionMode(false)
                .build(), GALLERY_ACTIVITY_REQUEST);
    }

    private void onCategoryChooseClick() {
        Intent intent = new Intent(getActivity(), CategoriesSelectionResult.class);

        if (categoriesSelected != null && categoriesSelected.size() != 0)
            intent.putIntegerArrayListExtra(CATEGORIES_ATTACH, categoriesSelected);

        startActivityForResult(intent, CATEGORIES_ACTIVITY_REQUEST);
    }

    private void handleAvatarIntent(@NonNull Intent intent) {
        String path = intent.getStringExtra(PUT_CROPPED_IMAGE);
    }

    private void handleCategoriesIntent(@NonNull Intent intent) {
        categoriesSelected = intent.getIntegerArrayListExtra(CATEGORIES_ATTACH);
        companyBuilder.categories(categoriesSelected);
    }

    private boolean validateForm(boolean isExtraRequired) {
        boolean result = true;

        if (companyNameEditText.getText().length() == 0){
            companyNameEditText.setError(EMPTY_ERROR);
            result = false;
        }

        if (companyDescriptionEditText.getText().length() == 0) {
            companyDescriptionEditText.setError(EMPTY_ERROR);
            result = false;
        }

        if (categoriesSelected == null ||
                categoriesSelected.size() == 0) {
            Snackbar
                    .make(getView(), getString(R.string.not_present_category_company_registration_activity), Snackbar.LENGTH_LONG)
                    .show();
            result = false;
        }

        if (!result) return false;
        if (!isExtraRequired) return result;

        if (contactPhonesRecycler.getPhones() == null ||
                contactPhonesRecycler.getPhones().size() == 0 ||
                    companySiteEditText.getText().length() == 0) {
            Snackbar
                    .make(getView(), getString(R.string.extra_information_was_not_supply_company_registration_activity), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.ignore_extra_company_registration_activity), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onAddressChooseClick(false);
                        }
                    })
                    .show();
            result = false;
        }

        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != BaseActivity.RESULT_OK) return;

        if (requestCode == GALLERY_ACTIVITY_REQUEST) {
            handleAvatarIntent(data);
            return;
        }

        if (requestCode == CATEGORIES_ACTIVITY_REQUEST) {
            handleCategoriesIntent(data);
            return;
        }
    }
}
