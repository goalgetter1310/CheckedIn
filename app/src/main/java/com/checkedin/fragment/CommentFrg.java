package com.checkedin.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.checkedin.R;
import com.checkedin.adapter.CommentAdapter;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.model.Comment;
import com.checkedin.model.FriendsActivity;
import com.checkedin.model.UserDetail;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.CommentModel;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;


public class CommentFrg extends Fragment implements View.OnClickListener, VolleyStringRequest.AfterResponse {

    private RecyclerView rvComment;
    private EditText etNewComment;
    private ImageView ivPostComment;

    private ArrayList<Comment> alComment;
    private CommentAdapter adptComment;
    private WebServiceCall webServiceCall;
    private FriendsActivity friendsActivity;
    private boolean isCommentPost;

    private LinearLayoutManager mLayoutManager;
    private boolean isLoading;
    private int page;
    private int total;

    private ImageView ivBack;
    private CommentPost commentPostListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_comment, container, false);
        initViews(view);

        Bundle argument = getArguments();
        friendsActivity = argument.getParcelable("friend_activity");
        commentPostListener = argument.getParcelable("comment_post_listener");

        rvComment.setLayoutManager(mLayoutManager);
        rvComment.setAdapter(adptComment);

        webServiceCall.commentListWsCall(getContext(), friendsActivity.getPostId(), page);
        scrollListenerOnrecyclerView();
        ivBack.setOnClickListener(this);
        ivPostComment.setOnClickListener(this);
        return view;
    }

    private void initViews(View view) {
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        rvComment = (RecyclerView) view.findViewById(R.id.rv_comment);
        etNewComment = (EditText) view.findViewById(R.id.et_comment_add);
        ivPostComment = (ImageView) view.findViewById(R.id.iv_comment_post);

        alComment = new ArrayList<>();
        adptComment = new CommentAdapter(getActivity(), alComment);
        webServiceCall = new WebServiceCall(this);
        mLayoutManager = new LinearLayoutManager(getActivity());
    }

    private void scrollListenerOnrecyclerView() {
        rvComment.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                if (!isLoading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && adptComment.getItemCount() < total) {
                        page += 10;
                        isLoading = true;
                        webServiceCall.commentListWsCall(getContext(), friendsActivity.getPostId(), page);
                    }
                }
            }

        });
    }


    @Override
    public void onResponseReceive(int requestCode) {
        isLoading = false;
        switch (requestCode) {
            case WebServiceCall.REQUEST_CODE_COMMENT_LIST:
                CommentModel mComment = (CommentModel) webServiceCall.volleyRequestInstatnce().getModelObject(CommentModel.class, CommentModel.class.getSimpleName());
                if (mComment != null) {
                    if (mComment.getStatus() == BaseModel.STATUS_SUCCESS) {
                        total = mComment.getTotal();
                        for (int counter = mComment.getData().size() - 1; counter >= 0; counter--) {
                            alComment.add(mComment.getData().get(counter));
                        }
                        rvComment.scrollToPosition(alComment.size() - 1);
                        adptComment.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), mComment.getMessage(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
                }
                break;
            case WebServiceCall.REQUEST_CODE_POST_COMMENT:
                BaseModel baseModel = (BaseModel) webServiceCall.volleyRequestInstatnce().getModelObject(BaseModel.class, BaseModel.class.getSimpleName());
                if (baseModel != null) {

                    if (baseModel.getStatus() == BaseModel.STATUS_SUCCESS) {
                        SimpleDateFormat mFormat = new SimpleDateFormat(Utility.SERVER_MESSAGE_DATE_FORMAT, Locale.getDefault());
                        mFormat.setTimeZone(TimeZone.getTimeZone(Utility.SERVER_TIMEZONE));

                        Comment comment = new Comment();
                        UserDetail user = UserPreferences.fetchUserDetails(getActivity());
                        comment.setId(user.getId());
                        comment.setProfileImg(user.getThumbImage());
                        comment.setFirstName(user.getFirstName());
                        comment.setLastName(user.getLastName());
                        comment.setProfileImg(user.getImageUrl());
                        comment.setTime(mFormat.format(Calendar.getInstance().getTime()));
                        comment.setText(etNewComment.getText().toString());
                        alComment.add(comment);
                        adptComment.notifyDataSetChanged();
                        friendsActivity.setCommentCount((friendsActivity.getCommentCount() + 1));

                        Utility.hideKeyboard(getActivity());
                        etNewComment.setText("");

                        isCommentPost = true;
                        rvComment.scrollToPosition(alComment.size() - 1);
                    } else {
                        Toast.makeText(getActivity(), baseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    @Override
    public void onErrorReceive() {
        isLoading = true;
        Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
            Utility.doubleTapTime = System.currentTimeMillis();
            switch (v.getId()) {
                case R.id.iv_back:
                    ((DialogFragmentContainer) getParentFragment()).popFragment();
                    break;
                case R.id.iv_comment_post:
                    String comment = etNewComment.getText().toString().trim();
                    if (TextUtils.isEmpty(comment))
                        return;
                    isLoading = true;
                    webServiceCall.postCommentWsCall(getContext(), friendsActivity.getPostId(), comment);
                    break;
            }
        }
    }

    @Override
    public void onDestroyView() {
        if (commentPostListener != null)
            commentPostListener.onCommentPosted(isCommentPost);
        super.onDestroyView();
    }

    public interface CommentPost extends Parcelable {
        void onCommentPosted(boolean isPosted);
    }

}
