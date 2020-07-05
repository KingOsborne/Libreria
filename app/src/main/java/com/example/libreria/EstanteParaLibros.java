package com.example.libreria;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;

public class EstanteParaLibros implements Parcelable {
    private String id;
    private String title;
    private String subtitle;
    private String authors;
    private String publisher;
    private String publishedDate;
    private int pageCount;
    private int averageRating;
    private String desc;
    private String thumbnail;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public EstanteParaLibros(String id, String title, String subtitle, String[] authors, String publisher, String publishedDate, int pageCount, int averageRating, String desc, String thumbnail) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        try{
            this.authors = TextUtils.join(", ",authors);
        }catch (Exception e){
            this.authors = "";
        }

        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.pageCount = pageCount;
        this.averageRating = averageRating;
        this.desc =desc;
        this.thumbnail = thumbnail;
    }

    protected EstanteParaLibros(Parcel in) {
        id = in.readString();
        title = in.readString();
        subtitle = in.readString();
        authors = in.readString();
        publisher = in.readString();
        publishedDate = in.readString();
        pageCount = in.readInt();
        averageRating = in.readInt();
        desc = in.readString();
        thumbnail = in.readString();
    }

    public static final Creator<EstanteParaLibros> CREATOR = new Creator<EstanteParaLibros>() {
        @Override
        public EstanteParaLibros createFromParcel(Parcel in) {
            return new EstanteParaLibros(in);
        }

        @Override
        public EstanteParaLibros[] newArray(int size) {
            return new EstanteParaLibros[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = TextUtils.join(", ",authors);
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublishers(String publishers) {
        this.publisher = publishers;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(int averageRating) {
        this.averageRating = averageRating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(authors);
        dest.writeString(publisher);
        dest.writeString(publishedDate);
        dest.writeInt(pageCount);
        dest.writeInt(averageRating);
        dest.writeString(desc);
        dest.writeString(thumbnail);
    }

    @BindingAdapter({"android:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl){
        if(!imageUrl.isEmpty()){
            Picasso.get().load(imageUrl).into(view);
        }else {
            view.setBackgroundResource(R.drawable.ic_baseline_menu_book_24);
        }

    }
}
