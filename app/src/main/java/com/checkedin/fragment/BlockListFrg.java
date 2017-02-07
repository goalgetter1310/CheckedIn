package com.checkedin.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.checkedin.AnalyticsTrackers;
import com.checkedin.AppController;
import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.adapter.BlockListAdapter;
import com.checkedin.model.Friend;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.BlockListModel;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;
import java.util.Locale;

public class BlockListFrg extends Fragment implements VolleyStringRequest.AfterResponse, TextWatcher {

    private RecyclerView rvBlocklist;
    private EditText etSearch;

    private ArrayList<Friend> alBlockList, alAllBlockList;
    private BlockListAdapter adptBlockList;

    private WebServiceCall webServiceCall;
    private int page;
    private LinearLayoutManager mLayoutManager;
    private boolean isSearching;
    private LinearLayout llLoading;
    private int total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_blocklist, container, false);
        initViews(view);

        rvBlocklist.setLayoutManager(mLayoutManager);
        rvBlocklist.setAdapter(adptBlockList);

        webServiceCall.blockListWsCall(getContext(), page);

        rvBlocklist.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && adptBlockList.getItemCount() < total && !isSearching) {
                    llLoading.setVisibility(LinearLayout.VISIBLE);
                    page += 10;
                    webServiceCall.blockListWsCall(getContext(), page);

                }
            }
        });

        etSearch.addTextChangedListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        AppController.getInstance().trackScreenView(AnalyticsTrackers.ANALYTICS_PAGE_BLOCK_LIST);
    }

    private void initViews(View view) {
        ((MainActivity) getActivity()).toggleActionBarIcon(0, true);
        ((MainActivity) getActivity()).showSearch(View.GONE);
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_block_list));

        rvBlocklist = (RecyclerView) view.findViewById(R.id.rv_blocklist);
        etSearch = (EditText) view.findViewById(R.id.et_blocklist_search);
        llLoading = (LinearLayout) view.findViewById(R.id.ll_block_list_loading);
        webServiceCall = new WebServiceCall(this);

        alAllBlockList = new ArrayList<>();
        alBlockList = new ArrayList<>();
        adptBlockList = new BlockListAdapter(getActivity(), alBlockList);
        mLayoutManager = new LinearLayoutManager(getActivity());

    }


    @Override
    public void onResponseReceive(int requestCode) {
        llLoading.setVisibility(View.GONE);
        BlockListModel mBlockList = (BlockListModel) webServiceCall.volleyRequestInstatnce().getModelObject(BlockListModel.class, BlockListModel.class.getSimpleName());
        if (mBlockList != null) {
            if (mBlockList.getStatus() == BaseModel.STATUS_SUCCESS) {
                total = mBlockList.getTotRecords();
                alBlockList.clear();
                alBlockList.addAll(mBlockList.getData());
                alAllBlockList.addAll(mBlockList.getData());
                adptBlockList.notifyDataSetChanged();
            } else {
                Utility.showSnackBar(getActivity().findViewById(android.R.id.content), mBlockList.getMessage());
            }
        } else {
            Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getActivity().getString(R.string.server_connect_error));
        }
    }

    @Override
    public void onErrorReceive() {
        llLoading.setVisibility(View.GONE);
        Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getActivity().getString(R.string.server_connect_error));
    }

    private void blocklistSearch(String search) {
        alBlockList.clear();
        search = search.toLowerCase(Locale.getDefault());
        for (int counter = 0; counter < alAllBlockList.size(); counter++) {
            if (alAllBlockList.get(counter).getFullName().toLowerCase(Locale.getDefault()).contains(search)) {
                alBlockList.add(alAllBlockList.get(counter));
            }
        }
        adptBlockList.notifyDataSetChanged();
    }

    @Override
    public void afterTextChanged(Editable s) {
        String search = etSearch.getText().toString();
        if (!TextUtils.isEmpty(search) && alAllBlockList.size() > 0) {
            isSearching = true;
            blocklistSearch(search);
        } else {
            isSearching = false;
            alBlockList.clear();
            alBlockList.addAll(alAllBlockList);
            adptBlockList.notifyDataSetChanged();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

}
