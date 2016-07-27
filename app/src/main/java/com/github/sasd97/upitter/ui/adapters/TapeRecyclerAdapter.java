package com.github.sasd97.upitter.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.components.CollageLayoutManager;
import com.github.sasd97.upitter.models.CategoryModel;
import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.response.company.CompanyResponseModel;
import com.github.sasd97.upitter.models.response.fileServer.MediaResponseModel;
import com.github.sasd97.upitter.models.response.posts.PostResponseModel;
import com.github.sasd97.upitter.services.query.TapeQueryService;
import com.github.sasd97.upitter.ui.schemas.ShowOnMapActivity;
import com.github.sasd97.upitter.utils.Categories;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.ListUtils;
import com.github.sasd97.upitter.utils.Palette;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.COORDINATES_ATTACH;

/**
 * Created by alexander on 08.07.16.
 */

public class TapeRecyclerAdapter extends RecyclerView.Adapter<TapeRecyclerAdapter.TapeViewHolder> {

    private static final String TAG = "TAPE RECYCLER ADAPTER";
    private static final int FIRST_POSITION = 0;

    private Context context;

    private CompanyModel company;
    private List<PostResponseModel> posts;

    public TapeRecyclerAdapter(Context context, CompanyModel company) {
        posts = new ArrayList<>();
        this.context = context;
        this.company = company;
    }

    public class TapeViewHolder extends RecyclerView.ViewHolder
        implements TapeQueryService.OnTapeQueryListener,
        TapeQuizRecyclerAdapter.OnItemClickListener,
        Toolbar.OnMenuItemClickListener {

        private Toolbar postToolbar;

        private TextView userNameTextView;
        private TextView postTitleTextView;
        private TextView postDescriptionTextView;
        private TextView categoryNameTextView;
        private TextView likeAmountTextView;
        private TextView commentsAmountTextView;
        private TextView offsetFromNow;
        private TextView watchesAmountTextView;

        private ImageView userAvatarImageView;
        private CircleImageView categoryImageView;
        private ImageView commentImageView;

        private ShineButton likeShineButton;
        private ShineButton favoriteShineButton;

        private LinearLayout likeLinearLayout;
        private LinearLayout commentLinearLayout;

        private View.OnClickListener likeClick;
        private View.OnClickListener favoriteClick;

        private RecyclerView quizVariantsRecyclerView;
        private RecyclerView quizResultHorizontalChart;
        private RecyclerView imagesRecyclerView;

        private TapeQueryService queryService;

        public TapeViewHolder(View itemView) {
            super(itemView);

            queryService = TapeQueryService.getService(this);
            bind();
            setup();
        }

        private void bind() {
            postToolbar = (Toolbar) itemView.findViewById(R.id.post_toolbar);

            userNameTextView = (TextView) itemView.findViewById(R.id.user_name_post_single_view);
            postTitleTextView = (TextView) itemView.findViewById(R.id.title_post_single_view);
            postDescriptionTextView = (TextView) itemView.findViewById(R.id.text_post_single_view);
            categoryNameTextView = (TextView) itemView.findViewById(R.id.category_label_post_single_view);
            likeAmountTextView = (TextView) itemView.findViewById(R.id.like_counter_post_single_view);
            commentsAmountTextView = (TextView) itemView.findViewById(R.id.comments_counter_post_single_view);
            offsetFromNow = (TextView) itemView.findViewById(R.id.offset_from_now_post_single_view);
            watchesAmountTextView = (TextView) itemView.findViewById(R.id.watch_counter_post_single_view);

            userAvatarImageView = (ImageView) itemView.findViewById(R.id.user_icon_post_single_view);
            categoryImageView = (CircleImageView) itemView.findViewById(R.id.category_preview_post_single_view);
            commentImageView = (ImageView) itemView.findViewById(R.id.comments_icon_post_single_view);

            likeShineButton = (ShineButton) itemView.findViewById(R.id.like_icon_post_single_view);
            favoriteShineButton = (ShineButton) itemView.findViewById(R.id.favorites_icon_post_single_view);

            likeLinearLayout = (LinearLayout) itemView.findViewById(R.id.like_layout_post_single_view);
            commentLinearLayout = (LinearLayout) itemView.findViewById(R.id.comments_layout_post_single_view);

            quizVariantsRecyclerView = (RecyclerView) itemView.findViewById(R.id.quiz_variants_post_single_view);
            quizResultHorizontalChart = (RecyclerView) itemView.findViewById(R.id.quiz_result_post_single_view);
            imagesRecyclerView = (RecyclerView) itemView.findViewById(R.id.post_images_post_single_view);
        }

        private void setup() {
            postToolbar.setOnMenuItemClickListener(this);

            likeClick = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    likeShineButton.setChecked(true);
                    queryService.like(Locale.getDefault().getLanguage(),
                            company.getAccessToken(),
                            posts.get(getAdapterPosition()).getId());
                }
            };

            favoriteClick = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    queryService.favorite(Locale.getDefault().getLanguage(),
                            company.getAccessToken(),
                            posts.get(getAdapterPosition()).getId());
                }
            };

            likeShineButton.setOnClickListener(likeClick);
            likeLinearLayout.setOnClickListener(likeClick);
            favoriteShineButton.setOnClickListener(favoriteClick);

            quizResultHorizontalChart.setLayoutManager(new LinearLayoutManager(context));
            quizVariantsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            PostResponseModel post = posts.get(getAdapterPosition());

            switch (item.getItemId()) {
                case R.id.action_show_on_map:
                    Intent intent = new Intent(context, ShowOnMapActivity.class);
                    intent.putExtra(COORDINATES_ATTACH, post.toAuthorOnMapModel());
                    context.startActivity(intent);
                    break;
            }

            return false;
        }

        @Override
        public void onLike(PostResponseModel post) {
            posts.set(getAdapterPosition(), post);
            likeShineButton.showAnim();
            likeShineButton.setImageResource(R.drawable.ic_feed_icon_like_active);
            likeAmountTextView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            likeAmountTextView.setText(post.getLikesAmount());
        }

        @Override
        public void onDislike(PostResponseModel post) {
            posts.set(getAdapterPosition(), post);
            likeShineButton.setImageResource(R.drawable.ic_feed_icon_like);
            likeAmountTextView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            likeAmountTextView.setText(post.getLikesAmount());
        }

        @Override
        public void onAddFavorites(PostResponseModel post) {
            posts.set(getAdapterPosition(), post);
            favoriteShineButton.showAnim();
            favoriteShineButton.setImageResource(R.drawable.ic_feed_icon_favorite_active);
            likeAmountTextView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }

        @Override
        public void onVote(PostResponseModel post) {
            quizResultHorizontalChart.setVisibility(View.VISIBLE);
            quizVariantsRecyclerView.setVisibility(View.GONE);
            posts.set(getAdapterPosition(), post);

            quizResultHorizontalChart.setAdapter(new TapeQuizResultRecyclerAdapter(post.getQuiz(),
                    context.getString(R.string.voice_postfix),
                    posts.get(getAdapterPosition()).getVotersAmount()));
            quizResultHorizontalChart.setHasFixedSize(true);
        }

        @Override
        public void onRemoveFromFavorites(PostResponseModel post) {

        }

        @Override
        public void onError(ErrorModel error) {
            Log.d("TAPE_RECYCLER_ADAPTER", error.toString());
        }

        @Override
        public void onItemClick(int position) {
            queryService.vote(Locale.getDefault().getLanguage(),
                    company.getAccessToken(),
                    posts.get(getAdapterPosition()).getId(),
                    position);
        }
    }

    @Override
    public TapeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.post_single_view, parent, false);
        return new TapeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final TapeViewHolder holder, int position) {
        PostResponseModel post = posts.get(position);
        CompanyResponseModel author = post.getCompany();

        obtainPostAuthor(holder, author);
        obtainPost(holder, post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void addAll(List<PostResponseModel> posts) {
        this.posts.addAll(posts);
        notifyItemInserted(this.posts.size());
    }

    private void obtainPostAuthor(TapeViewHolder holder, CompanyResponseModel author) {
        obtainAvatar(holder.userAvatarImageView, author);
        holder.userNameTextView.setText(author.getName());
    }

    private void obtainAvatar(ImageView holder, CompanyResponseModel author) {
        if (author.getLogoUrl() == null) {
            TextDrawable textDrawable = TextDrawable
                    .builder()
                    .beginConfig()
                    .width(Dimens.dpToPx(28))
                    .height(Dimens.dpToPx(28))
                    .endConfig()
                    .buildRoundRect(author.getPreview(), Palette.getAvatarPalette(), Dimens.dpToPx(5));
            holder.setImageDrawable(textDrawable);
            return;
        }

        Glide
                .with(context)
                .load(author.getLogoUrl())
                .bitmapTransform(new CenterCrop(context), new RoundedCornersTransformation(context, Dimens.dpToPx(4), 0))
                .into(holder);
    }

    private void obtainPost(TapeViewHolder holder, PostResponseModel post) {
        obtainToolbar(holder.postToolbar, post.getCompany());

        obtainCategory(holder.categoryImageView, holder.categoryNameTextView, post.getCategoryId());
        obtainSubBar(holder.likeShineButton, holder.likeAmountTextView,
                holder.commentImageView, holder.commentsAmountTextView,
                holder.favoriteShineButton, post);
        obtainQuiz(holder, post);
        obtainCollage(holder, post);

        holder.offsetFromNow.setText(post.getDateFromNow());
        holder.postTitleTextView.setText(post.getTitle());
        holder.postDescriptionTextView.setText(post.getText());
        holder.watchesAmountTextView.setText(String.valueOf(post.getWatchesAmount()));
    }

    private void obtainToolbar(Toolbar toolbar, CompanyResponseModel author) {
        toolbar.getMenu().clear();

        if (company.getUId().equalsIgnoreCase(author.getId())) {
            toolbar.inflateMenu(R.menu.post_owner_single_view_menu);
            return;
        }

        toolbar.inflateMenu(R.menu.post_user_single_view_menu);
    }

    private void obtainCategory(CircleImageView imageHolder, TextView textHolder, String cid) {
        CategoryModel currentCategory = Categories.find(cid);
        textHolder.setText(currentCategory.getTitle());
        imageHolder.setImageResource(currentCategory.getDrawable());
    }

    private void obtainSubBar(ImageView likesImageHolder, TextView likesTextHolder,
                              ImageView commentsImageHolder, TextView commentsTextHolder,
                              ImageView favoritesImageHolder, PostResponseModel post) {

        if (post.isLikedByMe()) {
            likesImageHolder.setImageResource(R.drawable.ic_feed_icon_like_active);
            likesTextHolder.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }
        else {
            likesImageHolder.setImageResource(R.drawable.ic_feed_icon_like);
            likesTextHolder.setTextColor(ContextCompat.getColor(context, R.color.colorCounter));
        }

        likesTextHolder.setText(post.getLikesAmount());

        if (post.getCommentsAmount() > 0) {
            commentsImageHolder.setImageResource(R.drawable.ic_feed_icon_comment_active);
            commentsTextHolder.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }
        else {
            commentsImageHolder.setImageResource(R.drawable.ic_feed_icon_comment);
            commentsTextHolder.setTextColor(ContextCompat.getColor(context, R.color.colorCounter));
        }

        commentsTextHolder.setText(String.valueOf(post.getCommentsAmount()));
    }

    private void obtainQuiz(TapeViewHolder holder, PostResponseModel post) {
        if (post.getQuiz() == null || post.getQuiz().size() == 0) {
            holder.quizResultHorizontalChart.setVisibility(View.GONE);
            holder.quizVariantsRecyclerView.setVisibility(View.GONE);
            return;
        }

        if (post.isVotedByMe()) {
            holder.quizResultHorizontalChart.setVisibility(View.VISIBLE);
            holder.quizVariantsRecyclerView.setVisibility(View.GONE);
            holder.quizResultHorizontalChart.setAdapter(new TapeQuizResultRecyclerAdapter(post.getQuiz(),
                    context.getString(R.string.voice_postfix),
                    post.getVotersAmount()));
            holder.quizResultHorizontalChart.setHasFixedSize(true);
            return;
        }

        holder.quizResultHorizontalChart.setVisibility(View.GONE);
        holder.quizVariantsRecyclerView.setVisibility(View.VISIBLE);
        holder.quizVariantsRecyclerView.setAdapter(new TapeQuizRecyclerAdapter(post.getQuiz(), holder));
    }

    private void obtainCollage(final TapeViewHolder holder, PostResponseModel post) {
        if (post.getMedia() == null || post.getMedia().size() == 0) {
            holder.imagesRecyclerView.setVisibility(View.GONE);
            return;
        }

        CollageLayoutManager collageLayoutManager = new CollageLayoutManager(post.getMedia());
        CollageAdapter adapter = new CollageAdapter(context, post.getMedia());

        holder.imagesRecyclerView.setVisibility(View.VISIBLE);
        holder.imagesRecyclerView.setLayoutManager(collageLayoutManager);
        holder.imagesRecyclerView.setAdapter(adapter);
    }

    public String getFirstPostId() {
        return posts.get(FIRST_POSITION).getId();
    }

    public String getLastPostId() {
        return posts.get(getItemCount() - 1).getId();
    }

    public void addAhead(List<PostResponseModel> newPosts) {
        posts.addAll(FIRST_POSITION, newPosts);
        notifyItemInserted(newPosts.size() - 1);
    }

    public void addBehind(List<PostResponseModel> newPosts) {
        posts.addAll(newPosts);
        notifyItemInserted(posts.size());
    }
}
